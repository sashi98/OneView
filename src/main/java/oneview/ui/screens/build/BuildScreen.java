package oneview.ui.screens.build;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJPanel;
import oneview.ui.screens.common.IScreen;
import oneview.ui.screens.build.context.BuildContext;
import oneview.ui.screens.build.table.BuildTableUI;
import oneview.ui.screens.common.util.ScreenUtil;

import javax.swing.*;
import java.awt.*;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class BuildScreen extends CJPanel implements IScreen, RegisterListener {

    public static final String NEW_BUILD_ADDED = "NEW_BUILD_ADDED";
    public static final String BUILDS = "BUILDS";

    private BuildTableUI buildTableUI;
    private BuildContext buildContext;

    public BuildScreen() {
        this(new BuildContext());
    }

    public BuildScreen(BuildContext buildContext) {
        this.buildContext = buildContext;
        this.setFont(new Font("Arial", Font.PLAIN, 13));
        buildTableUI = new BuildTableUI(MAIN_GUI_WIDTH, SERVER_CENTER_PANEL_HEIGHT, buildContext);
        createUI();
        registerListener();
        loadSettings();
    }

    @Override
    public void registerListener() {
        BuildScreen buildScreen = this;
        buildTableUI.getAddBuildButton().addActionListener(e -> {
            int optionBtnClicked = ScreenUtil.showEditBuildUI("New Build", buildContext, null, buildScreen);
            if (JOptionPane.YES_OPTION == optionBtnClicked) {
                buildTableUI.getBuildTable().updateTable(buildContext);
            }
        });
    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout(0, 0));
        this.add(buildTableUI, BorderLayout.CENTER);
    }

    @Override
    public void loadSettings() {

    }


}


