package util;

import processing.core.PImage;

public class Image {
	/**
	 * Check whether image is transparent only.
	 *
	 * @param img	The image to analyse.
	 * @return true if image is transparent
	 */
	public static boolean checkAlpha(PImage img) {
		img.loadPixels();

		for (int i = 0; i < img.pixels.length; i++) {
			if (img.pixels[i] != 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Return rectangular area of image that contains non-trnasparent pixels.
	 * 
	 * @param img
	 * @return
	 */
	public static int[] pictureRange(PImage img) {
		int[] ret = { Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };

		img.loadPixels();

		for(int y = 0; y < img.height; y++) {
			for(int x = 0; x < img.width; x++) {
				if(img.pixels[y * img.width + x] != 0) {
					if(x > ret[0]) {
						ret[0] = x;
					}
					if(x < ret[2]) {
						ret[2] = x;
					}
					if(y > ret[1]) {
						ret[1] = y;
					}
					if(y < ret[3]) {
						ret[3] = y;
					}
				}
			}
		}

		return ret;
	}
}
