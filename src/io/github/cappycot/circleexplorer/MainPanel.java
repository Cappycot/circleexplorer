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
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
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

	private RenderGroup test = new RenderGroup(10, 10, 0.5, 0.5);

	@Override
	public synchronized void paintComponent(Graphics g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, (int) scrX, (int) scrY);
		test.draw(g, toProportion(mouseX), toProportion(mouseY), timerPaint);
		timerPaint = false;
		g.setColor(mousePress ? Color.BLUE : Color.GREEN);
		g.fillOval((int) toPixels(toProportion(mouseX) - 0.005),
				(int) toPixels(toProportion(mouseY) - 0.005),
				(int) toPixels(0.01), (int) toPixels(0.01));
	}
}
