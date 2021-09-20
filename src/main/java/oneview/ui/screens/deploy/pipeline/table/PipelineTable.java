package oneview.ui.screens.deploy.pipeline.table;

import oneview.ui.component.CJPopupTextView;
import oneview.ui.component.CJTable;
import oneview.ui.screens.common.context.OneViewContext;
import oneview.ui.screens.common.table.renderer.ColumnHeaderRenderer;
import oneview.ui.screens.common.table.renderer.SortingColumnHeaderRenderer;
import oneview.ui.screens.deploy.pipeline.table.renderer.PipelineCellRenderer;
import oneview.ui.screens.deploy.table.DeployTableDataModel;
import oneview.ui.screens.deploy.table.renderer.LogCellRenderer;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;


public class PipelineTable extends CJTable {
    private PipelineTableDataModel model;
    private LogCellRenderer logCellRenderer;
    private SortingColumnHeaderRenderer sortingColumnHeaderRenderer;
    private ColumnHeaderRenderer columnHeaderRenderer;
    private boolean DESC = true;
    private int defaultSortingColumn = 1;


    public PipelineTable(PipelineTableDataModel tm) {
        super(tm);
        this.model = tm;
        this.sortingColumnHeaderRenderer = new SortingColumnHeaderRenderer(DESC);
        this.columnHeaderRenderer = new ColumnHeaderRenderer();
        this.logCellRenderer = new LogCellRenderer();
        
        getTableHeader().setPreferredSize(new Dimension(100, 25));
        setRowSelectionAllowed(false);
        setSelectionBackground(Color.white);
        setCellRenderer();
        setHeaderRenderer();
        setRowHeight(60);
    }



    private void setCellRenderer() {
        //PipelineCellRenderer defaultTableCellRenderer = new PipelineCellRenderer();

//        getColumnModel().getColumn(PipelineTableDataModel.ColumnIndex.UNINSTALL_INDEX.ordinal()).setCellRenderer(new PipelineCellRenderer());
//        getColumnModel().getColumn(PipelineTableDataModel.ColumnIndex.STOP_SERVER_INDEX.ordinal()).setCellRenderer(new PipelineCellRenderer());
//        getColumnModel().getColumn(PipelineTableDataModel.ColumnIndex.CLEANUP_INDEX.ordinal()).setCellRenderer(new PipelineCellRenderer());
//        getColumnModel().getColumn(PipelineTableDataModel.ColumnIndex.START_SERVER_INDEX.ordinal()).setCellRenderer(new PipelineCellRenderer());
//        getColumnModel().getColumn(PipelineTableDataModel.ColumnIndex.ADD_DISTRIBUTION_INDEX.ordinal()).setCellRenderer(new PipelineCellRenderer());
//        getColumnModel().getColumn(PipelineTableDataModel.ColumnIndex.INSTALL_INDEX.ordinal()).setCellRenderer(new PipelineCellRenderer());

        for (int i=0; i< getModel().getColumnCount();i++){
            getColumnModel().getColumn(i).setCellRenderer(new PipelineCellRenderer());
        }
    }

    public int getDefaultSortingColumn() {
        return defaultSortingColumn;
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
    @Override
    public void updateTable(OneViewContext ctx) {

    }

    @Override
    public CJTable getInvoker() {
        return this;
    }


}
