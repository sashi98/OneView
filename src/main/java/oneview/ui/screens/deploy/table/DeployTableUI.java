package oneview.ui.screens.deploy.table;

import com.jcraft.jsch.JSchException;
import oneview.karaf.pipeline.CommandPipelineBuilder;
import oneview.karaf.pipeline.CommandPipelineBuilderHelper;
import oneview.karaf.ssh.client.SshClient;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJButton;
import oneview.ui.component.CJPanel;
import oneview.ui.component.CJPopupTextView;
import oneview.ui.screens.common.util.ScreenUtil;
import oneview.ui.screens.deploy.context.DeploymentContext;
import oneview.ui.screens.deploy.pipeline.PipelineUI;
import oneview.ui.screens.deploy.table.renderer.InstallUninstallCellRenderer;
import oneview.ui.screens.deploy.table.renderer.LogCellRenderer;
import oneview.ui.screens.deploy.table.renderer.ViewPipelineCellRenderer;
import oneview.ui.screens.server.table.renderer.RestartCellRenderer;
import oneview.util.EnvironmentVariables;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.DimensionConstants.POPUP_TEXT_VIEW_HEIGHT;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.ADD_DEFAULT_ICON;
import static oneview.ui.constants.IconConstants.ADD_HOVERED_ICON;
import static oneview.ui.screens.deploy.DeploymentScreen.CONNECTOR_SERVER;

public class DeployTableUI extends CJPanel implements RegisterListener {
    private DeployTable deployTable;
    private CJButton addDeployButton;
    private DeploymentContext deployContext;

