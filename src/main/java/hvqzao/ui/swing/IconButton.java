package hvqzao.ui.swing;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconButton extends JButton {

    public IconButton(ImageIcon icon) {
        super(icon);
        initialize();
    }

    private void initialize() {
        Default.customizeUiComponent(this);
        setPreferredSize(Default.ICON_DIMENSION);
        setMaximumSize(Default.ICON_DIMENSION);
    }

}
