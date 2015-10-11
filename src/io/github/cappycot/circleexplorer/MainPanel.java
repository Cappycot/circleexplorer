package io.github.cappycot.circleexplorer;

import static io.github.cappycot.circleexplorer.RenderGroup.RADIUS;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This is where all the fun happens.
 * 
 * @author Chris Wang
 */
public class MainPanel extends DontEvenPanel {
	/* Global Variables */
	private static final long serialVersionUID = 2L;
	private static double scrX;
	private static double scrY;
	private static double scrYP;
	/* Instance Variables */
	private volatile boolean timerPaint = false;
	private volatile boolean mousePress = false;
	private volatile double mouseX = 0.0;
	private volatile double mouseY = 0.0;
	/* Operation Variables */
	ArrayList<RenderGroup> groups = new ArrayList<RenderGroup>();
	private RenderGroup selected = null;
	private boolean outer = false;
	private RenderGroup holding = null;
	private boolean carry = false;
	private int xDir;
	private int yDir;
	private int xInit;
	private int yInit;
	private double xOffset;
	private double yOffset;
	private ArrayList<Circle> graveyard = new ArrayList<Circle>();
	private ArrayList<Color> gravecolors = new ArrayList<Color>();
	private ArrayList<Double> gravetimes = new ArrayList<Double>();
	private static double decayBase = 0.02;
	private static double decayRate = 0.0001;

	/* Main Constructor */
	public MainPanel(double scrX, double scrY) {
		super();
		MainPanel.scrX = scrX;
		MainPanel.scrY = scrY;
		MainPanel.scrYP = toProportion(scrY);
		setBackground(Color.BLACK);
		for (int i = 1; i <= 5; i++) {
			groups.add(new RenderGroup(i, i, i / 6D, toProportion(scrY / 2)));
		}
		Collections.sort(groups);
	}

	/* Action Listeners */
	@Override
	public void actionPerformed(ActionEvent ae) {
		timerPaint = true;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent m) {
		if (m.getButton() == MouseEvent.BUTTON1)
			mousePress = true;
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		switch (m.getButton()) {
		case MouseEvent.BUTTON1:
			mousePress = false;
			break;
		case MouseEvent.BUTTON2:
			if (selected != null)
				selected.rotate();
			break;
		case MouseEvent.BUTTON3:
			if (holding == null) {
				groups.add(new RenderGroup(1, 1, toProportion(mouseX),
						toProportion(mouseY)));
			}
			break;
		}
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		mouseX = m.getX();
		mouseY = m.getY();
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent m) {
		mouseX = m.getX();
		mouseY = m.getY();
		repaint();
	}

	/* Geometric Functions */
	public static double toProportion(double px) {
		return px / scrX;
	}

	public static double toPixels(double scr) {
		return scr * scrX;
	}

	/* RenderGroup Functions */
	public void kill(Circle c, Color color) {
		graveyard.add(c);
		gravecolors.add(color);
		gravetimes.add(1.0);
	}

	public void kill(RenderGroup rg) {
		groups.remove(rg);
		ArrayList<Circle> holder = rg.kill();
		graveyard.addAll(holder);
		for (int i = 0; i < holder.size(); i++) {
			gravecolors.add(rg.getColor());
			gravetimes.add(1.0);
		}
	}

	@Override
	public synchronized void paintComponent(Graphics g) {
		double mx = toProportion(mouseX);
		double my = toProportion(mouseY);
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) scrX, (int) scrY);
		if (mousePress) {
			if (holding == null && selected != null) {
				holding = selected;
				groups.remove(holding);
				groups.add(holding);
				carry = !outer;
				if (!carry) {
					xDir = (int) ((mx - holding.getX()) / Math.abs(holding
							.getX() - mx));
					yDir = (int) ((my - holding.getY()) / Math.abs(holding
							.getY() - my));
					xInit = holding.getCluster();
					yInit = holding.getSize();
					xOffset = mx;
					yOffset = my;
				}
				selected = null;
			}
		} else {
			if (holding != null) {
				if (selected != null) {
					if (outer) {

					} else {
						selected.merge(holding, this);
					}
				}
				holding = null;
				selected = null;
				carry = false;
				Collections.sort(groups);
			}
		}
		// Get selection.
		if (holding == null || carry) {
			selected = null;
			for (int i = groups.size() - 1; i >= 0; i--) {
				RenderGroup rg = groups.get(i);
				if (rg.inOuter(mx, my) && rg.isSelectable() && rg != holding) {
					selected = rg;
					outer = !rg.inInner(mx, my);
					break;
				}
			}
		}
		// Render Graveyard.
		for (int i = graveyard.size() - 1; i >= 0; i--) {
			gravetimes.set(i, gravetimes.get(i) - decayBase - decayRate
					* graveyard.size());
			double life = gravetimes.get(i);
			if (life <= 0) {
				graveyard.remove(i);
				gravecolors.remove(i);
				gravetimes.remove(i);
			} else {
				Circle c = graveyard.get(i);
				g.setColor(Color.WHITE);
				g.fillOval((int) toPixels(c.getX() - RADIUS * life) - 2,
						(int) toPixels(c.getY() - RADIUS * life) - 2,
						(int) toPixels(2 * RADIUS * life) + 4, (int) toPixels(2
								* RADIUS * life) + 4);
				g.setColor(gravecolors.get(i));
				g.fillOval((int) toPixels(c.getX() - RADIUS * life),
						(int) toPixels(c.getY() - RADIUS * life),
						(int) toPixels(2 * RADIUS * life), (int) toPixels(2
								* RADIUS * life));
			}
		}
		// Render RenderGroups.
		for (RenderGroup rg : groups) {
			if (rg == holding && carry) {
				rg.move(mx, my);
				rg.draw(g, mx, my, timerPaint, false, false, true);
			} else if (rg == holding && !carry) {
				int xProg = (int) ((mx - xOffset) * 2 * xDir / RADIUS);
				int yProg = (int) ((my - yOffset) * yDir / RADIUS);
				if (rg.getCluster() != xInit + xProg)
					rg.incCluster(xInit + xProg - rg.getCluster(), this);
				if (rg.getSize() != yInit + yProg)
					rg.incSize(yInit + yProg - rg.getSize(), this);
				rg.draw(g, rg.getX(), rg.getY(), timerPaint, true, false, false);
			} else if (rg == selected)
				rg.draw(g, rg.getX(), rg.getY(), timerPaint, outer, !outer,
						false);
			else
				rg.draw(g, timerPaint);
			if (rg.getX() < 0D || rg.getX() > 1D || rg.getY() < 0D
					|| rg.getY() > scrYP)
				kill(rg);
		}
		timerPaint = false;
		// Render mouse pointer last.
		g.setColor(mousePress ? Color.GREEN : Color.BLUE);
		g.fillOval((int) toPixels(toProportion(mouseX) - 0.0030),
				(int) toPixels(toProportion(mouseY) - 0.0030),
				(int) toPixels(0.006), (int) toPixels(0.006));
		g.setColor(mousePress ? Color.BLUE : Color.GREEN);
		g.fillOval((int) toPixels(toProportion(mouseX) - 0.0025),
				(int) toPixels(toProportion(mouseY) - 0.0025),
				(int) toPixels(0.005), (int) toPixels(0.005));
	}
}
