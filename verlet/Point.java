package verlet;

import processing.core.PVector;

public class Point {
	public PVector pos;
	public PVector oldPos;
	public PVector gravity = new PVector(0.0f, 0.9f);
	public float radius = 0.0f;
	public float friction = 0.999f;
	public boolean fixed = false;
	public boolean draw = false;

	protected Point() {
	}

	/**
	 * Construct a particle from a PVector with no initial velocity.
	 * 
	 * @param pos The particle's position.
	 */
	public Point(PVector pos) {
		this.pos = pos.copy();
		oldPos = pos.copy();
	}

	/**
	 * Construct a particle from a PVector with initial velocity.
	 * 
	 * @param pos The particle's position.
	 * @param v   The initial velosity.
	 */
	public Point(PVector pos, PVector v) {
		this.pos = pos.copy();
		oldPos = new PVector(pos.x - v.x, pos.y - v.y);
	}

	/**
	 * Construct a particle from given coordiantes with no initial velocity.
	 * 
	 * @param x The particle's X coordinate.
	 * @param y The particle's Y coordinate.
	 */
	public Point(float x, float y) {
		pos = new PVector(x, y);
		oldPos = new PVector(x, y);
	}

	/**
	 * Construct a particle from given coordiantes with initial velocity.
	 * 
	 * @param x  The particle's X coordinate.
	 * @param y  The particle's Y coordinate.
	 * @param vx The initial velosity in X direction.
	 * @param vy The initial velosity in Y direction.
	 */
	public Point(float x, float y, float vx, float vy) {
		pos = new PVector(x, y);
		oldPos = new PVector(x - vx, y - vy);
	}

	/**
	 * Move a particle, if it is not fixed.
	 * 
	 * The new position is determined by the difference of the old and the current
	 * position,times the friction, plus the gravity.
	 */
	public void move() {
		if (fixed) {
			return;
		}

		PVector v = PVector.sub(pos, oldPos).mult(friction);

		oldPos.set(pos);
		PVector np = pos.copy().add(v).add(gravity);

		pos = np;
	}

	public void draw() {
	}

	@Override
	public String toString() {
		return "(" + Float.toString(pos.x) + ", " + Float.toString(pos.y) + ")";
	}
}
