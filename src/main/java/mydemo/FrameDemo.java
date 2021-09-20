package mydemo;

import oneview.ui.component.CJPanel;

import javax.swing.*;
import java.awt.*;

import static oneview.ui.constants.IconConstants.*;

public class FrameDemo extends JFrame {
    private final static int WIDTH = 700;
    private final static int HEIGHT = 500;

    FrameDemo() {
        super("Deployment Util GUI");
    }

    public static void main(String[] args) {
        FrameDemo mainGUI = new FrameDemo();
        createWindow(mainGUI);
    }

    private static void createWindow(FrameDemo mainGUI) {
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.createUI();
    }

    private void createUI() {
        this.setLayout(new GridLayout(1, 1));
        CJPanel panel = new CJPanel(new GridLayout(1, 5));
        for (int i = 0; i < 5; i++) {
        }
        panel.add(new CJPanel(BUILD_FAILED_LOG_IP));
        panel.add(new CJPanel(BUILD_FAILED_IP));
        panel.add(new CJPanel(BUILD_FAILED_LOG_IP));
        panel.add(new CJPanel(BUILD_FAILED_LOG_IP));

        this.add(panel);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setVisible(true);
    }
}
