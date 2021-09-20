package oneview.ui.screens.deploy.table.renderer;

import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDummyImagePanelObj;

public class ViewPipelineCellRenderer extends CJPanel implements TableCellRenderer {
    private JComponent component;

    public ViewPipelineCellRenderer() {
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
        if (VIEW_PIPELINE_HOVERED_STATE== valueOfValue) {
            component = VIEW_PIPELINE_STATE_HOVERED_IP;
        }else if (VIEW_PIPELINE_STATE == valueOfValue) {
            component = VIEW_PIPELINE_STATE_IP;
        }  else {
            component = newDummyImagePanelObj();
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
