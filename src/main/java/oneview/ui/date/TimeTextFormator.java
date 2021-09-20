package oneview.ui.date;

import javax.swing.*;
import java.text.ParseException;

import static oneview.ui.constants.GuiConstants.GLOBAL_ERROR_MSG;

public class TimeTextFormator extends JFormattedTextField.AbstractFormatter {
    public enum TIME_TYPE {
        HOUR(0),
        MIN(1),
        SEC(2);

        private int type;

        TIME_TYPE(int i) {
            this.type = i;
        }

        public int type() {
            return this.type;
        }
    }

    private TIME_TYPE timeType;

    public TimeTextFormator(TIME_TYPE timeType) {
        this.timeType = timeType;
    }

    @Override
    public Object stringToValue(String text) throws ParseException {
        switch (this.timeType) {
            case HOUR:
                return getHrs(text);
            case MIN:
                return getMin(text);
            case SEC:
                return getSecond(text);
        }
        return "";
    }

    @Override
    public String valueToString(Object value) throws ParseException {
        switch (this.timeType) {
            case HOUR:
                return (String) getHrs(value);
            case MIN:
                return (String) getMin(value);
            case SEC:
                return (String) getSecond(value);
        }
        return "";
    }

    private Object getSecond(Object value) throws ParseException {
        if (value == null || value.toString().trim().length() == 0) {
            return "00";
        }
        Integer sec = Integer.valueOf(value.toString());
        if (sec >= 60) {
            GLOBAL_ERROR_MSG.addErrorMsg("Sec cant be greater than 60.");
        }
        if (sec < 10) {
            return "0" + sec;
        } else {
            return "" + sec;
        }
    }


    private Object getMin(Object value) throws ParseException {
        if (value == null || value.toString().trim().length() == 0) {
            return "00";
        }
        Integer min = Integer.valueOf(value.toString());
        if (min >= 60) {
            GLOBAL_ERROR_MSG.addErrorMsg("Min cant be greater than 60.");
        }
        if (min == 60) {
            min = 0;
        }
        if (min < 10) {
            return "0" + min;
        } else {
            return "" + min;
        }
    }

    private Object getHrs(Object value) throws ParseException {
        if (value == null || value.toString().trim().length() == 0) {
            return "00";
        }
        Integer hr = Integer.valueOf(value.toString());
        if (hr > 24) {
            GLOBAL_ERROR_MSG.addErrorMsg("Hour cant be greater than 24.");
        }
//        if (hr == 12) {
//            hr = 0;
//        }
        if (hr < 10) {
            return "0" + hr;
        } else {
            return "" + hr;
        }
    }

}
