package oneview.ui;

import javax.swing.*;

public class ModelProvider {

    public static DefaultListModel<String> getScreenListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("My Elastic Search");
        listModel.addElement("Servers");
        listModel.addElement("Build");
        listModel.addElement("Deploy");

        return listModel;
    }

    public static DefaultListModel<String> getCommandListModel() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        return listModel;
    }
}
