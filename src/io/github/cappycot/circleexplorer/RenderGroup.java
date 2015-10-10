package io.github.cappycot.circleexplorer;

import java.awt.Color;
import java.awt.Graphics;

public class RenderGroup extends Grouping {
	/* Global Variables */
	public static final double MAX_SPEED = 59 / 60D;
	public static final double MIN_SPEED = 1 / 60D;
	/* Instance Variables */
	private Color color;

	public RenderGroup(int size, int cluster) {
		super(size, cluster);
	}

	public RenderGroup(Grouping group) {
		super(group);
	}

	/**
	 * 
	 * @param g graphics
	 * @param x center
	 * @param y center
	 * @param scale 1.0 to width
	 */
	public void draw(Graphics g, double x, double y, double scale) {
		
	}
}
