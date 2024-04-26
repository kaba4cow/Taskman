package kaba4cow.taskman.ui;

import java.awt.Color;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import kaba4cow.taskman.utils.ApplicationUtils;

public class SplashScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	public SplashScreen() {
		super();
		JLabel iconLabel = new JLabel();
		Image iconImage = ApplicationUtils.getApplicationIconImage();
		iconLabel.setIcon(new ImageIcon(iconImage.getScaledInstance(256, 256, Image.SCALE_FAST)));
		add(iconLabel);
		setResizable(false);
		setUndecorated(true);
		setBackground(new Color(0, 0, 0, 0));
		setSize(256, 256);
		setLocationRelativeTo(null);
		setVisible(true);
	}

}
