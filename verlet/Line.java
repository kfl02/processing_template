package verlet;

import processing.core.PVector;

public class Line {
	public Point p1;
	public Point p2;
	public boolean draw;
	public float length;
	public float stiffness = 1.0f;
	
	protected Line() {}

	public Line(Point p1, Point p2) {
		this.p1 = p1;
		this.p2 = p2;
		length = PVector.sub(p1.pos, p2.pos).mag();
	}

	public Line(Point p1, Point p2, float length) {
		this.p1 = p1;
		this.p2 = p2;
		this.length = length;
	}

	public void move() {
		PVector d = PVector.sub(p2.pos, p1.pos);

		float dist = d.mag();
		float diff = (length - dist) / dist / 2.0f * stiffness;

		d.mult(diff);

		if(p2.fixed) {
			if(!p1.fixed) {
				p1.pos.sub(d.mult(2.0f));
			}
		} else if(p1.fixed) {
			if(!p2.fixed) {
				p2.pos.add(d.mult(2.0f));
			}
		} else {
			p1.pos.sub(d);
			p2.pos.add(d);
		}
	}

	public void draw() {
	}

	public String toString() {
		return "(" + p1.toString() + ", " + p2.toString() + ")";
	}
}
