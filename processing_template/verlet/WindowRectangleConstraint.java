package verlet;

import processing.core.PApplet;
import processing.core.PVector;

public class WindowRectangleConstraint extends RectangleConstraint {
	public WindowRectangleConstraint(PApplet app) {
		super(new PVector(0.0f, 0.0f), new PVector(app.width, app.height));
	}
}
