package oneview.ui.screens.deploy.table.renderer;

import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;

public class ReinstallCellRenderer extends CJPanel implements TableCellRenderer {
    private JComponent component;

    public ReinstallCellRenderer() {
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
        if (FEATURE_REINSTALL == valueOfValue) {
            component = FEATURE_REINSTALL_IP;
        }
        if (FEATURE_REINSTALLING == valueOfValue) {
            component = FEATURE_REINSTALLING_IP;
        }
        if (FEATURE_REINSTALLED == valueOfValue) {
            component = FEATURE_REINSTALLED_IP;
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
