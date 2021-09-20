package mydemo;

import oneview.ui.screens.common.list.renderer.ScreenListCellRenderer;
import oneview.ui.screens.jobtrigger.MyElasticSearchScreen;
import oneview.ui.component.CJPanel;
import oneview.ui.component.list.CJList;
import oneview.ui.component.tabbedpane.CJTabbedPane;
import oneview.ui.component.tabbedpane.CJTabbedPaneUI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

import static oneview.ui.ModelProvider.getScreenListModel;
import static oneview.ui.constants.DimensionConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class TabbedPaneDemo extends JFrame  {
    public final static int WIDTH_T = 700;
    private final static int HEIGHT = 500;
    TabbedPaneDemo() {
        super("Deployment Util GUI");
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        TabbedPaneDemo mainGUI = new TabbedPaneDemo();
        createWindow(mainGUI);
    }

    private static void createWindow(TabbedPaneDemo mainGUI) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.createUI();
        mainGUI.packNShow();
    }

    private void packNShow() {
        setSize(new Dimension(WIDTH_T, HEIGHT));
        setLocation(WINDOW_X, 15*WINDOW_Y);
        setVisible(true);
    }

    private void createUI() {
        CJTabbedPane tabbedPane = new CJTabbedPane(new CJTabbedPaneUI(CJTabbedPaneUI.ICON_ALIGN.RIGHT));
        String tbTitle = "Tab";
        String dash="";
        for (int i = 0; i<=1; i++){
            dash+="-";
            String thisTabTitle = tbTitle+dash+(i);
            tabbedPane.add(thisTabTitle, new MyElasticSearchScreen());
        }
        CJList list = new CJList(getScreenListModel(), new ScreenListCellRenderer());
        this.add(new CJPanel(list, Color.GRAY), BorderLayout.WEST);
        this.add(tabbedPane, BorderLayout.CENTER);

    }



    private Color randomColor() {
        Random rand = new Random();
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);
        return new Color(r,g,b);
    }


}
