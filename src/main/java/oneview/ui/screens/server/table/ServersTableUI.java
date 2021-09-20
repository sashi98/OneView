package oneview.ui.screens.server.table;

import oneview.ui.RegisterListener;
import oneview.ui.component.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class ServersTableUI extends CJPanel implements RegisterListener {
    private ServerTable serverTable;
    private CJButton addServerButton;

    public ServersTableUI(int width, int height) {
        super(width,height);
        serverTable = new ServerTable(new ServerTableDataModel(new ArrayList<>()));
        addServerButton = new CJButton(ADD_DEFAULT_ICON, ADD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Add Server");
        JScrollPane tableScrollPane = new JScrollPane(serverTable);
        tableScrollPane.setLayout(new ScrollPaneLayout());
        this.setLayout(new BorderLayout(0,0));
        CJPanel panel = new CJPanel(width, ICON_HEIGHT);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0,0));
        panel.add(addServerButton);
        //panel.add(trashButton);
        this.add(panel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Servers", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));
        registerListener();
    }

    public ServerTable getServerTable() {
        return serverTable;
    }

    public void setServerTable(ServerTable serverTable) {
        this.serverTable = serverTable;
    }

    public CJButton getAddServerButton() {
        return addServerButton;
    }

    public void setAddServerButton(CJButton addServerButton) {
        this.addServerButton = addServerButton;
    }

    @Override
    public void registerListener() {

    }


//    @Override
//    public void registerListener() {
//        trashButton.addActionListener(actionEvent -> {
//            getTriggerLogTable().clearTable();
//        });
//    }
}
