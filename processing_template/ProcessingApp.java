package processing_template;

import processing.core.PApplet;

public class ProcessingApp extends ProcessingAppBase {
    public static void main(String[] args) {
        PApplet.runSketch(new String[] { "MAIN" }, new ProcessingApp());
    }

    @Override
    public void settings() {
        size(1000, 1000);
    }

    @Override
    public void setup() {
        frameRate(60);

        // call after setting framRate to initialize video
        super.setup();
    }

    @Override
    public void draw() {
        // call after drawing everything else to save video frame
        super.draw();
    }

    @Override
    public void keyPressed() {
        // call first to allow video recording toggle
        super.keyPressed();
    }
}
