package hvqzao.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public class Default {

	public static final Color BACKGROUND_COLOR = new Color(252, 252, 252);
	public static final Color HIGHLIGHT_BACKGROUND_COLOR = new Color(255, 206, 130);
	public static final Color PROGRESSBAR_COLOR = new Color(235, 135, 1);
	public static final ColorUIResource BACKGROUND_COLOR_RES = new ColorUIResource(BACKGROUND_COLOR);
	public static final Dimension ICON_DIMENSION = new Dimension(34, 26);
	public static final int FONT_SIZE = 11;
	public static final Font monotypeFont = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
	private static DefaultListCellRenderer defaultComboBoxCellRenderer;
	private static JTextField stringCellEditor;
	private static JCheckBox booleanCellEditor;

	public Default() {
		interfaceCustomization();
	}

	private void interfaceCustomization() {
		for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
			if ("Nimbus".equals(info.getName())) {
				try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
				}
				break;
			}
		}
		UIDefaults uidef = UIManager.getLookAndFeelDefaults();
		FontUIResource fui = null;
		for (Entry<Object, Object> e : uidef.entrySet()) {
			Object val = e.getValue();
			if (val != null && val instanceof FontUIResource) {
				fui = (FontUIResource) val;
				uidef.put(e.getKey(), new FontUIResource(fui.getName(), fui.getStyle(), Default.FONT_SIZE));
			}
		}
		if (fui == null) {
			fui = new FontUIResource(new Font("SansSerif", Font.PLAIN, Default.FONT_SIZE));
		}
		UIManager.getLookAndFeelDefaults().put("CheckBox.font", new Font(fui.getName(), fui.getStyle(), Default.FONT_SIZE));
		UIManager.getLookAndFeelDefaults().put("RadioButton.font", new Font(fui.getName(), fui.getStyle(), Default.FONT_SIZE));
		uidef.put("TabbedPane.backgroundPainter", new BackgroundPainter(Default.BACKGROUND_COLOR));
		uidef.put("OptionPane.background", BACKGROUND_COLOR_RES);
		uidef.put("Panel.background", BACKGROUND_COLOR_RES);
		uidef.put("nimbusOrange", Default.PROGRESSBAR_COLOR);
		defaultComboBoxCellRenderer = new ComboBoxCellRenderer();
		stringCellEditor = new JTextField();
		stringCellEditor.putClientProperty("JComponent.sizeVariant", "small");
		stringCellEditor.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		booleanCellEditor = new JCheckBox();
		booleanCellEditor.putClientProperty("JComponent.sizeVariant", "small");
		booleanCellEditor.setHorizontalAlignment(JLabel.CENTER);
		booleanCellEditor.setBorderPainted(true);
	}

	public static void customizeUiComponent(JComponent component) {
		if (component instanceof JPanel) {
			JPanel control = (JPanel) component;
			control.setBackground(Default.BACKGROUND_COLOR);
		}
		if ((component instanceof JCheckBox) || (component instanceof JRadioButton)) {
			component.putClientProperty("JComponent.sizeVariant", "small");
			SwingUtilities.updateComponentTreeUI(component);
		}
		if (component instanceof JComboBox<?>) {
			JComboBox<?> control = (JComboBox<?>) component;
			control.setRenderer(defaultComboBoxCellRenderer);

		}
		if (component instanceof JList<?>) {
			JList<?> control = (JList<?>) component;
			control.setFixedCellHeight(16);
			control.setSelectionBackground(Default.HIGHLIGHT_BACKGROUND_COLOR);
		}
		if (component instanceof JSplitPane) {
			JSplitPane control = (JSplitPane) component;
			control.setDividerSize(5);
			control.setContinuousLayout(true);
		} else if (component instanceof JTextField) {
			JTextField control = (JTextField) component;
			control.setSelectionColor(Default.HIGHLIGHT_BACKGROUND_COLOR);
			control.setSelectedTextColor(Color.black);
		} else if (component instanceof JTextArea) {
			JTextArea control = (JTextArea) component;
			control.setSelectionColor(Default.HIGHLIGHT_BACKGROUND_COLOR);
			control.setSelectedTextColor(Color.black);
		} else if (component instanceof JEditorPane) {
			JEditorPane control = (JEditorPane) component;
			control.setSelectionColor(Default.HIGHLIGHT_BACKGROUND_COLOR);
			control.setSelectedTextColor(Color.black);
		} else if (component instanceof JScrollPane) {
			JScrollPane control = (JScrollPane) component;
			control.getViewport().setBackground(Default.BACKGROUND_COLOR);
		} else {
			component.setFocusable(false);
		}
	}

	private class BackgroundPainter implements Painter<JComponent> {

		private Color color = null;

		public BackgroundPainter(Color c) {
			color = c;
		}

		public void paint(Graphics2D g, JComponent object, int width, int height) {
			if (color != null) {
				g.setColor(color);
				g.fillRect(0, 0, width - 1, height - 1);
			}
		}

	}

	// combobox cell renderer

	private class ComboBoxCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = -2110318394243572252L;

		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			setPreferredSize(new Dimension((int)getSize().getWidth(),20));
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (isSelected) {
				setBackground(Default.HIGHLIGHT_BACKGROUND_COLOR);
			}
			return this;
		}

	}
}
