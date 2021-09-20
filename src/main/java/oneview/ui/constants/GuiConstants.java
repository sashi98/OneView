package oneview.ui.constants;

import oneview.bean.ErrorMsg;

import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.text.SimpleDateFormat;

public class GuiConstants {
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    public static final ErrorMsg GLOBAL_ERROR_MSG = new ErrorMsg();
    public static final String BUSY_SPIN = "BUSY_SPIN";
    public static final String FAILURE = "FAILURE";
    public static final String NOT_OK = "NOT_OK";
    public static final String OK = "OK";
    public static final String STARTED = "STARTED";
    public static final String STOPPED = "STOPPED";
    public static final String RESTART = "RESTART";
    public static final String RESTARTING = "RESTARTING";
    public static final String RESTARTED = "RESTARTED";
    public static final String BUILD_SUCCESS = "BUILD_SUCCESS";
    public static final String BUILD_FAILED = "BUILD_FAILED";
    public static final String BUILD_EDIT = "BUILD_EDIT";
    public static final String POM_RUN_SUCCESS = "POM_RUN_SUCCESS";
    public static final String POM_RUN_FAILURE = "POM_RUN_FAILURE";

    public static final String FEATURE_INSTALLED   = "FEATURE_INSTALL";
    public static final String FEATURE_UNINSTALLED = "FEATURE_UNINSTALL";

    public static final String FEATURE_REINSTALL = "FEATURE_REINSTALL";
    public static final String FEATURE_REINSTALLED = "FEATURE_REINSTALLED";
    public static final String FEATURE_REINSTALLING = "FEATURE_REINSTALLING";

    public static final String RUN_LOG   = "RUN_LOG";
    public static final String VIEW_PIPELINE_STATE   = "VIEW_PIPELINE_STATE";
    public static final String VIEW_PIPELINE_HOVERED_STATE   = "VIEW_PIPELINE_HOVERED_STATE";

    public static final LineBorder LINE_GRAY_1THK_BORDER = new LineBorder(Color.GRAY, 1);
    public static final LineBorder LINE_LGRAY_1THK_BORDER = new LineBorder(Color.LIGHT_GRAY, 1);
    public static final LineBorder LINE_CYAN_1THK_BORDER = new LineBorder(Color.MAGENTA, 1);
    public static final CompoundBorder COMPOUND_TXT_FLD_BORDER = new CompoundBorder(
            new MatteBorder(1, 1, 1, 1, new Color(122, 138, 153)),
            new EmptyBorder(1, 3, 2, 2));


    public static final Font TITLE_HEADER_1_FONT= new Font("Arial", Font.PLAIN, 13);
    public static final Font TITLE_HEADER_2_FONT= new Font("Arial", Font.PLAIN, 12);
    public static final Font TITLE_HEADER_3_FONT= new Font("Arial", Font.BOLD,  13);
    public static final Font NORMAL_TXT_FONT = new Font("Arial", Font.PLAIN, 12);

    public static final int HGAP = 1;
    public static final int VGAP = 1;

}
