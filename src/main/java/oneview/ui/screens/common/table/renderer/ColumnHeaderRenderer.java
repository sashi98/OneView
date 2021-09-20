package oneview.ui.screens.common.table.renderer;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ColumnHeaderRenderer extends JLabel implements TableCellRenderer {

    public ColumnHeaderRenderer() {
        setFont(new Font("Arial", Font.BOLD, 13));
        setOpaque(true);
        setForeground(Color.BLACK);
        setBackground(Color.ORANGE);
        setBorder(BorderFactory.createEtchedBorder());
        //setBorder(new LineBorder(Color.BLACK));
        setHorizontalAlignment(JLabel.CENTER);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
       // Dimension d = table.getTableHeader().getPreferredSize();
        setText(value.toString());
        setHorizontalTextPosition(SwingConstants.LEFT);
        return this;
    }
}
