package oneview.ui.screens.jobtrigger.jobnames;

import oneview.util.StringUtil;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

public class JobNamesComboBoxModel implements ComboBoxModel<String> {
    private String selectedItem;
    //private int selectedIndex = -1;
    private List<String> list;

    public JobNamesComboBoxModel() {
        list = new ArrayList<>();
    }

    public JobNamesComboBoxModel(List<String> list) {
        this.list = list;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        selectedItem = StringUtil.isBlank(String.valueOf(anItem)) ? "" : String.valueOf(anItem);
    }

    @Override
    public Object getSelectedItem() {
        return StringUtil.isBlank(String.valueOf(selectedItem)) ? "" : selectedItem;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public String getElementAt(int index) {
        return list.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }

//    public int addJobName(String name) {
//        list.add(name);
//        setSelectedItem(name);
//        return getSelectedIndex(name);
//    }

//    public int getSelectedIndex(String selectedItem) {
//        selectedItem = String.valueOf(selectedItem).equals("null") ? "" : selectedItem;
//        for (int i = 0; i < list.size(); i++) {
//            String ele = list.get(i);
//            if (selectedItem.equals(ele)) {
//                selectedIndex = i;
//                break;
//            }
//        }
//        return selectedIndex;
//    }

    public List<String> getList() {
        return list;
    }

    public void setSelectedItemAt(int i) {
        if (list.isEmpty()){
            setSelectedItem("");
        } else {
            setSelectedItem(list.get(i));
        }

    }
}
