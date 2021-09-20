package oneview.ui.screens.deploy.pipeline.table;


import oneview.karaf.PipelineUnit;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PipelineTableDataModel extends AbstractTableModel implements TableModelListener {


    private List<PipelineUnit> dataList;
    private String[] header;

    public PipelineTableDataModel() {
        this.dataList = new ArrayList<>();
    }

    public PipelineTableDataModel(List<PipelineUnit> dataList) {
        this();
        if (dataList != null) {
            this.dataList = dataList;
        } else {
            this.dataList = new ArrayList<>();
        }
        this.header = new String[dataList.size()];
        for (int i=0; i<dataList.size(); i++){
            header[i]=dataList.get(i).getPipelineUnitName();
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
        return dataList.get(rowIndex).getLog();
    }

    public void setHeader(String[] header) {
        this.header = header;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        this.dataList.add((PipelineUnit) aValue);
    }

    public String getColumnName(int col) {
        return header[col];
    }

    public Class<?> getColumnClass(int col) {
        if (dataList.isEmpty()) return "".getClass();
        return getValueAt(0, col).getClass();
    }

    public List<PipelineUnit> getDataList() {
        return dataList;
    }

    public void setDataList(List<PipelineUnit> dataList) {
        this.dataList = dataList;
    }

    @Override
    public void tableChanged(TableModelEvent e) {
    }

    public String getLog(int rowIndex, int columnIndex) {
        if (dataList.isEmpty()) return null;
        String o = null;
        PipelineUnit pu = dataList.get(rowIndex);
        return pu.getLog();
    }
}
