package kaba4cow.taskman.ui.dialogs.selection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import kaba4cow.taskman.utils.UiUtils;

public class ColorSelectionDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 1L;

	private static final int[] colorPalette = { //
			0xFF8080, 0xFFFF80, 0x80FF80, 0x00FF80, 0x80FFFF, 0x0080FF, 0xFF80C0, 0xFF80FF, //
			0xFF0000, 0xFFFF00, 0x80FF00, 0x00FF40, 0x00FFFF, 0x0080C0, 0x8080C0, 0xFF00FF, //
			0x804040, 0xFF8040, 0x00FF00, 0x008080, 0x004080, 0x8080FF, 0x800040, 0xFF0080, //
			0x800000, 0xFF8000, 0x008000, 0x008040, 0x0000FF, 0x0000A0, 0x800080, 0x8000FF, //
			0x400000, 0x804000, 0x004000, 0x004040, 0x000080, 0x000040, 0x400040, 0x400080, //
			0x000000, 0x808000, 0x808040, 0x808080, 0x408080, 0xC0C0C0, 0x400040, 0xFFFFFF //
	};

	private static final int paletteRows = 6;
	private static final int paletteCols = 8;

	private final Color initialColor;
	private Color selectedColor;

	private final JLabel previewLabel;
	private final JButton selectButton;
	private final JButton cancelButton;

	public ColorSelectionDialog(String title, Color initialColor) {
		super();
		setTitle("Choose " + title + " Color");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		this.initialColor = initialColor;
		selectedColor = initialColor;

		JPanel previewPanel = UiUtils.createTitledPanel("Preview", 1, 1);
		{
			previewLabel = new JLabel();
			previewLabel.setOpaque(true);
			previewLabel.setBackground(initialColor);
			previewLabel.setBorder(BorderFactory.createLoweredBevelBorder());
			previewLabel.setHorizontalAlignment(JLabel.CENTER);
			previewLabel.setPreferredSize(new Dimension(0, 16));
			previewPanel.add(previewLabel);
		}
		add(previewPanel, gbc);

		JPanel palettePanel = UiUtils.createTitledPanel("Palette", paletteRows, paletteCols, 8);
		for (int i = 0; i < colorPalette.length; i++) {
			JButton button = new JButton();
			button.setBackground(new Color(colorPalette[i]));
			button.setPreferredSize(new Dimension(16, 16));
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					previewLabel.setBackground(button.getBackground());
				}
			});
			palettePanel.add(button);
		}
		add(palettePanel, gbc);

		JPanel buttonPanel = UiUtils.createTitledPanel("", 1, 2);
		{
			selectButton = new JButton("Select");
			selectButton.addActionListener(this);
			buttonPanel.add(selectButton);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);
			buttonPanel.add(cancelButton);
		}
		add(buttonPanel, gbc);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public Color getSelectedColor() {
		return selectedColor;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == selectButton) {
			selectedColor = previewLabel.getBackground();
			dispose();
		} else if (event.getSource() == cancelButton) {
			selectedColor = initialColor;
			dispose();
		}
	}

}
