package oneview.ui.constants;

import oneview.ui.icon.ImagePanel;
import oneview.util.FileUtil;

import javax.swing.*;

import static oneview.ui.constants.DimensionConstants.ICON_HEIGHT;
import static oneview.ui.constants.DimensionConstants.ICON_WIDTH;
import static java.awt.Image.SCALE_SMOOTH;

public class IconConstants {
    public static final boolean FALSE = false;
    public static String SLASH = "/";
    public static String ICON_DIR= SLASH+"icons"+ SLASH +"in_use";
    public static String ICON_GIF_DIR= ICON_DIR+ SLASH +"gifs";

    public static final ImagePanel BUSY_SPIN_IP = new ImagePanel(ICON_GIF_DIR+ SLASH +"busy_spin.gif");

    public static final ImagePanel RESTART_IP = new ImagePanel(ICON_DIR+ SLASH +"restart.png", "Click to restart server.");
    public static final ImagePanel RESTARTING_IP = new ImagePanel(ICON_GIF_DIR+ SLASH +"restarting.gif", "Server Restarting.");
    public static final ImagePanel RESTARTED_IP = new ImagePanel(ICON_DIR+ SLASH +"restarted.png", "Server Restarted. Click to restart again.");
    
    public static final ImagePanel STARTED_IP = new ImagePanel(ICON_DIR+ SLASH +"started1.png", "Server Running. Click to stop.");
    public static final ImagePanel STOPPED_IP = new ImagePanel(ICON_DIR+ SLASH +"stopped1.png", "Server Stopped. Click to start.");

    public static final ImagePanel INFO_IP = new ImagePanel(ICON_DIR+ SLASH +"info.png", "Click here");
    public static final ImagePanel OK_IP = new ImagePanel(ICON_DIR+ SLASH +"ok.png");
    public static final ImagePanel NOT_OK_IP = new ImagePanel(ICON_DIR+ SLASH +"not_ok.png"/*, ICON_WIDTH, ICON_HEIGHT, FALSE, Color.WHITE*/);


    public static final ImageIcon TRIGGERED_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"trigger.png");
    public static final ImageIcon TRIGGERED_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"trigger1.png");

    public static final ImageIcon FOLDER_OPEN_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"open.png");
    public static final ImageIcon FOLDER_OPEN_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"open1.png");

    public static final ImageIcon DOWNLOAD_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"download.png");
    public static final ImageIcon DOWNLOAD_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"download1.png");

    public static final ImageIcon ADD_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"add.png");
    public static final ImageIcon ADD_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"add1.png");

    public static final ImageIcon REMOVE_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"remove.png");
    public static final ImageIcon REMOVE_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"remove1.png");

    public static final ImageIcon TRASH_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"trash.png");
    public static final ImageIcon TRASH_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"trash1.png");

    public static final ImageIcon DESC_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"desc.png");
    public static final ImageIcon ASC_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"asc.png");

    public static final ImageIcon ARROW_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"arrow.png");
    public static final ImageIcon ARROW_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"arrow1.png");

    public static final ImageIcon CALLENDER_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"calendar.png");
    public static final ImageIcon CALLENDER_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"calendar1.png");

    public static final ImageIcon CALLENDER_DEFAULT_SICON = FileUtil.getScaledIcon(CALLENDER_DEFAULT_ICON, ICON_WIDTH, ICON_HEIGHT, SCALE_SMOOTH);
    public static final ImageIcon CALLENDER_HOVERED_SICON = FileUtil.getScaledIcon(CALLENDER_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, SCALE_SMOOTH);

    public static final ImageIcon ON_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"on.png");
    public static final ImageIcon OFF_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"off.png");

    public static final ImageIcon ADD_TAB_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"add_tab.png");
    public static final ImageIcon ADD_TAB_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"add_tab1.png");

    public static final ImageIcon CLOSE_TAB_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"close_tab.png");
    public static final ImageIcon CLOSE_TAB_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"close_tab1.png");

    public static final ImageIcon APPLY_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"apply.png");
    public static final ImageIcon CANCEL_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"cancel.png");

//    public static final ImageIcon VIEW_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"view.png");
//    public static final ImageIcon VIEW_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"view1.png");

    //public static final ImageIcon EDIT_DEFAULT_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"edit.png");
    //public static final ImageIcon EDIT_HOVERED_ICON = FileUtil.getIcon(ICON_DIR+ SLASH +"edit1.png");


    public static final ImagePanel BUILD_SUCCESS_IP = new ImagePanel(ICON_DIR+ SLASH +"started1.png", "Build is successfull");
    public static final ImagePanel BUILD_FAILED_IP = new ImagePanel(ICON_DIR+ SLASH +"stopped1.png", "Build failure");
    public static final ImagePanel BUILD_FAILED_LOG_IP = new ImagePanel(ICON_DIR+ SLASH +"info.png", "Click here");
    public static final ImagePanel BUILD_EDIT_IP = new ImagePanel(ICON_DIR+ SLASH +"settings.png", "Build Edit");

    public static final ImagePanel FEATURE_INSTALLED_IP = new ImagePanel(ICON_DIR+ SLASH +"installed.png", "Click to restart server.");
    public static final ImagePanel FEATURE_UNINSTALLED_IP = new ImagePanel(ICON_DIR+ SLASH +"uninstalled.png", "Click to restart server.");

    public static final ImagePanel FEATURE_REINSTALL_IP = new ImagePanel(ICON_DIR+ SLASH +"reinstall.png", "Click to install.");
    public static final ImagePanel FEATURE_REINSTALLED_IP = new ImagePanel(ICON_DIR+ SLASH +"reinstalled.png", "Feature Reinstalled. Click to reinstall again.");
    public static final ImagePanel FEATURE_REINSTALLING_IP = new ImagePanel(ICON_GIF_DIR+ SLASH +"reinstalling.gif", "Feature Reinstalling.");

    public static final ImagePanel RUN_LOG_IP = new ImagePanel(ICON_DIR+ SLASH +"info.png", "Click here");
    public static final ImagePanel VIEW_PIPELINE_STATE_IP = new ImagePanel(ICON_DIR+ SLASH +"close_eye.png", "Click to install.");
    public static final ImagePanel VIEW_PIPELINE_STATE_HOVERED_IP = new ImagePanel(ICON_DIR+ SLASH +"open_eye.png", "Click to install.");

    public static final ImagePanel PIPELINE_LOG_IP = new ImagePanel(ICON_DIR+ SLASH +"info.png", "Click here");

    public static final ImagePanel DOWNLOADING_IP = new ImagePanel(ICON_GIF_DIR+ SLASH +"downloading.gif", "Downloading..");
    public static final ImagePanel DOWNLOADED_IP = new ImagePanel(ICON_DIR+ SLASH +"downloaded.png", "Downloaded");

}
