package oneview.ui.screens.server.table.renderer;

import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;

public class StartStopCellRenderer extends CJPanel implements TableCellRenderer {
    private JComponent component;

    public StartStopCellRenderer() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String valueOfValue = String.valueOf(value);

        if (component != null) {
            this.remove(component);
            this.revalidate();
            this.validate();
        }
        if (STARTED == valueOfValue) {
            component = STARTED_IP;
        }
        if (STOPPED == valueOfValue) {
            component = STOPPED_IP;
        }
        if (BUSY_SPIN == valueOfValue) {
            component = BUSY_SPIN_IP;
        }
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
        } else {
            component.setBackground(Color.WHITE);
        }
        this.add(component);
        return this;
    }

    public JComponent getComponent() {
        return component;
    }
}
