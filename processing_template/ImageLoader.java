package processing_template;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageLoader {
    protected PApplet app;
    public int num_images;
    public PImage[] images = null;
    public String[] image_names = null;
    public int[] x_offs = null;
    public int[] y_offs = null;
    public int num_tile_images;
    public PImage[][] tile_images = null;
    public String[] tile_image_names = null;
    public int[] tile_image_num = null;
    public int[][] tile_x_offs = null;
    public int[][] tile_y_offs = null;

    protected ImageLoader() {
    }

    public ImageLoader(PApplet app, int numImages, int numTileImages) {
        this.app = app;
        num_images = numImages;
        num_tile_images = numTileImages;

        if (num_images > 0) {
            images = new PImage[num_images];
        }

        if (num_tile_images > 0) {
            tile_images = new PImage[num_tile_images][];
        }
    }
}
