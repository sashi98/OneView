package oneview.ui.date;

import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import java.awt.*;

import static oneview.util.GuiUtil.*;

public class DatePanelHeader extends CJPanel {
    private JLabel hhTextLabel;
    private JLabel mmTextLabel;
    private JLabel ssTextLabel;

    public DatePanelHeader(int width, int height) {
        super(width,height);
        hhTextLabel = new CJLabel("HH");
        mmTextLabel = new CJLabel("MM");
        ssTextLabel = new CJLabel("SS");
        LayoutManager lm = new FlowLayout(FlowLayout.LEFT);
        this.setLayout(lm);
        this.add(newDummyPanelObj(208,1));
        this.add(hhTextLabel);
        this.add(newDummyPanelObj(5, 1));
        this.add(mmTextLabel);
        this.add(newDummyPanelObj(5, 1));
        this.add(ssTextLabel);
    }

}
