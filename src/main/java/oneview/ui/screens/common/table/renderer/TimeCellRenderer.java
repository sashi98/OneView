package oneview.ui.screens.common.table.renderer;

import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;
import oneview.util.DatePatterns;
import oneview.util.DateUtil;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Date;

public class TimeCellRenderer extends CJPanel implements TableCellRenderer {
    private JComponent component;

    public TimeCellRenderer() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setBackground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Date then = (Date) value;
        String elapsedTime="not now";
        if (component != null) {
            this.remove(component);
            this.revalidate();
            this.validate();
        }
        if (then != null){
            Date now = DateUtil.getCurrentDateObj(DatePatterns.DP_YYYY_MM_DD_HH_MM_SS);
            elapsedTime = DateUtil.getTimeElapsed(now, then);
        }
        component = new CJLabel(elapsedTime);
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
