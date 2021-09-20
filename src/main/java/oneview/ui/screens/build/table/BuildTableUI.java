package oneview.ui.screens.build.table;

import oneview.script.ScriptRunner;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJButton;
import oneview.ui.component.CJPanel;
import oneview.ui.component.CJPopupTextView;
import oneview.ui.icon.ImagePanel;
import oneview.ui.screens.build.context.BuildContext;
import oneview.ui.screens.build.table.renderer.BuildEditCellRenderer;
import oneview.ui.screens.build.table.renderer.BuildFaildLogRenderer;
import oneview.ui.screens.build.table.renderer.BuildStatusCellRenderer;
import oneview.ui.screens.common.util.ScreenUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static oneview.script.ScriptRunner.EXECUTING;
import static oneview.ui.constants.DimensionConstants.ICON_HEIGHT;
import static oneview.ui.constants.DimensionConstants.ICON_WIDTH;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.ADD_DEFAULT_ICON;
import static oneview.ui.constants.IconConstants.ADD_HOVERED_ICON;

public class BuildTableUI extends CJPanel implements RegisterListener {
    private BuildTable buildTable;
    private CJButton addBuildButton;
    private ScriptRunner scriptRunner;
    private BuildContext buildContext;
    public BuildTableUI(int width, int height, BuildContext buildContext) {
        super(width,height);
        this.buildContext = buildContext;
        buildTable = new BuildTable(new BuildTableDataModel(new ArrayList<>()));
        addBuildButton = new CJButton(ADD_DEFAULT_ICON, ADD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Add Build");
        JScrollPane tableScrollPane = new JScrollPane(buildTable);
        tableScrollPane.setLayout(new ScrollPaneLayout());
        this.setLayout(new BorderLayout(0,0));
        CJPanel panel = new CJPanel(width, ICON_HEIGHT);
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 0,0));
        panel.add(addBuildButton);
        //panel.add(trashButton);
        this.add(panel, BorderLayout.NORTH);
        this.add(tableScrollPane, BorderLayout.CENTER);
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Build", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));

        scriptRunner = new ScriptRunner();

        registerListener();
    }

    public BuildTable getBuildTable() {
        return buildTable;
    }

    public void setBuildTable(BuildTable buildTable) {
        this.buildTable = buildTable;
    }

    public CJButton getAddBuildButton() {
        return addBuildButton;
    }

    public void setAddBuildButton(CJButton addBuildButton) {
        this.addBuildButton = addBuildButton;
    }

    @Override
    public void registerListener() {
        getBuildTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int row = buildTable.rowAtPoint(clickPoint);
                int col = buildTable.columnAtPoint(clickPoint);
                Rectangle cellRect = buildTable.getCellRect(row, col, true);
                if (col == BuildTableDataModel.ColumnIndex.RUN_INDEX.ordinal()) {
                    runButtonClickActionPerformed(e, cellRect, clickPoint, row);
                    buildTable.repaint();
                }
                if(col == BuildTableDataModel.ColumnIndex.FAILED_LOG_INDEX.ordinal()){
                    logIconClickActionPerformed(e, cellRect, clickPoint, row);
                }
                if(col == BuildTableDataModel.ColumnIndex.EDIT_INDEX.ordinal()){
                    editIconClickActionPerformed(e, cellRect, clickPoint, row);
                }

            }

        });
    }

    private void runButtonClickActionPerformed(MouseEvent e, Rectangle cellRect, Point clickPoint, int row) {
        BuildStatusCellRenderer cellRenderer = (BuildStatusCellRenderer) buildTable.getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.RUN_INDEX.ordinal()).getCellRenderer();
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            BuildTableData data = getBuildTableModel().getDataList().get(row);
            data.setBuildStatus(BUSY_SPIN);
            getBuildTableModel().fireTableDataChanged();
            new Thread(() -> {
                runBuild(data);
                getBuildTableModel().fireTableDataChanged();
            }).start();
        }
    }

    private BuildTableDataModel getBuildTableModel() {
        return buildTable.getBuildTableDataModel();
    }

    private void editIconClickActionPerformed(MouseEvent e, Rectangle cellRect, Point clickPoint, int row) {
        BuildEditCellRenderer cellRenderer = (BuildEditCellRenderer) buildTable.getColumnModel().getColumn(
                BuildTableDataModel.ColumnIndex.EDIT_INDEX.ordinal()).getCellRenderer();
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            BuildTableData data = ((BuildTableDataModel)buildTable.getModel()).getDataList().get(row);
            int optionBtnClicked = ScreenUtil.showEditBuildUI("Edit Build", buildContext, data, null);
            if (JOptionPane.YES_OPTION == optionBtnClicked) {
                getBuildTableModel().getDataList().remove(data);
                buildTable.updateTable(buildContext);
            }
        }
    }

    private void logIconClickActionPerformed(MouseEvent e, Rectangle cellRect, Point clickPoint, int row) {
        BuildFaildLogRenderer cellRenderer = (BuildFaildLogRenderer) buildTable.getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.FAILED_LOG_INDEX.ordinal()).getCellRenderer();
        CJPanel component = cellRenderer.getComponent();
        Component[] ipPanels = component.getComponents();
        BuildTableData buildTableData =  ((BuildTableDataModel)buildTable.getModel()).getDataList().get(row);
        for (Component ipPanel: ipPanels){
            if(ipPanel instanceof ImagePanel) {
                String artifactId = ((ImagePanel) ipPanel).getComponentId();
                Rectangle ipRect = ipPanel.getBounds();
                Rectangle ipBounds = new Rectangle(cellRect.x + component.getBounds().x + ipRect.x, cellRect.y + component.getBounds().y + ipRect.y, ipRect.width, ipRect.height);
                if (ipBounds.contains(clickPoint)) {
                    int x = (int) clickPoint.getX() - 500;
                    int y = (int) clickPoint.getY();
                    CJPopupTextView tv = new CJPopupTextView(buildTableData.getLog(artifactId), x, y, buildTable.getInvoker(), 500, 300);
                    tv.showIt();
                    break;
                }
            }
        }
    }

    private void runBuild(BuildTableData data) {
        AtomicBoolean overAllRunStatusGreen = new AtomicBoolean(true);
        data.getCommands().forEach(command -> {
            Process process = scriptRunner.runMVN(command.getCommand()
            );
            while (scriptRunner.getStatus().equals(EXECUTING)) {
                buildTable.waitFor(1000);
            }
            String log = scriptRunner.captureOutput(process);
            boolean buildSuccess = scriptRunner.determineBuildSuccess(log);
            if (!buildSuccess) {
                command.setPomRunLog(log);
            }
            command.setPomRunStatus(buildSuccess ? POM_RUN_SUCCESS : POM_RUN_FAILURE);
            if (overAllRunStatusGreen.get()) {
                overAllRunStatusGreen.set(buildSuccess);
            }
        });

        if (overAllRunStatusGreen.get()) {
            data.setBuildStatus(BUILD_SUCCESS);
        } else {
            data.setBuildStatus(BUILD_FAILED);
        }
    }

}
