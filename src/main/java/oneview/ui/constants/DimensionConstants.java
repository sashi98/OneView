package oneview.ui.constants;

import oneview.ui.component.CJPopupTextView;

import java.awt.*;

import static oneview.util.GuiUtil.percent;

public class DimensionConstants {
    public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    public static int S_WIDTH = (int) screenSize.getWidth();
    public static int S_HEIGHT = (int) screenSize.getHeight();

    public static int MAIN_GUI_WIDTH = percent(S_WIDTH, 90);
    public static int MAIN_GUI_HEIGHT = percent(S_HEIGHT, 80);

    public static final int MYES_UPPER_PANEL_HEIGHT = percent(MAIN_GUI_HEIGHT, 40);
    public static final int MYES_CENTER_PANEL_HEIGHT = percent(MAIN_GUI_HEIGHT, 60);
    public static final int SERVER_CENTER_PANEL_HEIGHT = percent(MAIN_GUI_HEIGHT, 60);

    public static int WINDOW_X = (S_WIDTH - MAIN_GUI_WIDTH) / 2;
    public static int WINDOW_Y = (S_HEIGHT - MAIN_GUI_HEIGHT) / 4;

    public static final int ICON_WIDTH = 30;
    public static final int ICON_HEIGHT = 30;
    public static int LOCATION_UI_WIDTH = percent(MAIN_GUI_WIDTH, 41);
    public static final int LOCATION_UI_HEIGHT = 50;

    public static int LOCATION_UI_TXT_FIELD_WIDTH = percent(LOCATION_UI_WIDTH, 90);

    public static final int TEXT_FIELD_HEIGHT = ICON_HEIGHT - 10;

    public static int DATE_PANEL_WIDTH = percent(MAIN_GUI_WIDTH, 50);
    public static final int DATE_PANEL_HEIGHT = 30;

    public static int JOB_NAMES_UI_WIDTH = percent(MAIN_GUI_WIDTH, 25);
    public static final int JOB_NAMES_UI_HEIGHT = 100;
    public static int JOB_NAMES_COMBOBOX_WIDTH = percent(JOB_NAMES_UI_WIDTH, 50);
    public static final int JOB_NAMES_COMBOBOX_HEIGHT = TEXT_FIELD_HEIGHT;

    public static final int TXT_BTN_H_PERCENT = 20;
    public static final int UPPER_PANEL_H_PERCENT = 20;
    public static final int PER_DATE_PANEL_ROW_H_PERCENT = TXT_BTN_H_PERCENT;

    public static final int TABLE_ROW_HEIGHT = 25;
    public static final int POPUP_TEXT_VIEW_WIDTH = 800;
    public static final int POPUP_TEXT_VIEW_HEIGHT = 300;
    public static final int POPUP_TEXT_VIEW_X_DELTA = 150;
    public static final int POPUP_TEXT_VIEW_X_SHIFT = POPUP_TEXT_VIEW_WIDTH-POPUP_TEXT_VIEW_X_DELTA;

}
