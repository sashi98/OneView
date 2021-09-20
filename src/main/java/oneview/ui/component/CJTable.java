package oneview.ui.component;

import oneview.ui.screens.common.context.OneViewContext;
import oneview.ui.constants.GuiConstants;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;

import java.awt.*;

import static oneview.ui.constants.DimensionConstants.TABLE_ROW_HEIGHT;
import static oneview.util.ThreadUtil.sleep;

public abstract class CJTable extends JTable {
    public CJTable(AbstractTableModel tm){
        super(tm);
        this.setRowHeight(TABLE_ROW_HEIGHT);
        this.setBorder(GuiConstants.LINE_GRAY_1THK_BORDER);
        this.setBackground(Color.getColor("#ADD8E6"));

    }

    public abstract void updateTable(OneViewContext ctx);

    public void waitFor(long timeout) {
        while (timeout >= 0) {
            sleep(100);
            timeout -= 100;
            repaint();
        }
    }

    public void forkAndWaitFor(final long timeout) {
        new Thread(() -> {
            long tout = timeout;
            while (tout >= 0) {
                sleep(100);
                tout -= 100;
                repaint();
            }
        }).start();

    }

    public abstract CJTable getInvoker();
}
