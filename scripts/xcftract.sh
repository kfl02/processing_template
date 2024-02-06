shopt -s extglob

typeset -a tile_name
typeset -a tile_operators

# Crop to 7x9 equally sized tiles with 10 pixels overlap in both directions
# -background none -virtual-pixel background -set option:distort:viewport %[fx:ceil(w/7)*7+10]x%[fx:ceil(h/9)*9+10] -distort SRT 0 +repage -crop 7x9+10+10@

while getopts ":i:t:o:" opt
do
	case "$opt" in
		i)	ignore_filter="$OPTARG"
			;;
		t)	tile_name+=("${OPTARG%:*}")
			tile_operators+=("-crop ${OPTARG#*:}")
			;;
		o)	tile_name+=("${OPTARG%%:*}")
			tile_operators+=("${OPTARG#*:}")
			;;
		*)	echo "illegal option '$opt'"
			exit 1
			;;
	esac
done

shift $((OPTIND - 1))

xcf=$1
xcf_base=$(basename ${xcf%.xcf})
xcf_base=${xcf_base^}
dir=$(dirname $xcf)
typeset -i file_idx=0
typeset -i java_idx=0
typeset -i tile_java_idx=0

while IFS=$'\t' read -r name dim
do
	if [[ $name == $ignore_filter ]]
	then
		: echo "IGNORING $name $dim"
	else
		j=${name%.png}	# TODO: other known file extensions
		j=${j//[^[:word:]]/_}
		# w=${dim%x*}
		# h=${dim#*x}
		# h=${h%%+*}
		x=${dim#*+}
		x=${x%+*}
		y=${dim#*+}
		y=${y#*+}

		tile_match=
		typeset -i tile_idx=0

		for t in ${tile_name[@]}
		do
			if [[ $name == $t ]]
			then
				tile_match=1
				break
			fi

			tile_idx+=1
		done

		if [[ -n $tile_match ]]
		then
			rm -f ${j}_+([[:digit]]).png

			convert "$xcf[$file_idx]" +repage ${tile_operators[$tile_idx]} $dir/${j}_%d.png

			typeset -i n=$(ls $dir/${j}_+([[:digit:]]).png | sed -E "s/.*${j}_([[:digit:]]+)\.png/\1/" | sort -rn | head -1)

			# ws=$(eval identify -format '%w,' ${j}_{0..$n}.png)
			# ws=${ws::-1}
			# ws=${ws//+/}
			# ws=${ws//,/, }
			# hs=$(eval identify -format '%h,' ${j}_{0..$n}.png)
			# hs=${hs::-1}
			# hs=${hs//+/}
			# hs=${hs//,/, }
			xs=$(eval identify -format "%[fx:page.x+${x}]," $dir/${j}_{0..$n}.png)
			xs=${xs::-1}
			xs=${xs//,+/,}
			xs=${xs//,/, }
			ys=$(eval identify -format "%[fx:page.y+${y}]," $dir/${j}_{0..$n}.png)
			ys=${ys::-1}
			ys=${ys//,+/,}
			ys=${ys//,/, }
			
			eval 'for i in {0..'$n'}; do convert $dir/${j}_${i}.png +repage $dir/${j}_${i}.png; done'
			
			tile_java_name[$tile_java_idx]=$j
			tile_number[$tile_java_idx]=$((n + 1))
			# tile_width[$tile_java_idx]=$ws
			# tile_height[$tile_java_idx]=$hs
			tile_xoffset[$tile_java_idx]=$xs
			tile_yoffset[$tile_java_idx]=$ys

			tile_java_idx+=1
		else
			convert "$xcf[$file_idx]" +repage $dir/${j}.png

			java_name[$java_idx]=$j
			# width[$java_idx]=$w
			# height[$java_idx]=$h
			xoffset[$java_idx]=$x
			yoffset[$java_idx]=$y

			java_idx+=1
		fi
	fi

	file_idx+=1
done < <(identify -format '%l	%g\n' $xcf)

echo "package processing_template;"
echo 
echo "import processing.core.PImage;"
echo "import processing.core.PApplet;"
echo 
echo "public class ${xcf_base}ImageLoader extends ImageLoader {"

if (( $java_idx > 0 ))
then
	for i in ${!java_name[@]}
	do
		if [[ $i == 0 ]]
		then
			echo "	public static final int IDX_${java_name[$i]^^} = 0;	// ${i}"
		else
			echo "	public static final int IDX_${java_name[$i]^^} = IDX_${java_name[$((i - 1))]^^} + 1;	// ${i}"
		fi
	done

	for i in ${!java_name[@]}
	do
		echo "	public PImage img_${java_name[$i]};"
	done
fi

if (( $tile_java_idx > 0 ))
then
	for i in ${!tile_java_name[@]}
	do
		if [[ $i == 0 ]]
		then
			echo "	public final int IDX_${tile_java_name[$i]^^} = 0;	// ${i}"
		else
			echo "	public final int IDX_${tile_java_name[$i]^^} = IDX_${tile_java_name[$((i - 1))]^^} + 1;	// ${i}"
		fi
	done

	for i in ${!tile_java_name[@]}
	do
		echo "	public PImage[] tile_imgs_${tile_java_name[$i]};"
	done
fi

echo 
echo "	public ${xcf_base}ImageLoader(PApplet app) {"
echo "		super(app, ${java_idx}, ${tile_java_idx});"

if (( $java_idx > 0 ))
then
	echo "		image_names = new String[] {"

	for i in ${!java_name[@]}
	do
		echo -n "			\"$dir/${java_name[$i]}.png\""

		if [[ $i == $((java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
	echo "		x_offs = new int[] {"

	for i in ${!java_name[@]}
	do
		echo -n "			${xoffset[$i]}"

		if [[ $i == $((java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
	echo "		y_offs = new int[] {"

	for i in ${!java_name[@]}
	do
		echo -n "			${yoffset[$i]}"

		if [[ $i == $((java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
fi

if (( $tile_java_idx > 0 ))
then
	echo "		tile_image_num = new int[] {"

	for i in ${!tile_java_name[@]}
	do
		echo -n "			${tile_number[$i]}"

		if [[ $i == $((tile_java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
	echo "		tile_image_names = new String[] {"

	for i in ${!tile_java_name[@]}
	do
		echo -n "			\"$dir/${tile_java_name[$i]}_%d.png\""

		if [[ $i == $((tile_java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
	echo "		tile_x_offs = new int[][] {"

	for i in ${!tile_java_name[@]}
	do
		echo -n "			{ ${tile_xoffset[$i]} }"

		if [[ $i == $((tile_java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
	echo "		tile_y_offs = new int[][] {"

	for i in ${!tile_java_name[@]}
	do
		echo -n "			{ ${tile_yoffset[$i]} }"

		if [[ $i == $((tile_java_idx - 1)) ]]
		then
			echo "	// ${i}"
		else
			echo ",	// ${i}"
		fi
	done

	echo "		};"
fi

if (( $java_idx > 0 ))
then
	echo
	echo "		for(int i = 0; i < $java_idx; i++) {"
	echo "			images[i] = app.loadImage(image_names[i]);"
	echo "		}"

	for i in ${!java_name[@]}
	do
		echo "		img_${java_name[$i]} = images[IDX_${java_name[$i]^^}];"
	done
fi

if (( $tile_java_idx > 0 ))
then
	echo
	echo "		for(int i = 0; i < $tile_java_idx; i++) {"
	echo "			tile_images[i] = new PImage[tile_image_num[i]];"
	echo "			for(int j = 0; j < tile_image_num[i]; j++) {"
	echo "				tile_images[i][j] = app.loadImage(String.format(image_names[i], j));"
	echo "			}"
	echo "		}"

	for i in ${!tile_java_name[@]}
	do
		echo "		tile_imgs_${tile_java_name[$i]} = tile_images[IDX_${tile_java_name[$i]^^}];"
	done
fi

echo "	}"
echo "}"
