package oneview.ui.screens.tables.server;

import oneview.util.EnvironmentVariables;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EnvVarTableModel extends AbstractTableModel {
    private List<EnvVar> list;

    public EnvVarTableModel() {
        convertToList(EnvironmentVariables.envVars);
    }

    private void convertToList(Map<String, String> envVars) {
        if (list == null){
            list = new ArrayList<>();
        }
        envVars.entrySet().forEach(e -> list.add(new EnvVar(e.getKey(), e.getValue())));
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return list.get(rowIndex);
    }


    class EnvVar {
        private String name;
        private String value;

        public EnvVar(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}
