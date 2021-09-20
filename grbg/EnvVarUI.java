package oneview.ui.screens.tables.server;

import oneview.bean.OneViewContext;
import oneview.ui.component.CJButton;
import oneview.ui.component.CJPanel;
import oneview.ui.component.CJTable;

import java.awt.*;

import static oneview.ui.constants.DimensionConstants.ICON_HEIGHT;
import static oneview.ui.constants.DimensionConstants.ICON_WIDTH;
import static oneview.ui.constants.IconConstants.*;

public class EnvVarUI extends CJPanel {
    private CJTable envTable;
    private CJButton addButton;
    private CJButton removeButton;
    private CJButton editButton;
    private EnvVarTableModel tableModel;

    public EnvVarUI(int w, int h) {
        super(w, h);
        tableModel = new EnvVarTableModel();
        envTable = new CJTable(tableModel) {
            @Override
            public void updateTable(OneViewContext ctx) {

            }
        };
        addButton = new CJButton(ADD_DEFAULT_ICON, ADD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Add Variable");
        removeButton = new CJButton(REMOVE_DEFAULT_ICON, REMOVE_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Remove Variable");
        editButton = new CJButton("Edit");
        this.add(envTable, BorderLayout.CENTER);
        {
            CJPanel panel = new CJPanel(50, h);
            panel.add(addButton);
            panel.add(removeButton);
            panel.add(editButton);
            this.add(panel, BorderLayout.WEST);
        }
    }

}
