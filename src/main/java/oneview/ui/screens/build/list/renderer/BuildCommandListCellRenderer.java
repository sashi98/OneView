package oneview.ui.screens.build.list.renderer;

import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.NORMAL_TXT_FONT;

public class BuildCommandListCellRenderer implements ListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList jList, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        CJPanel panel = new CJPanel(new CJLabel(String.valueOf(value)), new FlowLayout(FlowLayout.LEFT));
        panel.setFont(NORMAL_TXT_FONT);
        if (isSelected){
            panel.setBackground(Color.LIGHT_GRAY);
        }
        return panel;
    }
}
