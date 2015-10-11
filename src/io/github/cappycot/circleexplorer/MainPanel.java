package io.github.cappycot.circleexplorer;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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
	/* Instance Variables */
	private volatile boolean timerPaint = false;
	private volatile boolean mousePress = false;
	private volatile boolean mouseNew = false;
	private volatile boolean mouseSplit = false;
	private volatile double mouseX = 0.0;
	private volatile double mouseY = 0.0;
	/* Operation Variables */
	ArrayList<RenderGroup> groups = new ArrayList<RenderGroup>();

	/* Main Constructor */
	public MainPanel(double scrX, double scrY) {
		super();
		MainPanel.scrX = scrX;
		MainPanel.scrY = scrY;
		setBackground(Color.BLACK);
		groups.add(new RenderGroup(2, 2, 0, 0));
	}

	private int rott = 100;
	private boolean rot = false;

	@Override
	public void actionPerformed(ActionEvent ae) {
		rott--;
		if (rott <= 0) {
			rott = 20;
			rot = true;
		}
		timerPaint = true;
		repaint();
	}

	@Override
	public void mousePressed(MouseEvent m) {
		switch (m.getButton()) {
		case MouseEvent.BUTTON1:
			mousePress = true;
			break;
		case MouseEvent.BUTTON2:
			mouseSplit = true;
			break;
		case MouseEvent.BUTTON3:
			mouseNew = true;
			break;
		}
		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent m) {
		mousePress = false;
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

	public static double toProportion(double px) {
		return px / scrX;
	}

	public static double toPixels(double scr) {
		return scr * scrX;
	}

	@Override
	public synchronized void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) scrX, (int) scrY);
		for (RenderGroup rg : groups) {
			rg.move(toProportion(mouseX), toProportion(mouseY));
			if (rot)
				rg.rotate();
			rg.draw(g, timerPaint);
		}
		timerPaint = false;
		rot = false;
		g.setColor(mousePress ? Color.BLUE : Color.GREEN);
		g.fillOval((int) toPixels(toProportion(mouseX) - 0.005),
				(int) toPixels(toProportion(mouseY) - 0.005),
				(int) toPixels(0.01), (int) toPixels(0.01));
	}
}
