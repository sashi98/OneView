package oneview;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJPanel;
import oneview.ui.component.list.CJList;
import oneview.ui.component.tabbedpane.CJTabbedPane;
import oneview.ui.component.tabbedpane.CJTabbedPaneUI;
import oneview.ui.screens.build.BuildScreen;
import oneview.ui.screens.build.context.BuildContext;
import oneview.ui.screens.common.list.renderer.ScreenListCellRenderer;
import oneview.ui.screens.deploy.DeploymentScreen;
import oneview.ui.screens.deploy.context.DeploymentContext;
import oneview.ui.screens.jobtrigger.MyElasticSearchScreen;
import oneview.ui.screens.jobtrigger.context.ESContext;
import oneview.ui.screens.server.ServersScreen;
import oneview.ui.screens.server.context.ServerContext;
import oneview.util.FileUtil;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static oneview.ui.ModelProvider.getScreenListModel;
import static oneview.ui.constants.DimensionConstants.*;
import static oneview.util.ThreadUtil.sleep;

public class OneViewMainGUI extends JFrame implements RegisterListener {

    private List<ESContext> esContextList;
    private CJTabbedPane myESTabbedPane;
    private ServersScreen serversScreen;
    private BuildScreen buildScreen;
    private DeploymentScreen deploymentScreen;
    private CJList screensList;
    private CJPanel mainPanel;
    private CJPanel screenPanel;

    OneViewMainGUI() {
        super("Deployment Util GUI");
        this.esContextList = new ArrayList<>();
        this.setLayout(new GridLayout(1,1));
    }

    public static void main(String[] args) {
        OneViewMainGUI mainGUI = new OneViewMainGUI();
        mainGUI.createWindow();
        mainGUI.loadMyESTabs();
        mainGUI.packAndShow();
        mainGUI.registerListener();
    }

    private void packAndShow() {
        this.setPreferredSize(new Dimension(MAIN_GUI_WIDTH, MAIN_GUI_HEIGHT));
        this.setResizable(false);
        this.setVisible(true);
        this.setLocation(WINDOW_X, WINDOW_Y);
        this.pack();
    }

    private void createWindow() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.createUI();
    }

    private void createUI() {
        this.mainPanel = new CJPanel(new LineBorder(Color.black));
        this.mainPanel.setLayout(new BorderLayout(0,0));
        this.myESTabbedPane = new CJTabbedPane(new CJTabbedPaneUI(CJTabbedPaneUI.ICON_ALIGN.RIGHT));
        this.screensList = new CJList(getScreenListModel(), new ScreenListCellRenderer());
        screenPanel = new CJPanel(new GridLayout(1,1,0,0));
        {
            CJPanel screensListPanel = new CJPanel(screensList, true, Color.RED, Color.GREEN);
            screensListPanel.setBorder(new BevelBorder(BevelBorder.RAISED, Color.WHITE, Color.GRAY));
            this.mainPanel.add(screensListPanel, BorderLayout.WEST);
            {//Default View
                this.screensList.setSelectedIndex(0);
                screenPanel.add(myESTabbedPane);
                mainPanel.add(screenPanel, BorderLayout.CENTER);
            }
        }
        this.serversScreen = new ServersScreen(new ServerContext());
        this.buildScreen = new BuildScreen(new BuildContext());
        this.deploymentScreen = new DeploymentScreen(new DeploymentContext());
        this.add(this.mainPanel);
    }


    private void loadMyESTabs() {
        try {
            esContextList = FileUtil.loadSettings("./master_data.cache");
        } catch (Exception ex) {
            esContextList = new ArrayList<>();
            ex.printStackTrace();
        }
        sleep(5000);
        esContextList.forEach(this::loadMyESTab);
        if (esContextList.isEmpty()) {
            loadMyESTab(null);
        }

    }

    private void loadMyESTab(ESContext esContext) {
        if (esContext == null) {
            esContext = new ESContext();
            esContext.set(ESContext.TITLE, "MyES");
            esContextList.add(esContext);
        }
        MyElasticSearchScreen myES = new MyElasticSearchScreen(esContext);
        myESTabbedPane.add(esContext.getStringValue(ESContext.TITLE), myES);
    }

    private void registerWindowListener() {
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    updateContext();
                    FileUtil.saveSettings("./master_data.cache", esContextList);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    private void updateContext() {
        esContextList = new ArrayList<>();
        for (int t = 0; t < myESTabbedPane.getTabCount() - 1; t++) {
            esContextList.add(((MyElasticSearchScreen) myESTabbedPane.getComponentAt(t)).getEsContext());
        }
    }

    @Override
    public void registerListener() {
        registerWindowListener();
        registerListSelectionListener();

    }

    private void registerListSelectionListener() {

        screensList.addListSelectionListener(e -> {
            this.screenPanel.removeAll();
            if (screensList.getSelectedValue().equals(getScreenListModel().elementAt(0))){
                //this.screenPanel.remove(serversScreen);
                screenPanel.add(myESTabbedPane);
            }
            if (screensList.getSelectedValue().equals(getScreenListModel().elementAt(1))){
                this.screenPanel.add(serversScreen);
            }
            if (screensList.getSelectedValue().equals(getScreenListModel().elementAt(2))){
                this.screenPanel.add(buildScreen);
            }

            if (screensList.getSelectedValue().equals(getScreenListModel().elementAt(3))){

                this.screenPanel.add(deploymentScreen);
            }
            this.screenPanel.revalidate();
            this.screenPanel.validate();
            this.screenPanel.repaint();
            setVisible(true);
        });
    }
}
