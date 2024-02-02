package verlet;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import processing.core.PVector;

public class Entity {
	private Constraint constraint;

	public Set<Point> points = new HashSet<Point>();
	public Set<Line> lines = new HashSet<Line>();
	public int iterations = 1;

	public Entity(Constraint constraint) {
		this.constraint = constraint;
	}

	public void setPoints(Collection<Point> points) {
		this.points = new HashSet<Point>();
		this.points.addAll(points);
	}

	public void addPoints(Collection<Point> points) {
		this.points.addAll(points);
	}

	public Point addPoint(Point p) {
		points.add(p);

		return p;
	}

	public Point addPoint(PVector p, PVector v) {
		Point point = new Point(p, v);

		points.add(point);

		return point;
	}

	public void setLines(Collection<Line> lines) {
		for (Line l : lines) {
			points.add(l.p1);
			points.add(l.p2);
		}

		this.lines = new HashSet<Line>();
		this.lines.addAll(lines);
	}

	public void addLines(Collection<Line> lines) {
		for (Line l : lines) {
			points.add(l.p1);
			points.add(l.p2);
		}

		this.lines.addAll(lines);
	}

	public Line addLine(Line l) {
		points.add(l.p1);
		points.add(l.p2);

		lines.add(l);

		return l;
	}

	public Line addLine(Point p1, Point p2) {
		points.add(p1);
		points.add(p2);

		Line l = new Line(p1, p2);

		lines.add(l);

		return l;
	}

	public Line addLine(Point p1, Point p2, float length) {
		points.add(p1);
		points.add(p2);

		Line l = new Line(p1, p2, length);

		lines.add(l);

		return l;
	}

	public void movePoints() {
		for (Point p : points) {
			p.move();
		}
	}

	public void constrainPoints() {
		for (Point p : points) {
			constraint.constrain(p);
		}
	}

	public void constrainLines() {
		for (Line l : lines) {
			l.constrain();
		}
	}

	public void update() {
		movePoints();

		for (int i = 0; i < iterations; i++) {
			constrainLines();
			constrainPoints();
		}
	}

	public void draw() {
		for (Point p : points) {
			p.draw();
		}

		for (Line l : lines) {
			l.draw();
		}
	}
}
