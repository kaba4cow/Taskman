package kaba4cow.taskman.ui.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import kaba4cow.taskman.utils.ApplicationUtils;

public class AboutDialog extends JDialog {

	private static final long serialVersionUID = 1L;

	public AboutDialog() {
		super();
		setTitle("About");
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = GridBagConstraints.RELATIVE;
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.weightx = 0d;
		gbc.weighty = 0d;
		gbc.ipady = 4;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;

		String[] aboutLines = ApplicationUtils.readLines("about");

		JLabel titleLabel = new JLabel();
		titleLabel.setText(aboutLines[0]);
		add(titleLabel, gbc);

		JLabel iconLabel = new JLabel();
		Image iconImage = ApplicationUtils.getApplicationIconImage();
		iconLabel.setIcon(new ImageIcon(iconImage.getScaledInstance(64, 64, Image.SCALE_FAST)));
		add(iconLabel, gbc);

		JScrollPane licensePanel = new JScrollPane();
		licensePanel.setBorder(
				BorderFactory.createTitledBorder(null, aboutLines[1], TitledBorder.CENTER, TitledBorder.TOP));
		licensePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		licensePanel.setPreferredSize(new Dimension(300, 200));
		{
			JTextPane licenseTextPane = new JTextPane();
			licenseTextPane.setEditable(false);
			licenseTextPane.setBackground(null);
			StyledDocument document = licenseTextPane.getStyledDocument();
			SimpleAttributeSet center = new SimpleAttributeSet();
			StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
			document.setParagraphAttributes(0, document.getLength(), center, false);
			StringBuilder licenseText = new StringBuilder();
			for (int i = 2; i < aboutLines.length; i++)
				licenseText.append(aboutLines[i]);
			licenseTextPane.setText(licenseText.toString());
			licenseTextPane.setCaretPosition(0);
			licensePanel.setViewportView(licenseTextPane);
		}
		add(licensePanel, gbc);

		JButton closeButton = new JButton("Close");
		closeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				dispose();
			}
		});
		add(closeButton, gbc);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

}
