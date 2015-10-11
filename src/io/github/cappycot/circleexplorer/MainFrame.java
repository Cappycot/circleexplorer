package io.github.cappycot.circleexplorer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * This is merely a container for the MainPanel. It also does fullscreen. :P
 * 
 * @author Chris Wang
 */
public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		double xScr = d.getWidth();
		double yScr = d.getHeight();
		setBounds(0, 0, (int) xScr, (int) yScr);
		setUndecorated(true);
		// setOpacity(0.01F);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		// contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		MainPanel mainPanel = new MainPanel(xScr, yScr);
		contentPane.add(mainPanel, BorderLayout.CENTER);
	}

	public static void main(String[] args) {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}
}
