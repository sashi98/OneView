package oneview.ui.screens.jobtrigger.table;

import oneview.ui.screens.jobtrigger.context.ESContext;
import oneview.ui.screens.common.context.OneViewContext;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPopupTextView;
import oneview.ui.component.CJTable;
import oneview.ui.icon.ImagePanel;
import oneview.ui.screens.jobtrigger.table.renderer.ErrorLogCellRenderer;
import oneview.ui.screens.jobtrigger.table.renderer.StatusCellRenderer;
import oneview.ui.screens.common.table.renderer.*;
import oneview.util.FileUtil;
import oneview.util.StringUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Comparator;
import java.util.Enumeration;

import static oneview.ui.screens.jobtrigger.context.ESContext.*;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.SLASH;
import static oneview.util.DatePatterns.DP_YYYYMMDD;
import static oneview.util.DatePatterns.DP_YYYY_MM_DD_HH_MM_SS_SSS;
import static oneview.util.DateUtil.getCurrentDateAsStr;
import static oneview.util.ThreadUtil.sleep;

public class TriggerLogTable extends CJTable implements RegisterListener {
    private volatile TriggerLogTableDataModel triggerLogTableDataModel;
    private StatusCellRenderer statusCellRenderer;
    private ErrorLogCellRenderer errorLogCellRenderer;
    private SortingColumnHeaderRenderer sortingColumnHeaderRenderer;
    private ColumnHeaderRenderer columnHeaderRenderer;
    private int defaultSortingColumn = 1;
    private boolean DESC = true;
    private JPopupMenu pm;
    public TriggerLogTable(TriggerLogTableDataModel dataModel) {
        super(dataModel);
        this.triggerLogTableDataModel = dataModel;
        this.statusCellRenderer = new StatusCellRenderer();
        this.errorLogCellRenderer = new ErrorLogCellRenderer();
        this.sortingColumnHeaderRenderer = new SortingColumnHeaderRenderer(DESC);
        this.columnHeaderRenderer = new ColumnHeaderRenderer();
        pm = new JPopupMenu();
        pm.add(new CJLabel());
        getTableHeader().setPreferredSize(new Dimension(100, 25));

        setSelectionBackground(Color.LIGHT_GRAY);
        setCellRenderer();
        setHeaderRenderer();
        registerListener();
        setRowHeight(30);
    }

    @Override
    public CJTable getInvoker() {
        return this;
    }

    private void setCellRenderer() {
        NormalCellRenderer defaultTableCellRenderer = new NormalCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        defaultTableCellRenderer.setBorder(new LineBorder(Color.BLACK));

        getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.JOB_NAME_INDEX.getColIdx()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.JOB_TRIGGERED_AT_INDEX.getColIdx()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.START_DATE_INDEX.getColIdx()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.END_DATE_INDEX.getColIdx()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.STATUS_INDEX.getColIdx()).setCellRenderer(statusCellRenderer);
        getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.ERROR_LOG_INDEX.getColIdx()).setCellRenderer(errorLogCellRenderer);
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

    public TriggerLogTableDataModel getTriggerLogTableDataModel() {
        return triggerLogTableDataModel;
    }


    @Override
    public void updateTable(OneViewContext context) {
        ESContext esContext = null;
        if (context instanceof ESContext) {
            esContext = (ESContext) context;
            ESContext finalEsContext = esContext;
            new Thread(() -> {
                TriggerLogTableData tableData = new TriggerLogTableData();
                tableData.setJobName(finalEsContext.getStringValue(CRON_JOB_NAME));
                tableData.setTriggeredAt(getCurrentDateAsStr(DP_YYYY_MM_DD_HH_MM_SS_SSS));
                tableData.setStartDate(finalEsContext.getStringValue(START_DATE_TIME));
                tableData.setEndDate(finalEsContext.getStringValue(END_DATE_TIME));
                triggerLogTableDataModel.getDataList().add(tableData);
                sort();
                tableData.setStatus(BUSY_SPIN);
                triggerLogTableDataModel.fireTableDataChanged();
                sleep(500);
                File f = new File(finalEsContext.getStringValue(ON_DEMAND_DIR_LOCATION), finalEsContext.getStringValue(TRIGGER_FILE_NAME));
                while (f.exists()) {
                    repaint();
                    sleep(100);
                }
                String successFileName = FileUtil.getMatchedFile(finalEsContext.getStringValue(SUCCESS_DIR_LOCATION), f.getName());
                if (StringUtil.isNotBlank(successFileName)) {
                    tableData.setStatus(OK);
                    tableData.getInfo().setInfo(OK);
                    repaint();
                    return;
                }
                String failureFileName = FileUtil.getMatchedFile(finalEsContext.getStringValue(FAILURE_DIR_LOCATION), f.getName());
                if (StringUtil.isNotBlank(failureFileName)) {
                    tableData.setStatus(NOT_OK);
                    tableData.getInfo().setInfo(FAILURE);
                    tableData.getInfo().setLog(FileUtil.readFile(finalEsContext.getStringValue(LOG_DIR_LOCATION) + SLASH + getCurrentDateAsStr(DP_YYYYMMDD), "error"));
                    repaint();
                    return;
                }
            }).start();
        }
    }



    private void sort() {
        switch (getDefaultSortingColumn()) {
            case 0:
                if (DESC) {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getJobName).reversed());
                } else {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getJobName));
                }
                break;
            case 1:
                if (DESC) {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getStartDate).reversed());
                } else {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getStartDate));
                }
                break;
            case 2:
                if (DESC) {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getEndDate).reversed());
                } else {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getEndDate));
                }
                break;
            case 3:
                if (DESC) {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getStatus).reversed());
                } else {
                    triggerLogTableDataModel.getDataList().sort(Comparator.comparing(TriggerLogTableData::getStatus));
                }
                break;
        }

    }

    public int getDefaultSortingColumn() {
        return defaultSortingColumn;
    }

    public void setDefaultSortingColumn(int defaultSortingColumn) {
        this.defaultSortingColumn = defaultSortingColumn;
    }

    public void clearTable() {
        triggerLogTableDataModel.getDataList().removeAll(triggerLogTableDataModel.getDataList());
        triggerLogTableDataModel.fireTableDataChanged();
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
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row=rowAtPoint(e.getPoint());
                int col=columnAtPoint(e.getPoint());
                //triggerLogTableDataModel.getValueAt(row, STATUS);
                ErrorLogInfo errorLogInfo = (ErrorLogInfo) getTriggerLogTableDataModel().getValueAt(row, col);

                if(col == TriggerLogTableDataModel.ColumnIndex.ERROR_LOG_INDEX.getColIdx() && errorLogInfo.getInfo() == FAILURE) {
                    Rectangle cellRect = getCellRect(row, col, true);
                    ErrorLogCellRenderer cellRenderer = (ErrorLogCellRenderer) getColumnModel().getColumn(TriggerLogTableDataModel.ColumnIndex.ERROR_LOG_INDEX.ordinal()).getCellRenderer();
                    JComponent ip = cellRenderer.getComponent();
                    if (ip instanceof ImagePanel) {
                        Rectangle ipRect = ip.getBounds();
                        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
                        if (ipBounds.contains(e.getPoint())) {
                            int x = (int) e.getPoint().getX() - 500;
                            int y = (int) e.getPoint().getY();
                            CJPopupTextView tv = new CJPopupTextView(errorLogInfo.getLog(), x, y, getInvoker(), 500, 300);
                            tv.showIt();
                        }
                    }
                }
            }

        });

        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortingPerformed(e);
            }
        });
    }
}
