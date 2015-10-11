package io.github.cappycot.circleexplorer;

import static io.github.cappycot.circleexplorer.MainPanel.toPixels;

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
	public static final double MAX_SPEED = 0.096;
	public static final double MIN_SPEED = 0.004;
	public static final Random RANDOM = new Random();
	public static final double RADIUS = 0.02;
	/* Instance Variables */
	private double x; // x-coordinate of center
	private double y; // y-coordinate of center
	private Color color;
	private Color highlight;
	private ArrayList<ArrayList<Circle>> circles;
	private volatile boolean selectable = true;

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
		color = new Color(RANDOM.nextInt(96) + 32, RANDOM.nextInt(128) + 128,
				RANDOM.nextInt(128) + 128);
		highlight = new Color(RANDOM.nextInt(128) + 128,
				RANDOM.nextInt(96) + 32, RANDOM.nextInt(96) + 32);
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

	public void reset(MainPanel mp) {
		int size = getSize();
		int cluster = getCluster();
		ArrayList<Circle> holder = new ArrayList<Circle>(getTotal());
		for (ArrayList<Circle> cg : circles)
			holder.addAll(cg);
		while (holder.size() < getTotal()) {
			holder.add(new Circle(x, y));
		}
		// Collections.shuffle(holder);
		while (holder.size() > getTotal()) {
			mp.kill(holder.remove(holder.size() - 1), color);
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

	/* Getters */
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public Color getColor() {
		return color;
	}

	public boolean isSelectable() {
		return selectable;
	}

	/* Operation Methods */
	public void move(double x, double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public Grouping rotate() {
		super.rotate();
		reset(null);
		return this;
	}

	public void incSize(int inc, MainPanel mp) {
		super.incSize(inc);
		reset(mp);
	}

	public void incCluster(int inc, MainPanel mp) {
		super.incCluster(inc);
		reset(mp);
	}

	public void merge(RenderGroup group, MainPanel mp) {
		super.merge(group);
		for (int i = 0; i < getSize(); i++) {
			ArrayList<Circle> holder = new ArrayList<Circle>(group.getTotal());
			for (ArrayList<Circle> cg : group.circles) {
				if (i == getSize() - 1) {
					while (cg.size() > 0)
						holder.add(cg.remove(0));
				} else
					for (Circle c : cg)
						holder.add(new Circle(c));
			}
			circles.get(i).addAll(holder);
		}
		mp.kill(group);
		reset(mp);
	}

	public RenderGroup append(RenderGroup group, MainPanel mp, double mx,
			double my) {
		Grouping extra = super.append(group);
		mp.kill(group);
		reset(mp);
		if (extra == null)
			return null;
		return new RenderGroup(extra, mx, my);
	}

	public ArrayList<Circle> kill() {
		ArrayList<Circle> holder = new ArrayList<Circle>(getTotal());
		for (ArrayList<Circle> cg : circles)
			holder.addAll(cg);
		while (holder.size() < getTotal()) {
			holder.add(new Circle(x, y));
		}
		circles = null;
		return holder;
	}

	/* Graphics Methods */
	public boolean inInner(double mx, double my) {
		return mx >= getInnerLeft() && mx <= getInnerRight()
				&& my >= getInnerTop() && my <= getInnerBottom();
	}

	public boolean inOuter(double mx, double my) {
		return mx >= getOuterLeft() && mx <= getOuterRight()
				&& my >= getOuterTop() && my <= getOuterBottom();
	}

	public double getInnerLeft() {
		return x - (getCluster() + 1) * RADIUS / 2;
	}

	public double getInnerRight() {
		return x + (getCluster() + 1) * RADIUS / 2;
	}

	public double getInnerTop() {
		return y - getSize() * RADIUS;
	}

	public double getInnerBottom() {
		return y + getSize() * RADIUS;
	}

	public double getInnerWidth() {
		return (getCluster() + 1) * RADIUS;
	}

	public double getInnerHeight() {
		return getSize() * RADIUS * 2;
	}

	public double getOuterLeft() {
		return getInnerLeft() - RADIUS / 2;
	}

	public double getOuterRight() {
		return getInnerRight() + RADIUS / 2;
	}

	public double getOuterTop() {
		return getInnerTop() - RADIUS / 2;
	}

	public double getOuterBottom() {
		return getInnerBottom() + RADIUS / 2;
	}

	public double getOuterWidth() {
		return getInnerWidth() + RADIUS;
	}

	public double getOuterHeight() {
		return getInnerHeight() + RADIUS;
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
	public void draw(Graphics g, double x, double y, boolean move,
			boolean outer, boolean inner, boolean center) {
		if (inner)
			outer = false;
		if (center)
			inner = false;
		int height = getSize();
		int length = getCluster();
		g.setColor(Color.WHITE);
		if (outer) {
			g.fillRect((int) toPixels(getOuterLeft()) - 2,
					(int) toPixels(getOuterTop()) - 2,
					(int) toPixels(getOuterWidth()) + 4,
					(int) toPixels(RADIUS / 2) + 4);
			g.fillRect((int) toPixels(getOuterLeft()) - 2,
					(int) toPixels(getOuterTop()) - 2,
					(int) toPixels(RADIUS / 2) + 4,
					(int) toPixels(getOuterHeight()) + 4);
			g.fillRect((int) toPixels(getOuterLeft()) - 2,
					(int) toPixels(getInnerBottom()) - 2,
					(int) toPixels(getOuterWidth()) + 4,
					(int) toPixels(RADIUS / 2) + 4);
			g.fillRect((int) toPixels(getInnerRight()) - 2,
					(int) toPixels(getOuterTop()) - 2,
					(int) toPixels(RADIUS / 2) + 4,
					(int) toPixels(getOuterHeight()) + 4);
			g.setColor(inner ? color : highlight);
			g.fillRect((int) toPixels(getOuterLeft()),
					(int) toPixels(getOuterTop()),
					(int) toPixels(getOuterWidth()), (int) toPixels(RADIUS / 2));
			g.fillRect((int) toPixels(getOuterLeft()),
					(int) toPixels(getOuterTop()), (int) toPixels(RADIUS / 2),
					(int) toPixels(getOuterHeight()));
			g.fillRect((int) toPixels(getOuterLeft()),
					(int) toPixels(getInnerBottom()),
					(int) toPixels(getOuterWidth()), (int) toPixels(RADIUS / 2));
			g.fillRect((int) toPixels(getInnerRight()),
					(int) toPixels(getOuterTop()), (int) toPixels(RADIUS / 2),
					(int) toPixels(getOuterHeight()));
		}
		if (move)
			selectable = true;
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < length; i++) {
				Circle c = circles.get(j).get(i);
				if (move) {
					double destX = x
							+ (center ? 0 : -(length - 1) * RADIUS / 2 + i
									* RADIUS);
					double destY = y
							+ (center ? 0 : -(height - 1) * RADIUS + j * RADIUS
									* 2);
					double dist = c.distFrom(destX, destY);
					double spd = MIN_SPEED + dist * MAX_SPEED;
					if (dist <= spd) {
						c.setX(destX);
						c.setY(destY);
					} else {
						selectable = false;
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
				g.setColor(inner ? highlight : color);
				g.fillOval((int) toPixels(c.getX() - RADIUS),
						(int) toPixels(c.getY() - RADIUS),
						(int) toPixels(2 * RADIUS), (int) toPixels(2 * RADIUS));
			}
		}
	}

	public void draw(Graphics g, double x, double y, boolean move) {
		draw(g, x, y, move, false, false, false);
	}

	public void draw(Graphics g, boolean move) {
		draw(g, x, y, move, false, false, false);
	}
}
