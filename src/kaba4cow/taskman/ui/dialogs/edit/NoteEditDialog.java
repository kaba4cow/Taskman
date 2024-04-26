package kaba4cow.taskman.ui.dialogs.edit;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import kaba4cow.taskman.Application;
import kaba4cow.taskman.repositories.note.Note;
import kaba4cow.taskman.utils.UiUtils;

public class NoteEditDialog extends JDialog implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;

	private boolean option = false;

	private final JTextPane descriptionTextPane;
	private final JTextField tagTextField;
	private final JComboBox<String> tagComboBox;

	private final JButton saveButton;
	private final JButton cancelButton;

	public NoteEditDialog(Note note, boolean createNote) {
		super();
		if (createNote)
			setTitle("Add New Note");
		else
			setTitle("Edit Note - " + note.getDescription());
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = UiUtils.createGridBagConstraints();

		JPanel descriptionPanel = UiUtils.createTitledPanel("Description", 1, 1);
		{
			descriptionTextPane = new JTextPane();
			descriptionTextPane.setText(note.getDescription());
			descriptionTextPane.setBorder(BorderFactory.createLoweredBevelBorder());
			descriptionTextPane.setPreferredSize(new Dimension(300, 100));
			descriptionPanel.add(descriptionTextPane);
		}
		add(descriptionPanel, gbc);

		JPanel tagPanel = UiUtils.createTitledPanel("Tag", 3, 1);
		{
			tagTextField = new JTextField(note.getTag());
			tagPanel.add(tagTextField);

			tagPanel.add(new JLabel("All Tags:"));
			tagComboBox = new JComboBox<>();
			tagComboBox.removeAllItems();
			Set<String> tagSet = Application.getNoteRepository().getAllTags();
			for (String tag : tagSet)
				tagComboBox.addItem(tag);
			tagComboBox.addItemListener(this);
			tagComboBox.addActionListener(this);
			tagPanel.add(tagComboBox);
		}
		add(tagPanel, gbc);

		JPanel buttonsPanel = UiUtils.createTitledPanel("", 1, 2, 4);
		{
			saveButton = new JButton("Save");
			saveButton.addActionListener(this);
			buttonsPanel.add(saveButton);
			cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(this);
			buttonsPanel.add(cancelButton);
		}
		add(buttonsPanel, gbc);

		pack();
		setResizable(false);
		setLocationRelativeTo(null);
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);

		if (option) {
			note.setDescription(descriptionTextPane.getText());
			note.setTag(tagTextField.getText());
			Application.getNoteRepository().updateElement(note);
		} else if (createNote)
			Application.getNoteRepository().deleteElement(note);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == saveButton) {
			option = true;
			dispose();
		} else if (event.getSource() == cancelButton) {
			option = false;
			dispose();
		} else if (event.getSource() == tagComboBox)
			tagTextField.setText((String) tagComboBox.getSelectedItem());
	}

	@Override
	public void itemStateChanged(ItemEvent event) {
		if (event.getSource() == tagComboBox)
			tagTextField.setText((String) tagComboBox.getSelectedItem());
	}

}
