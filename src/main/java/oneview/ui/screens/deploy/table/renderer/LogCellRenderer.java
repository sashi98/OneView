package oneview.ui.screens.deploy.table.renderer;

import oneview.ui.component.CJPanel;
import oneview.ui.screens.jobtrigger.table.ErrorLogInfo;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.FAILURE;
import static oneview.ui.constants.IconConstants.INFO_IP;
import static oneview.ui.constants.IconConstants.RUN_LOG_IP;
import static oneview.util.GuiUtil.newDummyImagePanelObj;

public class LogCellRenderer extends CJPanel implements TableCellRenderer {
    JComponent component;

    public LogCellRenderer() {
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
        if (value != null) {
            component = RUN_LOG_IP;
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
