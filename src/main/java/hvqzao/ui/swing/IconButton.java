package hvqzao.ui.swing;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {
	
	private static final long serialVersionUID = -4126134059223828507L;

	public IconButton(ImageIcon icon) {
		super(icon);
		Default.customizeUiComponent(this);
		setPreferredSize(Default.ICON_DIMENSION);
		setMaximumSize(Default.ICON_DIMENSION);
	}
	
}
