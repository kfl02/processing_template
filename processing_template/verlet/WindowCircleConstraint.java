package verlet;

import processing.core.PApplet;
import processing.core.PVector;
import static processing.core.PApplet.min;

public class WindowCircleConstraint extends CircleConstraint {
	public WindowCircleConstraint(PApplet app) {
		super(new PVector(app.width / 2.0f, app.height / 2.0f), min(app.width / 2.0f, app.height / 2.0f));
	}
}
