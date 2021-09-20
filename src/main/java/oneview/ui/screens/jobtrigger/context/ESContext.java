package oneview.ui.screens.jobtrigger.context;

import oneview.ui.screens.common.context.OneViewContext;
import oneview.util.StringUtil;

import java.text.ParseException;
import java.util.*;

import static oneview.ui.constants.GuiConstants.GLOBAL_ERROR_MSG;
import static oneview.ui.constants.GuiConstants.SIMPLE_DATE_FORMAT;

public class ESContext implements OneViewContext {
    public static final String ON_DEMAND_DIR_LOCATION = "ON_DEMAND_DIR_LOCATION";
    public static final String SUCCESS_DIR_LOCATION = "SUCCESS_DIR_LOCATION";
    public static final String FAILURE_DIR_LOCATION = "FAILURE_DIR_LOCATION";
    public static final String LOG_DIR_LOCATION = "LOG_DIR_LOCATION";
    public static final String START_DATE_TIME = "START_DATE_TIME";
    public static final String END_DATE_TIME = "END_DATE_TIME";
    public static final String CRON_JOB_NAME = "CRON_JOB_NAME";
    public static final String CRON_JOB_NAME_LIST = "CRON_JOB_NAME_LIST";
    public static final String TRIGGER_FILE_NAME = "TRIGGER_FILE_NAME";
    public static final String TITLE = "TITLE";

    private HashMap<String, Object> hm;

    public ESContext(){
        hm = new HashMap<>();
    }

    public void set(String key, Object val){
        hm.put(key,val);
    }

    public void print() {
        System.out.println(this);
    }

    public void validate() {
        if (StringUtil.isBlank(String.valueOf(getStringValue(CRON_JOB_NAME)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("Cron Job is not set");
        }
        if (StringUtil.isBlank(String.valueOf(getStringValue(START_DATE_TIME)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("Start Date Time is not set");
        }
        if (StringUtil.isBlank(String.valueOf(getStringValue(END_DATE_TIME)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("End Date Time is not set.");
        }
        if (StringUtil.isBlank(String.valueOf(getStringValue(ON_DEMAND_DIR_LOCATION)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("On Demand directory location is not set.");
        }
        if (StringUtil.isBlank(String.valueOf(getStringValue(SUCCESS_DIR_LOCATION)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("Success directory location is not set.");
        }
        if (StringUtil.isBlank(String.valueOf(getStringValue(FAILURE_DIR_LOCATION)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("Failure directory location is not set.");
        }
        if (StringUtil.isBlank(String.valueOf(getStringValue(LOG_DIR_LOCATION)))) {
            GLOBAL_ERROR_MSG.addErrorMsg("Log directory location is not set.");
        }
        if (StringUtil.isNotBlank(String.valueOf(getStringValue(START_DATE_TIME))) && StringUtil.isNotBlank(getStringValue(END_DATE_TIME))) {
            try {
                Date sd = SIMPLE_DATE_FORMAT.parse(String.valueOf(getStringValue(START_DATE_TIME)));
                Date ed = SIMPLE_DATE_FORMAT.parse(String.valueOf(getStringValue(END_DATE_TIME)));
                System.out.println(sd);
                System.out.println(ed);

                if (sd.after(ed)) {
                    GLOBAL_ERROR_MSG.addErrorMsg("Start Date cannot be after end date.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringValue(String key) {
        String val = String.valueOf(hm.get(key));
        return (val == null || val.equals("null") || val.trim().equals(""))?"":val;
    }

    public List getListValue(String key){
        return (List) hm.get(key);
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public void remove(String key) {
        hm.remove(key);
    }

    @Override
    public String toString() {
        return "ESContext{" +
                "hm=" + hm +
                '}';
    }
}
