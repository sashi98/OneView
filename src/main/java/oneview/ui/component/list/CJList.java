package oneview.ui.component.list;

import javax.swing.*;

public class CJList extends JList {

    public CJList(DefaultListModel<String> listModel, ListCellRenderer<?> cellRenderer) {
        super(listModel);
        this.setCellRenderer(cellRenderer);
    }

    public CJList() {

    }
}
