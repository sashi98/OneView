package oneview.ui.screens.deploy.table;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class DeployTableDataModel extends AbstractTableModel implements TableModelListener {
    private static final String FEATURE_NAME = "Feature";
    private static final String VERSION = "Version";
    private static final String TIME = "Dep. Time";
    private static final String INSTALL_UNINSTALL = "Install/Uninstall";
    private static final String REINSTALL = "Re-Install";
    private static final String FULL_LOG = "Full Log";
    private static final String VIEW_PIPELINE = "View Pipeline";

    private String[] header = new String[]{FEATURE_NAME,VERSION,TIME,INSTALL_UNINSTALL, REINSTALL,FULL_LOG,VIEW_PIPELINE};

    public enum ColumnIndex {
        FEATURE_NAME_INDEX,VERSION_INDEX, TIME_INDEX,INSTALL_UNINSTALL_INDEX, REINSTALL_INDEX,FULL_LOG_INDEX,VIEW_PIPELINE_INDEX
    }

    private List<DeployTableData> dataList;

    public DeployTableDataModel() {
        this.dataList = new ArrayList<>();
        this.addTableModelListener(this);
    }

    public DeployTableDataModel(List<DeployTableData> dataList) {
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
            case FEATURE_NAME:
                o = dataList.get(rowIndex).getFeatureName();
                break;
            case VERSION:
                o = dataList.get(rowIndex).getVersion();
                break;
            case TIME:
                o = dataList.get(rowIndex).getDeploymentTime();
                break;
            case INSTALL_UNINSTALL:
                o = dataList.get(rowIndex).getInstallOrUninstall();
                break;
            case REINSTALL:
                o = dataList.get(rowIndex).getReinstall();
                break;
            case FULL_LOG:
                o = dataList.get(rowIndex).getFullLog();
                break;
            case VIEW_PIPELINE:
                o = dataList.get(rowIndex).getViewPipeline();
                break;
        }
        return o;
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.dataList.add((DeployTableData) aValue);
    }

    public String getColumnName(int col) {
        return header[col];
    }

    public Class<?> getColumnClass(int col) {
        if (dataList.isEmpty()) return "".getClass();
        return getValueAt(0, col).getClass();
    }

    public List<DeployTableData> getDataList() {
        return dataList;
    }

    public void setDataList(List<DeployTableData> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }

    public String getFullLog(int rowIndex, int columnIndex) {
        if (dataList.isEmpty()) return null;
        String o = null;
        switch (header[columnIndex]) {
            case FULL_LOG:
                o = dataList.get(rowIndex).getFullLog();
                break;
        }
        return o;
    }
}
