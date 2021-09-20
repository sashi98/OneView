package oneview.ui.screens.deploy;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJPanel;
import oneview.ui.screens.common.IScreen;
import oneview.ui.screens.deploy.context.DeploymentContext;
import oneview.ui.screens.deploy.table.DeployTableUI;

import java.awt.*;

import static oneview.ui.constants.DimensionConstants.MAIN_GUI_WIDTH;
import static oneview.ui.constants.DimensionConstants.SERVER_CENTER_PANEL_HEIGHT;

public class DeploymentScreen extends CJPanel implements IScreen, RegisterListener {


    public static final String NEW_ARTIFACT_ADDED = "NEW_ARTIFACT_ADDED";
    public static final String ARTIFACTS = "ARTIFACTS";
    public static final String CONNECTOR_SERVER = "CONNECTOR_SERVER";

    private DeployTableUI deployTableUI;

    public DeploymentScreen(DeploymentContext deployContext) {
        this.setFont(new Font("Arial", Font.PLAIN, 13));
        deployTableUI = new DeployTableUI(deployContext, MAIN_GUI_WIDTH, SERVER_CENTER_PANEL_HEIGHT);
        createUI();
        registerListener();
        loadSettings();
    }

    @Override
    public void registerListener() {

    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.add(deployTableUI, BorderLayout.CENTER);
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
