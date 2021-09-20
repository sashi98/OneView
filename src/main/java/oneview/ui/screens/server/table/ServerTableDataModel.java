package oneview.ui.screens.server.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ServerTableDataModel extends AbstractTableModel implements TableModelListener {
    private static final String SERVER_NAME = "Server";
    private static final String START_TIME = "Start Time";
    private static final String STOP_TIME = "Stop Time";
    private static final String START_STOP = "Start/Stop";
    private static final String RESTART = "Restart";

    private String[] header = new String[]{SERVER_NAME,
            START_TIME, STOP_TIME, START_STOP, RESTART};

    public enum ColumnIndex {
        SERVER_NAME_INDEX,
        START_TIME_INDEX,
        STOP_TIME_INDEX,
        START_STOP_INDEX,
        RESTART_INDEX;
    }

    private List<ServerTableData> dataList;

    public ServerTableDataModel() {
        this.dataList = new ArrayList<>();
        this.addTableModelListener(this);
    }

    public ServerTableDataModel(List<ServerTableData> dataList) {
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
            case SERVER_NAME:
                o = dataList.get(rowIndex).getServerName();
                break;
            case START_TIME:
                o = dataList.get(rowIndex).getStartTime();
                break;
            case STOP_TIME:
                o = dataList.get(rowIndex).getStopTime();
                break;
            case START_STOP:
                o = dataList.get(rowIndex).getStartStop();
                break;
            case RESTART:
                o = dataList.get(rowIndex).getRestart();
                break;
        }
        return o;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.dataList.add((ServerTableData) aValue);
    }

    public String getColumnName(int col) {
        return header[col];
    }

    public Class<?> getColumnClass(int col) {
        if (dataList.isEmpty()) return "".getClass();
        return getValueAt(0, col).getClass();
    }

    public List<ServerTableData> getDataList() {
        return dataList;
    }

    public void setDataList(List<ServerTableData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }


}
