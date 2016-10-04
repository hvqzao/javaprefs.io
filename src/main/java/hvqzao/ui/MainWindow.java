package hvqzao.ui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.prefs.BackingStoreException;
import java.util.prefs.InvalidPreferencesFormatException;
import java.util.prefs.Preferences;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import hvqzao.ui.swing.Default;
import hvqzao.ui.swing.IconButton;

public class MainWindow extends JFrame {

    private static final long serialVersionUID = -7647347180509524890L;
    private JComboBox<String> comboBox;
    private JTextPane textPane;
    private ActionListener comboBoxActionListener;
    private IconButton addButton;
    private IconButton saveButton;
    private IconButton removeButton;
    private String origPrefs;
    private IconButton refreshButton;
    private Document textPaneDocument;

    private static MainWindow instance;

    public static MainWindow getInstance() {
        if (instance == null) {
            instance = new MainWindow();
        }
        return instance;
    }

    public MainWindow() {
        super();
        initialize();
    }

    private void initialize() {
        Default.getInstance();
        ImageIcon refreshIcon = Default.getResourceIcon("/res/icon_refresh.png");
        ImageIcon addIcon = Default.getResourceIcon("/res/icon_add.png");
        ImageIcon removeIcon = Default.getResourceIcon("/res/icon_remove.png");
        ImageIcon saveIcon = Default.getResourceIcon("/res/icon_save.png");

        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        SpringLayout sl_contentPane = new SpringLayout();
        contentPane.setLayout(sl_contentPane);

        refreshButton = new IconButton(refreshIcon);
        refreshButton.setToolTipText("Refresh");
        sl_contentPane.putConstraint(SpringLayout.SOUTH, refreshButton, 1 + 24, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, refreshButton, -2 - 12 - 24 - 2 - 12 - 24 - 12 - 24,
                SpringLayout.EAST, contentPane);
        contentPane.add(refreshButton);

        addButton = new IconButton(addIcon);
        addButton.setToolTipText("Add...");
        sl_contentPane.putConstraint(SpringLayout.SOUTH, addButton, 1 + 24, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, addButton, -2 - 12 - 24 - 12 - 24, SpringLayout.EAST,
                contentPane);
        contentPane.add(addButton);

        saveButton = new IconButton(saveIcon);
        saveButton.setToolTipText("Save (Ctrl+S)");
        saveButton.setEnabled(false);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, saveButton, 1 + 24, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, saveButton, -12 - 24, SpringLayout.EAST, contentPane);
        contentPane.add(saveButton);

        removeButton = new IconButton(removeIcon);
        removeButton.setToolTipText("Remove...");
        removeButton.setEnabled(false);
        sl_contentPane.putConstraint(SpringLayout.SOUTH, removeButton, 1 + 24, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, removeButton, 0, SpringLayout.EAST, contentPane);
        contentPane.add(removeButton);

        comboBox = new JComboBox<String>();
        Default.customizeUiComponent(comboBox);
        sl_contentPane.putConstraint(SpringLayout.NORTH, comboBox, -1, SpringLayout.NORTH, contentPane);
        sl_contentPane.putConstraint(SpringLayout.WEST, comboBox, 0, SpringLayout.WEST, contentPane);
        sl_contentPane.putConstraint(SpringLayout.EAST, comboBox, -4, SpringLayout.WEST, refreshButton);
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
        textPane.setFont(Default.MONOTYPE_FONT);
        scrollPane.setViewportView(textPane);

