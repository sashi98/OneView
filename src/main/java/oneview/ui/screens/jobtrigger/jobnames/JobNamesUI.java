package oneview.ui.screens.jobtrigger.jobnames;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJButton;
import oneview.ui.component.combobox.CJComboBox;
import oneview.ui.component.CJPanel;
import oneview.util.StringUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.List;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDimentionObj;

public class JobNamesUI extends CJPanel implements RegisterListener {
    private CJComboBox jobNamesComboBox;
    private CJButton addJobNameButton;
    private CJButton removeJobNameButton;
    private CJPanel parent;

    public JobNamesUI() {
        super();
        addJobNameButton = new CJButton(ADD_DEFAULT_ICON, ADD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Add Job Name");
        removeJobNameButton = new CJButton(REMOVE_DEFAULT_ICON, REMOVE_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Remove Job Name");
        JobNamesComboBoxModel model = new JobNamesComboBoxModel();
        jobNamesComboBox = new CJComboBox(model);
        jobNamesComboBox.setBorder(LINE_GRAY_1THK_BORDER);
        jobNamesComboBox.setPreferredSize(newDimentionObj(JOB_NAMES_COMBOBOX_WIDTH, JOB_NAMES_COMBOBOX_HEIGHT));
        createUI();
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Cron Job Names", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));
        registerListener();
    }

    public JobNamesUI(CJPanel parent) {
        this();
        this.parent = parent;
    }

    private void createUI() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));

        this.add(jobNamesComboBox);

        this.add(addJobNameButton);

        this.add(removeJobNameButton);
    }

    public void addActionPerformed() {
        String name = JOptionPane.showInputDialog(this, "Add cron job name");
        addItemAndSetSelected(name);
        parent.fireContextListener();
    }

    public void removeActionPerformed() {
        removeItemAndSetSelected(getComboBoxModel().getSelectedItem().toString());
        parent.fireContextListener();
    }

    public List<String> getNonSelectedCronJobNameList() {
        List<String> jobNameList = new ArrayList(getComboBoxModel().getList());
        jobNameList.remove(getComboBoxModel().getSelectedItem());
        return jobNameList;
    }

    public String getSelectedCronJobName() {
        return String.valueOf(getComboBoxModel().getSelectedItem());
    }

    public void addItemAndSetSelected(String name) {
        if (StringUtil.isNotBlank(name)) {
            JobNamesComboBoxModel model = new JobNamesComboBoxModel(getComboBoxModel().getList());
            model.getList().add(name);
            model.setSelectedItem(name);
            jobNamesComboBox.setModel(model);
            jobNamesComboBox.setToolTipText("Selected:" + name);
        }
    }

    public void removeItemAndSetSelected(String selectedItem) {
        if (StringUtil.isNotBlank(selectedItem)) {
            JobNamesComboBoxModel model = new JobNamesComboBoxModel(getComboBoxModel().getList());
            model.getList().remove(selectedItem);
            model.setSelectedItemAt(0);
            jobNamesComboBox.setModel(model);
        }
        if (StringUtil.isNotBlank(String.valueOf(getComboBoxModel().getSelectedItem()))) {
            jobNamesComboBox.setToolTipText("Selected:" + getComboBoxModel().getSelectedItem());
        }
    }
    public JobNamesComboBoxModel getComboBoxModel() {
        return (JobNamesComboBoxModel) jobNamesComboBox.getModel();
    }

    public JComboBox getJobNamesComboBox() {
        return jobNamesComboBox;
    }

    public CJButton getAddJobNameButton() {
        return addJobNameButton;
    }

    public CJButton getRemoveJobNameButton() {
        return removeJobNameButton;
    }


    public void loadNameList(String selectedItem, List<String> nonSelectedItemList) {
        List<String> list;

        if (nonSelectedItemList == null){
            list = new ArrayList<>();
        } else {
            list = new ArrayList<>(nonSelectedItemList);
        }
        if (StringUtil.isNotBlank(selectedItem)) {
            list.add(selectedItem);
            JobNamesComboBoxModel model = new JobNamesComboBoxModel(list);
            model.setSelectedItem(selectedItem);
            jobNamesComboBox.setModel(model);
        }
        if (StringUtil.isNotBlank(String.valueOf(getComboBoxModel().getSelectedItem()))) {
            jobNamesComboBox.setToolTipText("Selected:" + getComboBoxModel().getSelectedItem());
        }

    }

    @Override
    public void registerListener() {
        this.addJobNameButton.addActionListener(actionEvent -> addActionPerformed());
        this.removeJobNameButton.addActionListener(actionEvent -> removeActionPerformed());
        this.jobNamesComboBox.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String name = e.getItem().toString();
                if (StringUtil.isNotBlank(name)) {
                    jobNamesComboBox.setToolTipText("Selected:" + name);
                }
                parent.fireContextListener();
            }
        });
    }

}
