package hvqzao.javaprefs.io;

import javax.swing.SwingUtilities;

import hvqzao.ui.MainWindow;

public class Application implements Runnable {

    private static Application instance = null;

    public static void main(String[] args) {
        Application.getInstance();
    }

    public static synchronized Application getInstance() {
        if (instance == null) {
            instance = new Application();
        }
        return instance;
    }

    public Application() {
        initialize();
    }

    private void initialize() {
        SwingUtilities.invokeLater(this);
    }

    public void run() {
        MainWindow.getInstance();
    }

}
