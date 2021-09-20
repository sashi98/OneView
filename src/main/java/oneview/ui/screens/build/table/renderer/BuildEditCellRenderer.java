package oneview.ui.screens.build.table.renderer;

import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;

public class BuildEditCellRenderer extends CJPanel implements TableCellRenderer {
    private JComponent component;

    public BuildEditCellRenderer() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER,3,3));
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
        if (BUILD_EDIT == valueOfValue) {
            component = BUILD_EDIT_IP;
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
