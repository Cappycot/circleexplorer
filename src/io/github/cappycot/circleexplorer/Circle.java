package io.github.cappycot.circleexplorer;

/**
 * Object holding x and y coordinates.
 * 
 * @author Chris Wang
 */
public class Circle {
	/* Instance Variables */
	private double x;
	private double y;
	
	/* Constructor */
	public Circle(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/* Getters */
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	/* Setters */
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	/* Operations */
	public double distFrom(double x, double y) {
		return Math.sqrt((Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)));
	}
}
