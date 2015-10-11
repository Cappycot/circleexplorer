package io.github.cappycot.circleexplorer;

import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Code just to get rid of a bunch of unwanted code from MainPanel.
 * 
 * @author Chris Wang
 */
public abstract class DontEvenPanel extends JPanel implements MouseListener,
		MouseMotionListener, ActionListener {
	// WOW! Much UID! Very serial! Go away.
	private static final long serialVersionUID = 404;

	public DontEvenPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		Timer t = new Timer(100, this);
		t.start();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}
