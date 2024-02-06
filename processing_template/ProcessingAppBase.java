package processing_template;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.hamoid.VideoExport;

import processing.core.PApplet;
import processing.core.PVector;

public class ProcessingAppBase extends PApplet {
    private VideoExport videoExport;
    private boolean video_running = false;
    protected boolean animate = false;
    protected ImageLoader img;

    public class Point extends verlet.Point {
        public Point(PVector pos) {
            super(pos);
        }

        public Point(PVector pos, PVector v) {
            super(pos, v);
        }

        public Point(float x, float y) {
            super(x, y);
        }

        public Point(float x, float y, float vx, float vy) {
            super(x, y, vx, vy);
        }

        @Override
        public void draw() {
            if (draw) {
                circle(pos.x, pos.y, radius * 2);
            }
        }
    }

    public class Line extends verlet.Line {
        public Line(Point p1, Point p2) {
            super(p1, p2);
        }

        public Line(Point p1, Point p2, float length) {
            super(p1, p2, length);
        }

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
        switch (key) {
            case '#':
                if (!video_running) {
                    videoExport.startMovie();

                    video_running = true;

                    return;
                } else {
                    video_running = false;

                    videoExport.endMovie();

                    exit();
                }
                break;

            case 'm':
                animate = !animate;
                break;

            case '.':
                if (!animate) {
                    animate();
                }
                break;
        }
    }
}
