package oneview.ui.screens.common.util;

import oneview.bean.PomInfo;
import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;
import oneview.ui.component.CJTextField;
import oneview.ui.screens.build.AddRemoveBuildCommandUI;
import oneview.ui.screens.build.BuildScreen;
import oneview.ui.screens.build.context.BuildContext;
import oneview.ui.screens.build.table.BuildTableData;
import oneview.ui.screens.deploy.ArtifactUI;
import oneview.ui.screens.deploy.context.DeploymentContext;
import oneview.ui.screens.deploy.table.DeployTableData;
import oneview.ui.screens.deploy.table.DeployTableUI;
import oneview.util.DateUtil;
import oneview.util.EnvironmentVariables;
import oneview.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

import static java.awt.Image.SCALE_SMOOTH;
import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.BUSY_SPIN;
import static oneview.ui.constants.GuiConstants.FEATURE_REINSTALL;
import static oneview.ui.constants.IconConstants.APPLY_ICON;
import static oneview.ui.constants.IconConstants.CANCEL_ICON;
import static oneview.ui.screens.build.BuildScreen.BUILDS;
import static oneview.ui.screens.build.BuildScreen.NEW_BUILD_ADDED;
import static oneview.ui.screens.deploy.DeploymentScreen.*;
import static oneview.util.DatePatterns.DP_YYYY_MM_DD_HH_MM_SS;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class ScreenUtil {


    public static int showEditBuildUI(String title, BuildContext context, BuildTableData data, BuildScreen screen) {
        CJTextField projectNameTF = new CJTextField(360, TEXT_FIELD_HEIGHT);
        projectNameTF.setText(data == null ? "" : data.getProject());

        AddRemoveBuildCommandUI addRemoveBuildCommandUI = new AddRemoveBuildCommandUI(data);
        Icon applyBtn = FileUtil.getScaledIcon(APPLY_ICON, ICON_WIDTH / 2, ICON_HEIGHT / 2, SCALE_SMOOTH);
        Icon cancelBtn = FileUtil.getScaledIcon(CANCEL_ICON, ICON_WIDTH / 2, ICON_HEIGHT / 2, SCALE_SMOOTH);
        Object[] options = {applyBtn, cancelBtn};

        final JComponent[] inputs = new JComponent[]{
                new CJPanel(new CJLabel("Project Name"), projectNameTF, new FlowLayout(FlowLayout.LEFT)),
                newDummyPanelObj(),
                addRemoveBuildCommandUI
        };
        int optionBtnClicked = JOptionPane.showOptionDialog(screen, inputs,
                title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, ""
        );

        if (JOptionPane.YES_OPTION == optionBtnClicked) {
            String projectName = projectNameTF.getText();
            java.util.List<PomInfo> commands = addRemoveBuildCommandUI.getBuildCommandList();
            if (data == null) {
                data = new BuildTableData();
            }
            data.setProject(projectName);
            data.setCommands(commands);
            context.remove(NEW_BUILD_ADDED);
            context.set(NEW_BUILD_ADDED, data);

            context.getListValue(BUILDS).remove(data);
            context.getListValue(BUILDS).add(data);

            context.set(BUILDS, context.getListValue(BUILDS));
        }
        return optionBtnClicked;
    }

    public static int showAddArtifactDialog(String title, DeploymentContext context, DeployTableUI screen) {
        ArtifactUI artifactUI = new ArtifactUI();
        artifactUI.getArtifactLocation().getLocationTF().setText("/home/weblogic/PROJECT/MASTER/ucare-mass-trigger/ucare-mass-trigger-repos/target/ucare-mass-trigger-repos-21.2-SNAPSHOT.kar");
        Icon applyBtn = FileUtil.getScaledIcon(APPLY_ICON, ICON_WIDTH / 2, ICON_HEIGHT / 2, SCALE_SMOOTH);
        Icon cancelBtn = FileUtil.getScaledIcon(CANCEL_ICON, ICON_WIDTH / 2, ICON_HEIGHT / 2, SCALE_SMOOTH);
        Object[] options = {applyBtn, cancelBtn};

        final JComponent[] inputs = new JComponent[]{
                artifactUI
        };
        int optionBtnClicked = JOptionPane.showOptionDialog(screen, inputs,
                title, JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, ""
        );

        if (JOptionPane.YES_OPTION == optionBtnClicked) {
            DeployTableData data = new DeployTableData();
            Map<String, Object> artifactDetails = artifactUI.getFileDetails(artifactUI.getArtifactLocation().getLocationTF().getText());
            data.setArtifactPath(new File(artifactUI.getArtifactLocation().getLocationTF().getText()));
            data.setDeploymentTime(DateUtil.getCurrentDateObj(DP_YYYY_MM_DD_HH_MM_SS));
            data.setArtifactDetails(artifactDetails);
            data.setInstallOrUninstall(BUSY_SPIN);
            data.setReinstall(FEATURE_REINSTALL);
            context.set(NEW_ARTIFACT_ADDED, data);
            context.set(CONNECTOR_SERVER, EnvironmentVariables.getEnvVars("CONNECTOR_SERVER"));
            context.getListValue(ARTIFACTS).add(data);
        }
        return optionBtnClicked;
    }
}
