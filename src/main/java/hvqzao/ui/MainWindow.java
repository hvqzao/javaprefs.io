package hvqzao.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;

import hvqzao.ui.swing.Default;
import hvqzao.ui.swing.IconButton;

public class MainWindow extends JFrame implements ActionListener {

	private static final long serialVersionUID = -7647347180509524890L;
	private JComboBox<String> comboBox;
	private JTextPane textPane;

	public MainWindow() {
		super();
		new Default();
		ImageIcon addIcon = new ImageIcon(new ImageIcon(getClass().getResource("/res/icon_add.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
		ImageIcon removeIcon = new ImageIcon(new ImageIcon(getClass().getResource("/res/icon_remove.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));
		ImageIcon saveIcon = new ImageIcon(new ImageIcon(getClass().getResource("/res/icon_save.png")).getImage().getScaledInstance(13, 13, java.awt.Image.SCALE_SMOOTH));

		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);

		IconButton addButton = new IconButton(addIcon);
		addButton.setToolTipText("Add");
		addButton.setEnabled(false);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, addButton, 1 + 24, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, addButton, -2 -12 - 24 - 12 - 24, SpringLayout.EAST, contentPane);
		contentPane.add(addButton);

		IconButton saveButton = new IconButton(saveIcon);
		saveButton.setToolTipText("Save");
		saveButton.setEnabled(false);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, saveButton, 1 + 24, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, saveButton, -12 - 24, SpringLayout.EAST, contentPane);
		contentPane.add(saveButton);

		IconButton removeButton = new IconButton(removeIcon);
		removeButton.setToolTipText("Remove");
		removeButton.setEnabled(false);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, removeButton, 1 + 24, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, removeButton, 0, SpringLayout.EAST, contentPane);
		contentPane.add(removeButton);

		comboBox = new JComboBox<String>();
		Default.customizeUiComponent(comboBox);
		sl_contentPane.putConstraint(SpringLayout.NORTH, comboBox, -1, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, comboBox, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, comboBox, -4, SpringLayout.WEST, addButton);
		contentPane.add(comboBox);

		JScrollPane scrollPane = new JScrollPane();
		Default.customizeUiComponent(scrollPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrollPane, 5, SpringLayout.SOUTH, comboBox);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, contentPane);
		contentPane.add(scrollPane);

		textPane = new JTextPane();
		Default.customizeUiComponent(textPane);
		textPane.setFont(Default.monotypeFont);
		scrollPane.setViewportView(textPane);

		comboBoxDropdownRefresh();
		comboBox.addActionListener(this);

		setTitle("JavaPrefs.IO");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 600);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	private void comboBoxDropdownRefresh() {
		comboBox.removeAllItems();
		comboBox.addItem("");
		try {
			for (String item : Preferences.userRoot().childrenNames()) {
				comboBox.addItem(item);
			}
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	public synchronized void comboBoxUpdate() {
		textPane.setText("");
		if (comboBox.getSelectedItem().toString().length() > 0) {
			Preferences prefs;
			prefs = Preferences.userRoot().node(comboBox.getSelectedItem().toString());
			try {
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				prefs.exportNode((OutputStream) os);
				textPane.setText(os.toString());
			} catch (IOException | BackingStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		comboBoxUpdate();
	}

}
