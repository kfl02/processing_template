package verlet;

public abstract class Constraint {
	  public float surfaceFriction = 0.5f;

	  abstract void constrain(Point p);
}
