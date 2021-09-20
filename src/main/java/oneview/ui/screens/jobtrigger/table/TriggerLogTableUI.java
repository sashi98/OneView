package oneview.ui.screens.jobtrigger.table;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJButton;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.ui.constants.DimensionConstants.ICON_HEIGHT;
import static oneview.ui.constants.DimensionConstants.ICON_WIDTH;

public class TriggerLogTableUI extends CJPanel implements RegisterListener {
    private CJButton trashButton;
    private CJButton triggerButton;
    private TriggerLogTable triggerLogTable;
    public TriggerLogTableUI(int width, int height) {
        super(width,height);
        triggerLogTable = new TriggerLogTable(new TriggerLogTableDataModel(new ArrayList<>()));
        trashButton = new CJButton(TRASH_DEFAULT_ICON, TRASH_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Clear All Log");
        triggerButton = new CJButton(TRIGGERED_DEFAULT_ICON, TRIGGERED_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Trigger Job");

        JScrollPane tableScrollPane = new JScrollPane(triggerLogTable);
        tableScrollPane.setLayout(new ScrollPaneLayout());
        this.setLayout(new BorderLayout(0,0));
        CJPanel panel = new CJPanel(width, ICON_HEIGHT);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0,0));
        panel.add(triggerButton);
        panel.add(trashButton);
        this.add(panel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Trigger Log", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));
        registerListener();
    }

    public TriggerLogTable getTriggerLogTable() {
        return triggerLogTable;
    }

    public CJButton getTriggerButton() {
        return triggerButton;
    }

    public CJButton getTrashButton() {
        return trashButton;
    }


    @Override
    public void registerListener() {
        trashButton.addActionListener(actionEvent -> {
            getTriggerLogTable().clearTable();
        });
    }
}
