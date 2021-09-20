package oneview.ui.screens.common.table.renderer;

import javax.swing.*;
import java.awt.*;

import static oneview.ui.constants.IconConstants.ASC_ICON;
import static oneview.ui.constants.IconConstants.DESC_ICON;

public class SortingColumnHeaderRenderer extends ColumnHeaderRenderer {

    private  boolean desc;

    public SortingColumnHeaderRenderer(boolean desc){
        super();
        this.desc = desc;
    }
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());
            if (isDesc()){
                setIcon(DESC_ICON);
                setToolTipText("Latest<->Earliest");
            } else {
                setIcon(ASC_ICON);
                setToolTipText("Earliest<->Latest");
            }
            setHorizontalTextPosition(JLabel.LEFT);
            return this;

    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }
}
