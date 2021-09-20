package oneview.ui.screens.server;

import oneview.ui.screens.common.IScreen;
import oneview.ui.screens.server.context.ServerContext;
import oneview.ui.RegisterListener;
import oneview.ui.component.*;
import oneview.ui.component.CJLocation;
import oneview.ui.screens.server.table.ServerTableData;
import oneview.ui.screens.server.table.ServersTableUI;
import oneview.util.FileUtil;

import javax.swing.*;
import java.awt.*;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;
import static java.awt.Image.SCALE_SMOOTH;

public class ServersScreen extends CJPanel implements IScreen,RegisterListener {

    public static final String NEW_SERVER_ADDED = "NEW_SERVER_ADDED";
    public static final String SERVERS = "SERVERS";


    private ServersTableUI serversTableUI;
    private ServerContext serverContext;

    public ServersScreen(){
        this(new ServerContext());
    }
    public ServersScreen(ServerContext serverContext) {
        this.serverContext = serverContext;
        this.setFont(new Font("Arial", Font.PLAIN, 13));
        serversTableUI = new ServersTableUI(MAIN_GUI_WIDTH,SERVER_CENTER_PANEL_HEIGHT);
        createUI();
        registerListener();
        loadSettings();
    }

    @Override
    public void registerListener() {
        ServersScreen serversScreen = this;
        serversTableUI.getAddServerButton().addActionListener(e -> {
            CJTextField serverName = new CJTextField(250, TEXT_FIELD_HEIGHT);
            CJLocation serverHomeLocation = new CJLocation(null, 280, JFileChooser.FILES_AND_DIRECTORIES);
            CJLocation serverStartLocation = new CJLocation(null, 280, JFileChooser.FILES_AND_DIRECTORIES);
            CJLocation serverStopLocation = new CJLocation(null, 280, JFileChooser.FILES_AND_DIRECTORIES);
            Icon applyBtn = FileUtil.getScaledIcon(APPLY_ICON, ICON_WIDTH/2, ICON_HEIGHT/2, SCALE_SMOOTH);
            Icon cancelBtn = FileUtil.getScaledIcon(CANCEL_ICON, ICON_WIDTH/2, ICON_HEIGHT/2,SCALE_SMOOTH);
            Object[] options = {applyBtn, cancelBtn};

            serverName.setText("Weblogic");
            serverHomeLocation.getLocationTF().setText("/opt/oracle/weblogic12cR2/user_projects/domains/healthedge");
            serverStartLocation.getLocationTF().setText("/opt/oracle/weblogic12cR2/user_projects/domains/healthedge/start_server.sh");
            serverStopLocation.getLocationTF().setText("/opt/oracle/weblogic12cR2/user_projects/domains/healthedge/stop_server.sh");
            final JComponent[] inputs = new JComponent[] {
                    new CJPanel(new CJLabel("Server Name"),serverName),
                    newDummyPanelObj(),
                    new CJPanel(new CJLabel("Server Home"),serverHomeLocation),
                    newDummyPanelObj(),
                    new CJPanel(new CJLabel("Server Start"),serverStartLocation),
                    newDummyPanelObj(),
                    new CJPanel(new CJLabel("Server Stop"),serverStopLocation)
            };
            int optionBtnClicked = JOptionPane.showOptionDialog(null, inputs,
                    "New Server", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, ""
                    );
            if (JOptionPane.YES_OPTION == optionBtnClicked){
                ServerTableData newServer = new ServerTableData();
                newServer.setServerName(serverName.getText());
                newServer.setServerHome(serverHomeLocation.getLocationTF().getText());
                newServer.setStartScriptPath(serverStartLocation.getLocationTF().getText());
                newServer.setStopScriptPath(serverStopLocation.getLocationTF().getText());
                serverContext.set(NEW_SERVER_ADDED, newServer);
                serverContext.set(SERVERS, serverContext.getListValue(SERVERS).add(newServer));
                serversTableUI.getServerTable().updateTable(serverContext);
            }
        });
    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.add(serversTableUI,BorderLayout.CENTER);
    }

    @Override
    public void loadSettings() {

    }


//    @Override
//    public void registerListener() {
//        trashButton.addActionListener(actionEvent -> {
//            getTriggerLogTable().clearTable();
//        });
//    }
}
