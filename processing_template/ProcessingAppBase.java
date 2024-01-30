package processing_template;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hamoid.VideoExport;

import processing.core.PApplet;

public class ProcessingAppBase extends PApplet {
	private VideoExport videoExport;
	private boolean video_running = false;
	protected boolean animate = false;

	public class Point extends verlet.Point {
		@Override
		public void draw() {
			if (draw) {
				circle(pos.x, pos.y, radius * 2);
			}
		}
	}

	public class Line extends verlet.Line {
		@Override
		public void draw() {
			if (draw) {
				line(p1.pos.x, p1.pos.y, p2.pos.x, p2.pos.y);
			}
		}
	}

	@Override
	public void setup() {
		videoExport = new VideoExport(this, getClass().getName() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ".mp4");

		videoExport.setQuality(70, 128);
		videoExport.setFrameRate(frameRate);
	}

	@Override
	public void draw() {
		if (animate) {
			animate();
		}

		if (video_running) {
			videoExport.saveFrame();
		}
	}

	void animate() {
	}

	@Override
	public void keyPressed() {
		if (key == '#') {
			if (!video_running) {
				videoExport.startMovie();

				video_running = true;

				return;
			} else {
				video_running = false;

				videoExport.endMovie();

				exit();
			}
		}

		if (key == 'm') {
			animate = !animate;
		}
	}
}
