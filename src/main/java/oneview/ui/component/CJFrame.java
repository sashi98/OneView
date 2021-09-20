package oneview.ui.component;

import javax.swing.*;

public class CJFrame extends JFrame {
    public CJFrame(CJPanel uiPanel) {
        this.add(uiPanel);
        this.setResizable(false);
    }
}
