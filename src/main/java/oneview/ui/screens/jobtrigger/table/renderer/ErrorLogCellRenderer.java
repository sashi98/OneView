package oneview.ui.screens.jobtrigger.table.renderer;

import oneview.ui.component.CJPanel;
import oneview.ui.screens.jobtrigger.table.ErrorLogInfo;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDummyImagePanelObj;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class ErrorLogCellRenderer extends CJPanel implements TableCellRenderer {
    JComponent component;

    public ErrorLogCellRenderer() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        this.setBackground(Color.WHITE);
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (component != null) {
            this.remove(component);
            this.revalidate();
            this.validate();
        }
        if (value != null && (FAILURE == ((ErrorLogInfo)value).getInfo())) {
            component = INFO_IP;
        } else {
            component = newDummyImagePanelObj();
        }
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
            this.setBackground(Color.LIGHT_GRAY);
        } else {
            component.setBackground(Color.WHITE);
            this.setBackground(Color.WHITE);
        }
        this.add(component);
        return this;
    }

    public JComponent getComponent() {
        return component;
    }
}