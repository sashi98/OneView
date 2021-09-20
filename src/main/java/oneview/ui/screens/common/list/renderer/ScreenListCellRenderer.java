package oneview.ui.screens.common.list.renderer;

import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.TITLE_HEADER_3_FONT;

public class ScreenListCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        CJPanel panel = new CJPanel(true, Color.RED, Color.RED);
        CJLabel label = new CJLabel(String.valueOf(value));
        label.setFont(TITLE_HEADER_3_FONT);
        panel.setBorder(new LineBorder(Color.gray));
        if (isSelected){
            panel.setBorder(new LineBorder(Color.black));
            panel.setColor1(Color.WHITE);
            panel.setColor2(Color.RED);
            label.setForeground(Color.BLACK);
        }
        panel.add(label);
        return panel;
    }
}
