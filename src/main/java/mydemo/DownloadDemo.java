package mydemo;

import oneview.ui.component.CJLocation;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import java.awt.*;

import static oneview.ui.constants.IconConstants.*;

public class DownloadDemo extends JFrame {
    private final static int WIDTH = 700;
    private final static int HEIGHT = 500;

    DownloadDemo() {
        super("Deployment Util GUI");
    }

    public static void main(String[] args) {
        DownloadDemo mainGUI = new DownloadDemo();
        createWindow(mainGUI);
    }

    private static void createWindow(DownloadDemo mainGUI) {
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.createUI();
    }

    private void createUI() {
        this.setLayout(new GridLayout(1, 1));
        CJLocation location = new CJLocation(null, 200, true);

        this.add(new CJPanel(location));
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setVisible(true);
    }
}
/*
https://unsplash.com/photos/8Myh76_3M2U/download?force=true
 */