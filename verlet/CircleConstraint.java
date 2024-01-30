package verlet;

import processing.core.PVector;

public class CircleConstraint extends Constraint {
	private PVector center;
	private float radius;

	public CircleConstraint(PVector center, float radius) {
		this.center = center;
		this.radius = radius;
	}

	/**
	 * Constrain a point to the given circle.
	 * 
	 * @param p The point to be constrained.
	 */
	public void constrain(Point p) {
		PVector v = PVector.sub(p.pos, p.oldPos).mult(p.friction);
		float vm = v.mag();

		PVector po = PVector.sub(p.pos, center);
		float len = po.mag();

		if(len > radius - p.radius) {
			PVector np = PVector.mult(po, (radius - p.radius) / len).add(center);
			p.pos.set(np);

			PVector nv = po.copy().mult(vm / len * surfaceFriction);
			PVector onp = PVector.add(np, nv);

			p.oldPos.set(onp);
		}
	}
}
