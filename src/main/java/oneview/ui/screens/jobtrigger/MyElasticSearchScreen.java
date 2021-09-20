package oneview.ui.screens.jobtrigger;

import oneview.ui.screens.common.IScreen;
import oneview.ui.screens.jobtrigger.context.ESContext;
import oneview.ui.ContextListenerAdaptor;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJPanel;
import oneview.ui.date.DatePanelUI;
import oneview.ui.screens.jobtrigger.jobnames.JobNamesUI;
import oneview.ui.location.JobSpecificDirsUI;
import oneview.ui.screens.jobtrigger.table.TriggerLogTableUI;
import oneview.util.FileUtil;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static oneview.ui.screens.jobtrigger.context.ESContext.*;
import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class MyElasticSearchScreen extends CJPanel implements IScreen, RegisterListener {

    private ESContext esContext;

    private JobSpecificDirsUI locationBarUI;
    private DatePanelUI datePanelUI;
    private JobNamesUI jobNamesUI;
    private TriggerLogTableUI triggerLogTableUI;

    public MyElasticSearchScreen(){
        this(new ESContext());
    }
    public MyElasticSearchScreen(ESContext esContext) {
        this.esContext = esContext;
        this.setFont(new Font("Arial", Font.PLAIN, 13));

        locationBarUI = new JobSpecificDirsUI(this);
        datePanelUI = new DatePanelUI();
        jobNamesUI = new JobNamesUI(this);
        triggerLogTableUI = new TriggerLogTableUI(MAIN_GUI_WIDTH, MYES_CENTER_PANEL_HEIGHT);

        createUI();

        registerListener();
        loadSettings();

    }

    @Override
    public void createUI() {
        this.setLayout(new BorderLayout(0, 0));
        {
            CJPanel panelNorth = newDummyPanelObj(new GridLayout(2, 1, HGAP, VGAP));
            panelNorth.add(locationBarUI);

            {
                CJPanel row2 = newDummyPanelObj(new GridLayout(1, 2, 0, 0));
                row2.add(datePanelUI);

                {
                    CJPanel panel = newDummyPanelObj(new GridLayout(2, 2, 0, 0));
                    panel.add(jobNamesUI);
                    panel.add(newDummyPanelObj());
                    panel.add(newDummyPanelObj());
                    panel.add(newDummyPanelObj());
                    row2.add(panel);
                }
                panelNorth.add(row2);
            }
            this.add(panelNorth, BorderLayout.NORTH);
        }
        {
            CJPanel panelCenter = newDummyPanelObj(new GridLayout(1, 1, HGAP, VGAP));
            panelCenter.add(triggerLogTableUI);
            this.add(panelCenter, BorderLayout.CENTER);
        }

    }

    public void loadSettings() {
        setOnDemandDirLocationText(esContext.getStringValue(ON_DEMAND_DIR_LOCATION));
        setSuccessDirLocationText(esContext.getStringValue(SUCCESS_DIR_LOCATION));
        setFailureDirLocationText(esContext.getStringValue(FAILURE_DIR_LOCATION));
        setLogDirLocationText(esContext.getStringValue(LOG_DIR_LOCATION));

        jobNamesUI.loadNameList(esContext.getStringValue(CRON_JOB_NAME), esContext.getListValue(CRON_JOB_NAME_LIST));
    }

    private void setLogDirLocationText(String logDirLocation) {
        locationBarUI.getLogLocationUI().getLocationTF().setText(logDirLocation);
    }

    private void setFailureDirLocationText(String failureDirLocation) {
        locationBarUI.getFailureLocationUI().getLocationTF().setText(failureDirLocation);
    }

    private void setSuccessDirLocationText(String successDirLocation) {
        locationBarUI.getSuccessLocationUI().getLocationTF().setText(successDirLocation);
    }

    private void setOnDemandDirLocationText(String onDemandDirLocation) {
        locationBarUI.getOnDemandLocationUI().getLocationTF().setText(onDemandDirLocation);
    }

    private void registerActionListenerToTriggerButton() {
        final MyElasticSearchScreen thisEs = this;
        triggerLogTableUI.getTriggerButton().addActionListener(actionEvent -> {
            System.out.println(esContext);
            fireContextListener();

            esContext.validate();
            if (!GLOBAL_ERROR_MSG.isEmpty()) {
                JOptionPane.showMessageDialog(thisEs, GLOBAL_ERROR_MSG, "Dialog", JOptionPane.ERROR_MESSAGE);
                GLOBAL_ERROR_MSG.reset();
                return;
            }
            new Thread(() -> {
                try {
                    FileUtil.onDemandTriggering(esContext);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                triggerLogTableUI.getTriggerLogTable().updateTable(esContext);
                datePanelUI.getDatePanels()[0].setDateTime();
            }).start();
        });
    }
    
    public void populateESContext() {
        esContext.set(ESContext.TITLE, this.getName());
        esContext.set(ON_DEMAND_DIR_LOCATION, getOnDemandDirLocation());
        esContext.set(SUCCESS_DIR_LOCATION, getSuccessDirLocation());
        esContext.set(FAILURE_DIR_LOCATION, getFailureDirLocation());
        esContext.set(LOG_DIR_LOCATION, getLogDirLocation());

        esContext.set(CRON_JOB_NAME, getSelectedCronJobName());
        esContext.set(CRON_JOB_NAME_LIST, getCronJobNameList());

        esContext.set(START_DATE_TIME, getStartDateTime());
        esContext.set(END_DATE_TIME, getEndDateTime());
        String sDate = getStartDateTime().replaceAll("\\p{Punct}", "")
                .replaceAll("\\p{Blank}", "_");
        String triggerFileName = esContext.getStringValue(CRON_JOB_NAME) + "-" + sDate + ".xml";
        esContext.set(TRIGGER_FILE_NAME, triggerFileName);

    }

    private List<String> getCronJobNameList() {
        return jobNamesUI.getNonSelectedCronJobNameList();
    }

    private String getSelectedCronJobName() {
        return jobNamesUI.getSelectedCronJobName();
    }

    private String getEndDateTime() {
        return datePanelUI.getDatePanels()[1].getDateTime();
    }

    private String getStartDateTime() {
        return datePanelUI.getDatePanels()[0].getDateTime();
    }

    private String getLogDirLocation() {
        return locationBarUI.getLogLocationUI().getLocationTF().getText();
    }

    private String getFailureDirLocation() {
        return locationBarUI.getFailureLocationUI().getLocationTF().getText();
    }

    private String getSuccessDirLocation() {
        return locationBarUI.getSuccessLocationUI().getLocationTF().getText();
    }

    private String getOnDemandDirLocation() {
        return locationBarUI.getOnDemandLocationUI().getLocationTF().getText();
    }


    @Override
    public void registerListener() {
        registerActionListenerToTriggerButton();
        registerContextListener();
    }

    private void registerContextListener() {
        this.addContextListener(new ContextListenerAdaptor(){
            public void contextUpdate(){
                populateESContext();
            }
        });
    }

    public JobSpecificDirsUI getLocationBarUI() {
        return locationBarUI;
    }

    public DatePanelUI getDatePanelUI() {
        return datePanelUI;
    }

    public JobNamesUI getJobNamesUI() {
        return jobNamesUI;
    }

    public TriggerLogTableUI getTriggerLogTableUI() {
        return triggerLogTableUI;
    }

    public void updateName(String name){
        esContext.set(TITLE, name);
    }

    public ESContext getEsContext() {
        return esContext;
    }
}
