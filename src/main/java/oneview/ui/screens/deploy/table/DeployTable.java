package oneview.ui.screens.deploy.table;

import com.jcraft.jsch.JSchException;
import oneview.karaf.pipeline.CommandPipelineBuilder;
import oneview.karaf.pipeline.CommandPipelineBuilderHelper;
import oneview.karaf.ssh.client.SshClient;
import oneview.script.ScriptRunner;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJPopupTextView;
import oneview.ui.component.CJTable;
import oneview.ui.screens.common.context.OneViewContext;
import oneview.ui.screens.common.table.renderer.ColumnHeaderRenderer;
import oneview.ui.screens.common.table.renderer.NormalCellRenderer;
import oneview.ui.screens.common.table.renderer.SortingColumnHeaderRenderer;
import oneview.ui.screens.common.table.renderer.TimeCellRenderer;
import oneview.ui.screens.deploy.context.DeploymentContext;
import oneview.ui.screens.deploy.pipeline.PipelineUI;
import oneview.ui.screens.deploy.table.renderer.InstallUninstallCellRenderer;
import oneview.ui.screens.deploy.table.renderer.LogCellRenderer;
import oneview.ui.screens.deploy.table.renderer.ReinstallCellRenderer;
import oneview.ui.screens.deploy.table.renderer.ViewPipelineCellRenderer;
import oneview.ui.screens.server.table.renderer.RestartCellRenderer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.Enumeration;

import static oneview.archive.ArchieveConstants.*;
import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.screens.deploy.DeploymentScreen.NEW_ARTIFACT_ADDED;

public class DeployTable extends CJTable implements RegisterListener {
    private DeployTableDataModel tableDataModel;
    private InstallUninstallCellRenderer installUninstallCellRenderer;
    private TimeCellRenderer timeCellRenderer;
    private ReinstallCellRenderer reinstallCellRenderer;
    private LogCellRenderer logCellRenderer;
    private ViewPipelineCellRenderer viewPipelineCellRenderer;
    private SortingColumnHeaderRenderer sortingColumnHeaderRenderer;
    private ColumnHeaderRenderer columnHeaderRenderer;
    private int defaultSortingColumn = 1;
    private boolean DESC = true;
    private ScriptRunner scriptRunner;

    public DeployTable(DeployTableDataModel dataModel) {
        super(dataModel);
        this.tableDataModel = dataModel;
        this.sortingColumnHeaderRenderer = new SortingColumnHeaderRenderer(DESC);
        this.columnHeaderRenderer = new ColumnHeaderRenderer();
        this.installUninstallCellRenderer = new InstallUninstallCellRenderer();
        this.reinstallCellRenderer = new ReinstallCellRenderer();
        this.timeCellRenderer = new TimeCellRenderer();
        this.logCellRenderer = new LogCellRenderer();
        this.viewPipelineCellRenderer = new ViewPipelineCellRenderer();
        getTableHeader().setPreferredSize(new Dimension(100, 25));
        setRowSelectionAllowed(false);
        setSelectionBackground(Color.white);
        setCellRenderer();
        setHeaderRenderer();
        registerListener();
        setRowHeight(30);

        scriptRunner = new ScriptRunner();
    }

    @Override
    public CJTable getInvoker() {
        return this;
    }