    public DeployTableUI(DeploymentContext deployContext, int width, int height) {
        super(width, height);
        this.deployContext = deployContext;
        this.deployTable = new DeployTable(new DeployTableDataModel(new ArrayList<>()));
        this.addDeployButton = new CJButton(ADD_DEFAULT_ICON, ADD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Add Deploy");
        JScrollPane tableScrollPane = new JScrollPane(deployTable);
        tableScrollPane.setLayout(new ScrollPaneLayout());
        this.setLayout(new BorderLayout(0, 0));
        CJPanel panel = new CJPanel(width, ICON_HEIGHT);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.add(addDeployButton);
        //panel.add(trashButton);
        this.add(panel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Deploys", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));
        registerListener();
    }

    public DeployTable getDeployTable() {
        return deployTable;
    }

    public void setDeployTable(DeployTable deployTable) {
        this.deployTable = deployTable;
    }

    public CJButton getAddDeployButton() {
        return addDeployButton;
    }

    public void setAddDeployButton(CJButton addDeployButton) {
        this.addDeployButton = addDeployButton;
    }

    @Override
    public void registerListener() {
        final DeployTableUI tableUI = this;
        addDeployButton.addActionListener(e -> {
            int optionBtnClicked = ScreenUtil.showAddArtifactDialog("Add New Artifact", deployContext, tableUI);
            if (JOptionPane.YES_OPTION == optionBtnClicked) {
                tableUI.getDeployTable().updateTable(deployContext);
            }
        });

        tableUI.getDeployTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int row = tableUI.getDeployTable().rowAtPoint(clickPoint);
                int col = tableUI.getDeployTable().columnAtPoint(clickPoint);
                Rectangle cellRect = tableUI.getDeployTable().getCellRect(row, col, true);
                DeployTableData data = tableUI.getDeployTable().getDeployTableDataModel().getDataList().get(row);

                if (col == DeployTableDataModel.ColumnIndex.INSTALL_UNINSTALL_INDEX.ordinal()) {
                    performInstallUninstallIconClick(cellRect,clickPoint,row,col);
                }
                if (col == DeployTableDataModel.ColumnIndex.REINSTALL_INDEX.ordinal()) {
                    performReInstallIconClick(cellRect,clickPoint,row);
                }
                if (col == DeployTableDataModel.ColumnIndex.FULL_LOG_INDEX.ordinal()) {
                    performLogIconClick(cellRect,clickPoint,row, col);
                }
                if (col == DeployTableDataModel.ColumnIndex.VIEW_PIPELINE_INDEX.ordinal()) {
                    performViewPipelineIconClick(cellRect,clickPoint,row, data);
                }
                deployTable.repaint();
            }

        });
    }

    private void performInstallUninstallIconClick(Rectangle cellRect, Point clickPoint, int row, int col) {
        InstallUninstallCellRenderer cellRenderer = (InstallUninstallCellRenderer) getDeployTable().getCellRenderer(row, col);
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            DeployTableData data = getDeployTable().getDeployTableDataModel().getDataList().get(row);
            getDeployTable().getDeployTableDataModel().fireTableDataChanged();
            new Thread(() -> {
                if (data.getInstallOrUninstall().equals(FEATURE_UNINSTALLED)) {
                    data.setInstallOrUninstall(BUSY_SPIN);
                    installFeature(data);
                }
                if (data.getInstallOrUninstall().equals(FEATURE_INSTALLED)){
                    data.setInstallOrUninstall(BUSY_SPIN);
                    uninstallFeature(data);

                }
                getDeployTable().getDeployTableDataModel().fireTableDataChanged();
            }).start();

            new Thread(()->{
                while (data.getInstallOrUninstall().equals(BUSY_SPIN)){
                    getDeployTable().waitFor(2000);
                }
            }).start();

        }
    }
    private void performLogIconClick(Rectangle cellRect, Point clickPoint, int row, int col) {
        LogCellRenderer cellRenderer = (LogCellRenderer) getDeployTable().getCellRenderer(row, col);
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            int x = (int) clickPoint.getX() - POPUP_TEXT_VIEW_X_SHIFT;
            int y = (int) clickPoint.getY();
            CJPopupTextView tv = new CJPopupTextView(getDeployTable().getDeployTableDataModel().getFullLog(row,col), x, y, getDeployTable().getInvoker(), POPUP_TEXT_VIEW_WIDTH, POPUP_TEXT_VIEW_HEIGHT);
            tv.showIt();
        }
    }

    private void performViewPipelineIconClick(Rectangle cellRect, Point clickPoint, int row, DeployTableData data) {
        ViewPipelineCellRenderer cellRenderer = (ViewPipelineCellRenderer) getDeployTable().getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.VIEW_PIPELINE_INDEX.ordinal()).getCellRenderer();
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            int x = (int) clickPoint.getX() - POPUP_TEXT_VIEW_X_SHIFT;
            int y = (int) clickPoint.getY();
            CJPopupTextView tv = new CJPopupTextView(new PipelineUI(700, 400, data.getPipeline()), x, y, getDeployTable().getInvoker(), POPUP_TEXT_VIEW_WIDTH, POPUP_TEXT_VIEW_HEIGHT);
            tv.showIt();
        }
    }

    private void performReInstallIconClick(Rectangle cellRect, Point clickPoint, int row) {
        RestartCellRenderer cellRenderer = (RestartCellRenderer) getDeployTable().getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.REINSTALL_INDEX.ordinal()).getCellRenderer();
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            DeployTableData data = getDeployTable().getDeployTableDataModel().getDataList().get(row);
            data.setReinstall(FEATURE_REINSTALLING);
            getDeployTable().getDeployTableDataModel().fireTableDataChanged();
            new Thread(() -> {
                reinstallFeature(data);
                getDeployTable().getDeployTableDataModel().fireTableDataChanged();
            }).start();

        }
    }

    private void reinstallFeature(DeployTableData data) {
        uninstallFeature(data);
        installFeature(data);
    }



    private void uninstallFeature(DeployTableData data) {
        try {
            CommandPipelineBuilder cpb = CommandPipelineBuilderHelper.getCommandPipelineBuilderHelper(new SshClient(), data);
            assert cpb != null;
            String fulLog = cpb.uninstallFeature()
                    .uninstallArchives()
                    .stopServer("/bin/stop")
                    .cleanUpFiles()
                    .startServer("/bin/start")
                    .getConsolidatedLogs();
            data.setFullLog(fulLog);
            data.setPipeline(cpb.getPipeline());
            if ((Boolean) data.getArtifactDetails().get(FEATURE_UNINSTALLED)){
                data.setInstallOrUninstall(FEATURE_UNINSTALLED);
            }
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
    }

    private void installFeature(DeployTableData data) {
        try {
            CommandPipelineBuilder cpb = CommandPipelineBuilderHelper.getCommandPipelineBuilderHelper(new SshClient(),data);
            String log = cpb.addDistribution()
                    .installFeature()
                    .getConsolidatedLogs();
            data.setFullLog(log);
            data.setPipeline(cpb.getPipeline());
        } catch (JSchException | IOException e) {
            e.printStackTrace();
        }
        if (!(Boolean) data.getArtifactDetails().get(FEATURE_UNINSTALLED)){
            data.setInstallOrUninstall(FEATURE_INSTALLED);
        }
    }

    public DeploymentContext getDeployContext() {
        return deployContext;
    }

    public void setDeployContext(DeploymentContext deployContext) {
        this.deployContext = deployContext;
    }
}
