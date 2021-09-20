package oneview.ui.screens.jobtrigger.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class TriggerLogTableDataModel extends AbstractTableModel implements TableModelListener {
    private static final String JOB_NAME = "Job Name";
    private static final String JOB_TRIGGERED_AT = "Triggered At";
    private static final String START_DATE = "Start Date";
    private static final String END_DATE = "End Date";
    public static final String STATUS = "Status";
    public static final String ERROR_LOG = "Error Log";

    private String[] header = new String[]{JOB_NAME,
                                        JOB_TRIGGERED_AT, START_DATE, END_DATE,
                                        STATUS, ERROR_LOG};
    public enum ColumnIndex{
        JOB_NAME_INDEX (0),
        JOB_TRIGGERED_AT_INDEX (1),
        START_DATE_INDEX (2),
        END_DATE_INDEX (3),
        STATUS_INDEX (4),
        ERROR_LOG_INDEX(5);

        private int colIdx;
        ColumnIndex(int i) {
           colIdx = i;
        }
        public int getColIdx(){
            return colIdx;
        }
    }
    private List<TriggerLogTableData> dataList;

    public TriggerLogTableDataModel() {
        this.dataList = new ArrayList<>();
        this.addTableModelListener(this);
    }

    public TriggerLogTableDataModel(List<TriggerLogTableData> dataList) {
        this();
        if (dataList != null) {
            this.dataList = dataList;
        } else {
            this.dataList = new ArrayList<>();
        }
    }


    @Override
    public int getRowCount() {
        return dataList.size();
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (dataList.isEmpty()) return null;
        Object o = null;
        switch (header[columnIndex]) {
            case JOB_NAME:
                o = dataList.get(rowIndex).getJobName();
                break;
            case JOB_TRIGGERED_AT:
                o = dataList.get(rowIndex).getTriggeredAt();
                break;
            case START_DATE:
                o = dataList.get(rowIndex).getStartDate();
                break;
            case END_DATE:
                o = dataList.get(rowIndex).getEndDate();
                break;
            case STATUS:
                o = dataList.get(rowIndex).getStatus();
                break;
            case ERROR_LOG:
                o = dataList.get(rowIndex).getInfo();
                break;
        }
        return o;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.dataList.add((TriggerLogTableData) aValue);
    }

    public String getColumnName(int col) {
        return header[col];
    }

    public Class<?> getColumnClass(int col) {
        if (dataList.isEmpty()) return "".getClass();
        return getValueAt(0, col).getClass();
    }

    public List<TriggerLogTableData> getDataList() {
        return dataList;
    }

    public void setDataList(List<TriggerLogTableData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }

    /*public static void main(String[] args) {
        System.out.println(ColumnIndex.INFO_INDEX.ordinal());
    }*/
}
