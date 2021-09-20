package oneview.ui.screens.jobtrigger.table.renderer;

import oneview.ui.icon.ImagePanel;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.IconConstants.*;
import static oneview.ui.constants.GuiConstants.*;

public class StatusCellRenderer extends DefaultTableCellRenderer {
    ImagePanel component;
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

        String valueOfValue = String.valueOf(value);
        if (BUSY_SPIN == valueOfValue) {
            component = BUSY_SPIN_IP;
        }
        if (OK == valueOfValue){
            component = OK_IP;
        }
        if (NOT_OK == valueOfValue){
            component = NOT_OK_IP;
        }
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
        } else {
            component.setBackground(Color.WHITE);
        }
        return component;
    }
}