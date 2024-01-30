package verlet;

import processing.core.*;
import static processing.core.PApplet.*;

/**
 * Constraint for rectangalur areas.
 */
public class RectangleConstraint extends Constraint {
	  private PVector left_top;
	  private PVector right_bottom;

	  public RectangleConstraint(PVector left_top, PVector right_bottom) {
		  this.left_top = new PVector(min(left_top.x, right_bottom.x), min(left_top.y, right_bottom.y));
		  this.right_bottom = new PVector(max(left_top.x, right_bottom.x), max(left_top.y, right_bottom.y));
	  }

	/**
	 * Constrain a point to the given rectangle.
	 * 
	 * @param p The point to be constrained.
	 */
	public void constrain(Point p) {
		PVector v = PVector.sub(p.pos, p.oldPos).mult(p.friction);

		if(p.pos.y <= left_top.y + p.radius) {
			p.pos.y = left_top.y + p.radius;
			p.oldPos.y = p.pos.y + v.y * surfaceFriction;
		}

		if(p.pos.x <= left_top.x + p.radius) {
			p.pos.x = left_top.x + p.radius;
			p.oldPos.x = p.pos.x + v.x * surfaceFriction;
		}

		if(p.pos.y >= right_bottom.y - p.radius) {
			p.pos.y = right_bottom.y - p.radius;
			p.oldPos.y = p.pos.y + v.y * surfaceFriction;
		}

		if(p.pos.x >= right_bottom.x - p.radius) {
			p.pos.x = right_bottom.x - p.radius;
			p.oldPos.x = p.pos.x + v.x * surfaceFriction;
		}
	}
}