    private void setCellRenderer() {
        NormalCellRenderer defaultTableCellRenderer = new NormalCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        defaultTableCellRenderer.setBorder(new LineBorder(Color.BLACK));

        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.FEATURE_NAME_INDEX.ordinal()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.VERSION_INDEX.ordinal()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.TIME_INDEX.ordinal()).setCellRenderer(timeCellRenderer);
        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.INSTALL_UNINSTALL_INDEX.ordinal()).setCellRenderer(installUninstallCellRenderer);
        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.REINSTALL_INDEX.ordinal()).setCellRenderer(reinstallCellRenderer);
        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.FULL_LOG_INDEX.ordinal()).setCellRenderer(logCellRenderer);
        getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.VIEW_PIPELINE_INDEX.ordinal()).setCellRenderer(viewPipelineCellRenderer);

    }

    private void setHeaderRenderer() {
        Enumeration<TableColumn> tableColumns = this.getTableHeader().getColumnModel().getColumns();
        while (tableColumns.hasMoreElements()) {
            TableColumn tc = tableColumns.nextElement();
            if (tc.getModelIndex() == getDefaultSortingColumn()) {
                tc.setHeaderRenderer(sortingColumnHeaderRenderer);
            } else {
                tc.setHeaderRenderer(columnHeaderRenderer);
            }
        }
    }

    public DeployTableDataModel getDeployTableDataModel() {
        return tableDataModel;
    }

    @Override
    public void updateTable(OneViewContext context) {
        final DeployTable deployTable = this;
        if (context instanceof DeploymentContext) {
            DeploymentContext deployContext = (DeploymentContext) context;
            DeployTableData data = ((DeployTableData) deployContext.getObject(NEW_ARTIFACT_ADDED)).clone();
            tableDataModel.getDataList().add(data);
            tableDataModel.fireTableDataChanged();
            repaint();
            new Thread(() -> {
                while (data.getInstallOrUninstall().equals(BUSY_SPIN)){
                    deployTable.waitFor(2000);
                }
            }).start();

            new Thread(() -> {
                if (isFeatureInstalled(data)){
                    data.setInstallOrUninstall(FEATURE_INSTALLED);
                    data.setReinstall(FEATURE_REINSTALLED);
                } else{
                    data.setInstallOrUninstall(FEATURE_UNINSTALLED);
                    data.setReinstall(FEATURE_REINSTALL);
                }
            }).start();
        }
    }

    private boolean isFeatureInstalled(DeployTableData data) {
        CommandPipelineBuilder cpb;

        try {
            cpb = CommandPipelineBuilderHelper.getCommandPipelineBuilderHelper(new SshClient(), data);
            String fullLog = cpb.listFeature().getConsolidatedLogs();
            data.setFullLog(fullLog);
            data.setPipeline(cpb.getPipeline());
            if (cpb.getListFeatureLog().contains("ERROR") || !cpb.isFeatureInstalled()){
                return false;
            }
        } catch (IOException | JSchException e) {
            e.printStackTrace();
            data.setFullLog(e.getLocalizedMessage());
            return false;
        }
        return true;
    }


    private void sort() {

    }

    public int getDefaultSortingColumn() {
        return defaultSortingColumn;
    }

    public void setDefaultSortingColumn(int defaultSortingColumn) {
        this.defaultSortingColumn = defaultSortingColumn;
    }

    public void clearTable() {
        tableDataModel.getDataList().removeAll(tableDataModel.getDataList());
        tableDataModel.fireTableDataChanged();
    }

    public void sortingPerformed(MouseEvent e) {
        DESC = !DESC;
        sortingColumnHeaderRenderer.setDesc(DESC);
        TableColumnModel colModel = ((JTableHeader) e.getSource()).getColumnModel();
        int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
        setDefaultSortingColumn(columnModelIndex);
        setHeaderRenderer();
        sort();
    }

    @Override
    public void registerListener() {
        final DeployTable deployTable = this;
        addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent mouseEvent) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int row = rowAtPoint(clickPoint);
                int col = columnAtPoint(clickPoint);
                Rectangle cellRect = getCellRect(row, col, true);
                DeployTableData data = tableDataModel.getDataList().get(row);
                if (col == DeployTableDataModel.ColumnIndex.VIEW_PIPELINE_INDEX.ordinal()) {
                    performViewEyeOnOff(cellRect,clickPoint,row,col,data);
                } else {
                    closeEye(data);
                }
                tableDataModel.fireTableDataChanged();
                repaint();
            }
        });

        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortingPerformed(e);
            }
        });
    }

    private void closeEye(DeployTableData data) {
        data.setViewPipeline(VIEW_PIPELINE_STATE);
    }

    private void performViewEyeOnOff(Rectangle cellRect, Point clickPoint, int row, int col, DeployTableData data) {
        ViewPipelineCellRenderer cellRenderer = (ViewPipelineCellRenderer) getColumnModel().getColumn(DeployTableDataModel.ColumnIndex.VIEW_PIPELINE_INDEX.ordinal()).getCellRenderer();
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            data.setViewPipeline(VIEW_PIPELINE_HOVERED_STATE);
        } else {
            data.setViewPipeline(VIEW_PIPELINE_STATE);
        }
    }


}

