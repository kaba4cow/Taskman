package kaba4cow.taskman.utils;

import java.awt.GridBagConstraints;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public final class UiUtils {

	private UiUtils() {
	}

	public static JPanel createTitledPanel(String title, int rows, int cols) {
		return createTitledPanel(title, rows, cols, 2);
	}

	public static JPanel createTitledPanel(String title, int rows, int cols, int gap) {
		JPanel panel = new JPanel();
		panel.setBorder(BorderFactory.createTitledBorder(title));
		panel.setLayout(new GridLayout(rows, cols, gap, gap));
		return panel;
	}

	public static GridBagConstraints createGridBagConstraints() {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = 0;
		constraints.gridy = GridBagConstraints.RELATIVE;
		constraints.gridwidth = 1;
		constraints.gridheight = 1;
		constraints.weightx = 0d;
		constraints.weighty = 1d;
		constraints.anchor = GridBagConstraints.NORTH;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		return constraints;
	}

}
