package oneview.util;

import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;
import oneview.ui.icon.ImagePanel;

import javax.swing.border.LineBorder;
import java.awt.*;

public class GuiUtil {
    public static final int percent(int num, int p) {
        return (int) Math.ceil((num * p) / 100);
    }

    public static Dimension newDimentionObj(int w, int h) {
        return new Dimension(w, h);
    }

    public static CJPanel newDummyPanelObj() {
        CJPanel dummy = new CJPanel();
        //dummy.setLayout(new GridLayout(1,1,0,0));
        return dummy;
    }

    public static CJPanel newDummyPanelObj(boolean border) {
        CJPanel panel = newDummyPanelObj();
        panel.setBorder(new LineBorder(Color.BLACK));
        return panel;
    }

    public static CJPanel newDummyPanelObj(int w, int h) {
        CJPanel dummy = new CJPanel(w, h);
        dummy.setLayout(new GridLayout(1,1,0,0));
        return dummy;
    }

    public static Component newDummyPanelObj(Color c, String txt) {
        CJPanel dummy = new CJPanel(new GridLayout(1,1));
        CJLabel dummyLabel = new CJLabel(txt);
        dummyLabel.setBackground(c);
        dummy.add(dummyLabel);
        return dummy;
    }

    public static CJPanel newDummyPanelObj(LayoutManager lm) {
        CJPanel panel = new CJPanel();
        panel.setLayout(lm);
        return panel;
    }

    public static CJPanel newDummyPanelObj(int w, int h, boolean border) {
        CJPanel dummy = new CJPanel(w, h);
        dummy.setBorder(new LineBorder(Color.LIGHT_GRAY));
        return dummy;
    }

    public static CJPanel newDummyPanelObj(Color color) {
        CJPanel dummy = new CJPanel();
        dummy.setLayout(null);
        dummy.setBackground(color);
        return dummy;
    }
    public static GridBagConstraints newGBC(int fill, int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = fill;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        return gbc;
    }
    public static GridBagConstraints newGBC(GridBagConstraints gbc, int fill, int gridx, int gridy, double weightx, double weighty, int gridwidth, int gridheight) {
        gbc.fill = fill;
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.weightx = weightx;
        gbc.weighty = weighty;
        gbc.gridwidth = gridwidth;
        gbc.gridheight = gridheight;
        return gbc;
    }

    public static ImagePanel newDummyImagePanelObj() {
        ImagePanel ip = new ImagePanel();
        return ip;
    }
}
