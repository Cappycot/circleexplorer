package io.github.cappycot.circleexplorer;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * Extension of Grouping for Graphics.
 * 
 * @author Chris Wang
 */
public class RenderGroup extends Grouping {
	/* Global Variables */
	public static final double MAX_SPEED = 49 / 60D;
	public static final double MIN_SPEED = 11 / 60D;
	public static final Random RANDOM = new Random();
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
		color = new Color(RANDOM.nextInt(128) + 128, RANDOM.nextInt(128) + 128,
				RANDOM.nextInt(128) + 128);
		int size = getSize();
		int cluster = getCluster();
		circles = new ArrayList<ArrayList<Circle>>(size);
		for (int i = 0; i < size; i++) {
			ArrayList<Circle> group = new ArrayList<Circle>(cluster);
			for (int j = 0; j < cluster; j++) {
				group.add(new Circle(x, y));
			}
		}
	}

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
	public void draw(Graphics g, double x, double y, double scale) {

	}
}
