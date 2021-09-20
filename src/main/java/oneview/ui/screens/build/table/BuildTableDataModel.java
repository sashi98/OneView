package oneview.ui.screens.build.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class BuildTableDataModel extends AbstractTableModel implements TableModelListener {
    private static final String PROJECT = "Project";
    private static final String BUILD_TIME = "Build Time";
    private static final String RUN = "Run";
    private static final String FAILED_LOG = "Failed Log";
    private static final String EDIT = "Edit";

    private String[] header = new String[]{
            PROJECT, BUILD_TIME, RUN, FAILED_LOG, EDIT};

    public enum ColumnIndex {
        PROJECT_INDEX,
        BUILD_TIME_INDEX,
        RUN_INDEX,
        FAILED_LOG_INDEX,
        EDIT_INDEX
    }

    private List<BuildTableData> dataList;

    public BuildTableDataModel() {
        this.dataList = new ArrayList<>();
        this.addTableModelListener(this);
    }

    public BuildTableDataModel(List<BuildTableData> dataList) {
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
            case PROJECT:
                o = dataList.get(rowIndex).getProject();
                break;
            case BUILD_TIME:
                o = dataList.get(rowIndex).getBuildTime();
                break;
            case RUN:
                o = dataList.get(rowIndex).getBuildStatus();
                break;
            case FAILED_LOG:
                o = dataList.get(rowIndex).getCommands();
                break;
            case EDIT:
                o = dataList.get(rowIndex).getEdit();
                break;
        }
        return o;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.dataList.add((BuildTableData) aValue);
    }

    public String getColumnName(int col) {
        return header[col];
    }

    public Class<?> getColumnClass(int col) {
        if (dataList.isEmpty()) return "".getClass();
        return getValueAt(0, col).getClass();
    }

    public List<BuildTableData> getDataList() {
        return dataList;
    }

    public void setDataList(List<BuildTableData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }


}
