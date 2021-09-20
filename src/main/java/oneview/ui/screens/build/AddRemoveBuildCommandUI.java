package oneview.ui.screens.build;

import oneview.bean.PomInfo;
import oneview.command.BuildCommandBuilder;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJButton;
import oneview.ui.component.CJPanel;
import oneview.ui.component.list.CJList;
import oneview.ui.screens.build.list.renderer.BuildCommandListCellRenderer;
import oneview.ui.screens.build.table.BuildTableData;
import oneview.util.FileUtil;
import oneview.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import static oneview.ui.ModelProvider.getCommandListModel;
import static oneview.ui.constants.DimensionConstants.ICON_HEIGHT;
import static oneview.ui.constants.DimensionConstants.ICON_WIDTH;
import static oneview.ui.constants.GuiConstants.LINE_CYAN_1THK_BORDER;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDimentionObj;
import static oneview.util.GuiUtil.newDummyPanelObj;
import static java.awt.Image.SCALE_SMOOTH;

public class AddRemoveBuildCommandUI extends CJPanel implements RegisterListener {

    private final BuildTableData data;
    private CJButton addPomButton;
    private CJButton removePomButton;
    private CJList pomViewList;
    int w = 500;
    int h = 300;

    String buildCommandTemplate = "{MVN} -f {POM_FILE} {CLEAN} {INSTALL} {SKIP_TESTS} {SKIP_IT}";


    public AddRemoveBuildCommandUI(BuildTableData data) {
        super(new BorderLayout(0, 0));
        this.data = data;
        addPomButton = new CJButton(ADD_DEFAULT_ICON, ADD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Add Pom");
        removePomButton = new CJButton(REMOVE_DEFAULT_ICON, REMOVE_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Remove Pom");

        pomViewList = new CJList(getCommandListModel(), new BuildCommandListCellRenderer());
        pomViewList.setFixedCellHeight(25);
        pomViewList.setSelectionBackground(Color.GRAY);
        {
            CJPanel eastPanel = new CJPanel(new GridLayout(5, 1, 0, 0));
            eastPanel.add(addPomButton);
            eastPanel.add(removePomButton);
            eastPanel.add(newDummyPanelObj(1, 1));
            eastPanel.add(newDummyPanelObj(1, 1));
            eastPanel.add(newDummyPanelObj(1, 1));

            this.add(eastPanel, BorderLayout.EAST);

        }
        {
            JScrollPane scrollPane = new JScrollPane(pomViewList);
            scrollPane.setBorder(LINE_CYAN_1THK_BORDER);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
            this.add(scrollPane, BorderLayout.CENTER);
        }

        this.setPreferredSize(newDimentionObj(w, h));

        init();
        registerListener();
    }

    private void init() {
        if (data == null) return;
        DefaultListModel listModel = getCommandListModel();
        data.getCommands().forEach(pomInfo -> listModel.addElement(pomInfo.getCommand()));
        pomViewList.setModel(listModel);
    }

    @Override
    public void registerListener() {
        addPomButton.addActionListener(actionEvent -> {
            AddBuildCommandUI addBuildCommandUI = new AddBuildCommandUI();

            Icon applyBtn = FileUtil.getScaledIcon(APPLY_ICON, ICON_WIDTH / 2, ICON_HEIGHT / 2, SCALE_SMOOTH);
            Icon cancelBtn = FileUtil.getScaledIcon(CANCEL_ICON, ICON_WIDTH / 2, ICON_HEIGHT / 2, SCALE_SMOOTH);
            Object[] options = {applyBtn, cancelBtn};

            final JComponent[] inputs = new JComponent[]{
                    addBuildCommandUI
            };
            int optionBtnClicked = JOptionPane.showOptionDialog(null, inputs,
                    "New Build", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, options, ""
            );
            if (JOptionPane.YES_OPTION == optionBtnClicked) {
                BuildCommandBuilder bcb = new BuildCommandBuilder();
                bcb.mvn()
                        .pom(addBuildCommandUI.getPOM())
                        .clean(addBuildCommandUI.isCleanSelected())
                        .install(addBuildCommandUI.isInstallSelected())
                        .skipTests(addBuildCommandUI.isSkipTestSelected())
                        .skipIT(addBuildCommandUI.isSkipITSelected());
                DefaultListModel<String> model = (DefaultListModel<String>) pomViewList.getModel();
                model.addElement(bcb.getCommand());
                pomViewList.setModel(model);
            }
        });

        removePomButton.addActionListener(actionEvent -> ((DefaultListModel) pomViewList.getModel()).remove(pomViewList.getSelectedIndex()));
    }


    public java.util.List<PomInfo> getBuildCommandList() {
        java.util.List<PomInfo> commmandList = new ArrayList<>();
        DefaultListModel model = (DefaultListModel) pomViewList.getModel();
        for (int i = 0; i < model.size(); i++) {
            String command = (String) model.get(i);
            File pomFile = null;
            try {
                pomFile = FileUtil.getPomFile(command);
            } catch (Exception e) {
                e.printStackTrace();
            }
            String artifactID = StringUtil.getArtifactID(pomFile);
            PomInfo pomInfo = new PomInfo(command, artifactID, pomFile);
            commmandList.add(pomInfo);
        }
        return commmandList;
    }
}