        comboBoxDropdownRefresh();
        comboBoxActionListener = new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                origPrefs = null;
                saveButton.setEnabled(false);
                textPane.setText("");
                if (comboBox.getSelectedItem().toString().length() > 0) {
                    removeButton.setEnabled(true);
                    Preferences prefs = Preferences.userRoot().node(comboBox.getSelectedItem().toString());
                    try {
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        prefs.exportNode((OutputStream) os);
                        String s = os.toString();
                        textPane.setText(s);
                        origPrefs = s;
                    } catch (IOException e) {
                        // nothing
                    } catch (BackingStoreException e) {
                        // nothing
                    }
                } else {
                    removeButton.setEnabled(false);
                }
            }
        };
        comboBox.addActionListener(comboBoxActionListener);

        refreshButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String node = (String) comboBox.getSelectedItem();
                comboBoxDropdownRefresh();
                comboBox.setSelectedItem(node);
            }
        });

        addButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                String node = (String) JOptionPane.showInputDialog(MainWindow.this, "", "Name for new node?",
                        JOptionPane.PLAIN_MESSAGE);
                if (node != null && node.length() != 0) {
                    Preferences.userRoot().node(node);
                    comboBoxDropdownRefresh();
                    comboBox.setSelectedItem(node);
                }
            }
        });

        saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                saveAction();
            }
        });

        removeButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent evt) {
                if (JOptionPane.showConfirmDialog(
                        MainWindow.this, "Are you sure you want to recursively remove \""
                        + comboBox.getSelectedItem().toString() + "\" node?",
                        "Confirmation", JOptionPane.YES_NO_OPTION) == 0) {
                    try {
                        Preferences.userRoot().node(comboBox.getSelectedItem().toString()).removeNode();
                    } catch (BackingStoreException e) {
                        // nothing
                    }
                    comboBoxDropdownRefresh();
                }
            }
        });

        // textPane content edited?
        textPaneDocument = textPane.getDocument();
        textPaneDocument.addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent evt) {
                textPaneUpdateHandler();
            }

            @Override
            public void insertUpdate(DocumentEvent evt) {
                textPaneUpdateHandler();
            }

            @Override
            public void changedUpdate(DocumentEvent evt) {
                textPaneUpdateHandler();
            }
        });

        // textPane undo capability
        final UndoManager undo = new UndoManager();
        Document doc = textPane.getDocument();
        doc.addUndoableEditListener(new UndoableEditListener() {
            public void undoableEditHappened(UndoableEditEvent evt) {
                undo.addEdit(evt.getEdit());
            }
        });
        textPane.getActionMap().put("Undo", new AbstractAction("Undo") {

            private static final long serialVersionUID = -529529695064970831L;

            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undo.canUndo()) {
                        undo.undo();
                    }
                } catch (CannotUndoException e) {
                }
            }
        });
        textPane.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // textPane Save (Ctrl+S)
        KeyStroke ctrlS = KeyStroke.getKeyStroke("control S");
        textPane.getInputMap().put(ctrlS, new AbstractAction() {

            private static final long serialVersionUID = -6554064609954416509L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (saveButton.isEnabled()) {
                    saveAction();
                }
            }
        });

        setTitle("JavaPrefs.IO");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(300, 200, 750, 600);
        setMinimumSize(new Dimension(300, 200));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void comboBoxDropdownRefresh() {
        comboBox.removeActionListener(comboBoxActionListener);
        comboBox.removeAllItems();
        comboBox.addActionListener(comboBoxActionListener);
        comboBox.addItem("");
        try {
            for (String item : Preferences.userRoot().childrenNames()) {
                comboBox.addItem(item);
            }
        } catch (BackingStoreException e) {
            // nothing
        }
    }

    private void textPaneUpdateHandler() {
        if (origPrefs != null) {
            boolean changed = textPane.getText().equals(origPrefs);
            if (saveButton.isEnabled() == changed) {
                saveButton.setEnabled(!changed);
            }
        }
    }

    private void saveAction() {
        origPrefs = textPane.getText();
        InputStream is = new ByteArrayInputStream(origPrefs.getBytes(StandardCharsets.UTF_8));
        try {
            Preferences.importPreferences(is);
        } catch (IOException e) {
            // nothing
        } catch (InvalidPreferencesFormatException e) {
            // nothing
        }
        saveButton.setEnabled(false);
        String node = (String) comboBox.getSelectedItem();
        comboBoxDropdownRefresh();
        comboBox.setSelectedItem(node);
    }
}
