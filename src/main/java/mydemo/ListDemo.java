package mydemo;

import oneview.ui.component.list.CJList;
import oneview.ui.screens.build.list.renderer.BuildCommandListCellRenderer;

import javax.swing.*;
import java.awt.*;

public class ListDemo extends JFrame {
    private final static int WIDTH = 700;
    private final static int HEIGHT = 500;
    private enum A{
        ABC,
        EFG,
        IJK;
    }

    ListDemo() {
        super("Deployment Util GUI");
    }

    public static void main(String[] args) {
        ListDemo mainGUI = new ListDemo();
       // createWindow(mainGUI);
        System.out.println(A.ABC.ordinal());
    }

    private static void createWindow(ListDemo mainGUI) {
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.createUI();
    }

    private void createUI(){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addElement("Us");
        listModel.addElement("In");
        listModel.addElement("Vn");
        listModel.addElement("Ca");
        listModel.addElement("De");
        listModel.addElement("Fr");
        listModel.addElement("Gb");
        listModel.addElement("Jp");
        CJList list = new CJList(listModel, new BuildCommandListCellRenderer());
        this.add(list);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setVisible(true);
    }
}
