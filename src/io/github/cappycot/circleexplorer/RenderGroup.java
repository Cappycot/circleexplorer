package io.github.cappycot.circleexplorer;

import static io.github.cappycot.circleexplorer.MainPanel.toPixels;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Extension of Grouping for Graphics.
 * 
 * @author Chris Wang
 */
public class RenderGroup extends Grouping {
	/* Global Variables */
	public static final double MAX_SPEED = 0.049;
	public static final double MIN_SPEED = 0.001;
	public static final Random RANDOM = new Random();
	public static final double RADIUS = 0.02;
	/* Instance Variables */
	private double x; // x-coordinate of center
	private double y; // y-coordinate of center
	private Color color;
	private ArrayList<ArrayList<Circle>> circles;

	/* Constructors */
	public RenderGroup(int size, int cluster, double x, double y) {
		super(size, cluster);
		this.x = x;
		this.y = y;
		init();
	}

	public RenderGroup(Grouping group, double x, double y) {
		super(group);
		this.x = x;
		this.y = y;
		init();
	}

	public void init() {
		color = new Color(RANDOM.nextInt(128) + 64, RANDOM.nextInt(128) + 96,
				RANDOM.nextInt(128) + 128);
		int size = getSize();
		int cluster = getCluster();
		circles = new ArrayList<ArrayList<Circle>>(size);
		for (int i = 0; i < size; i++) {
			ArrayList<Circle> group = new ArrayList<Circle>(cluster);
			for (int j = 0; j < cluster; j++) {
				group.add(new Circle(x, y));
			}
			circles.add(group);
		}
	}
	
	public void reset() {
		int size = getSize();
		int cluster = getCluster();
		ArrayList<Circle> holder = new ArrayList<Circle>(getTotal());
		for (ArrayList<Circle> cg : circles)
			holder.addAll(cg);
		while (holder.size() < getTotal()) {
			holder.add(new Circle(x, y));
		}
		Collections.shuffle(holder, RANDOM);
		while (holder.size() > getTotal()) {
			holder.remove(0);
		}
		circles = new ArrayList<ArrayList<Circle>>(size);
		for (int i = 0; i < size; i++) {
			ArrayList<Circle> group = new ArrayList<Circle>(cluster);
			for (int j = 0; j < cluster; j++) {
				group.add(holder.remove(0));
			}
			circles.add(group);
		}
	}
	/* Operation Methods */
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Grouping rotate() {
		super.rotate();
		reset();
		return this;
	}
	/* Graphics Methods */
	/**
	 * 
	 * @param g
	 *            graphics
	 * @param x
	 *            center
	 * @param y
	 *            center
	 * @param scale
	 *            1.0 to width
	 */
	public void draw(Graphics g, double x, double y, boolean move,
			boolean center) {
		int height = getSize();
		int length = getCluster();
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < length; i++) {
				Circle c = circles.get(j).get(i);
				if (move) {
					double destX = x
							+ (center ? 0 : - (length - 1) * RADIUS / 2 + i
									* RADIUS);
					double destY = y - (height - 1) * RADIUS + j * RADIUS * 2;
					double dist = c.distFrom(destX, destY);
					double spd = MIN_SPEED + dist * MAX_SPEED;
					if (dist <= spd) {
						c.setX(destX);
						c.setY(destY);
					} else {
						double theta = Math.atan2(destY - c.getY(),
								destX - c.getX());
						double moveX = spd * Math.cos(theta);
						double moveY = spd * Math.sin(theta);
						c.setX(c.getX() + moveX);
						c.setY(c.getY() + moveY);
					}
				}
				g.setColor(Color.WHITE);
				g.fillOval((int) toPixels(c.getX() - RADIUS) - 2,
						(int) toPixels(c.getY() - RADIUS) - 2,
						(int) toPixels(2 * RADIUS) + 4,
						(int) toPixels(2 * RADIUS) + 4);
				g.setColor(color);
				g.fillOval((int) toPixels(c.getX() - RADIUS),
						(int) toPixels(c.getY() - RADIUS),
						(int) toPixels(2 * RADIUS), (int) toPixels(2 * RADIUS));
			}
		}
	}

	public void draw(Graphics g, double x, double y, boolean move) {
		draw(g, x, y, move, false);
	}

	public void draw(Graphics g, boolean move) {
		draw(g, x, y, move, false);
	}
}
