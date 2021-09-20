package oneview.ui.screens.build.table;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJTable;
import oneview.ui.screens.build.context.BuildContext;
import oneview.ui.screens.build.table.renderer.BuildEditCellRenderer;
import oneview.ui.screens.build.table.renderer.BuildFaildLogRenderer;
import oneview.ui.screens.build.table.renderer.BuildStatusCellRenderer;
import oneview.ui.screens.common.context.OneViewContext;
import oneview.ui.screens.common.table.renderer.*;
import oneview.util.DateUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import static oneview.ui.screens.build.BuildScreen.NEW_BUILD_ADDED;
import static oneview.util.DatePatterns.DP_YYYY_MM_DD_HH_MM_SS;

public class BuildTable extends CJTable implements RegisterListener {
    private BuildTableDataModel buildTableDataModel;
    private TimeCellRenderer timeCellRenderer;
    private BuildStatusCellRenderer buildStatusCellRenderer;
    private BuildFaildLogRenderer buildFaildLogRenderer;
    private BuildEditCellRenderer buildEditCellRenderer;
    private SortingColumnHeaderRenderer sortingColumnHeaderRenderer;
    private ColumnHeaderRenderer columnHeaderRenderer;
    private int defaultSortingColumn = 1;
    private boolean DESC = true;

    public BuildTable(BuildTableDataModel dataModel) {
        super(dataModel);
        this.buildTableDataModel = dataModel;
        this.sortingColumnHeaderRenderer = new SortingColumnHeaderRenderer(DESC);
        this.columnHeaderRenderer = new ColumnHeaderRenderer();
        this.timeCellRenderer = new TimeCellRenderer();
        this.buildStatusCellRenderer = new BuildStatusCellRenderer();
        this.buildFaildLogRenderer = new BuildFaildLogRenderer();
        this.buildEditCellRenderer = new BuildEditCellRenderer();
        getTableHeader().setPreferredSize(new Dimension(100, 25));
        setRowSelectionAllowed(false);
        setSelectionBackground(Color.white);
        setCellRenderer();
        setHeaderRenderer();
        registerListener();
        setRowHeight(30);

    }

    public BuildTable getInvoker() {
        return this;
    }

    public void setCellRenderer() {
        NormalCellRenderer defaultTableCellRenderer = new NormalCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        defaultTableCellRenderer.setBorder(new LineBorder(Color.BLACK));

        getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.PROJECT_INDEX.ordinal()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.BUILD_TIME_INDEX.ordinal()).setCellRenderer(timeCellRenderer);
        getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.RUN_INDEX.ordinal()).setCellRenderer(buildStatusCellRenderer);
        getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.FAILED_LOG_INDEX.ordinal()).setCellRenderer(buildFaildLogRenderer);
        getColumnModel().getColumn(BuildTableDataModel.ColumnIndex.EDIT_INDEX.ordinal()).setCellRenderer(buildEditCellRenderer);

    }

    public void setHeaderRenderer() {
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

    public BuildTableDataModel getBuildTableDataModel() {
        return buildTableDataModel;
    }

    @Override
    public void updateTable(OneViewContext context) {
        final BuildTable buildTable = this;
        if (context instanceof BuildContext) {
            BuildContext buildContext = (BuildContext) context;
            BuildTableData data = ((BuildTableData) buildContext.getObject(NEW_BUILD_ADDED)).clone();
            data.setBuildTime(DateUtil.getCurrentDateObj(DP_YYYY_MM_DD_HH_MM_SS));
            ((BuildTableDataModel)this.getModel()).getDataList().add(data);
            buildTableDataModel.fireTableDataChanged();
            buildTable.repaint();
        }
    }


    public void sort() {

    }

    public int getDefaultSortingColumn() {
        return defaultSortingColumn;
    }

    public void setDefaultSortingColumn(int defaultSortingColumn) {
        this.defaultSortingColumn = defaultSortingColumn;
    }

    public void clearTable() {
        buildTableDataModel.getDataList().removeAll(buildTableDataModel.getDataList());
        buildTableDataModel.fireTableDataChanged();
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
        final BuildTable buildTable = this;

        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortingPerformed(e);
            }
        });
    }





}

