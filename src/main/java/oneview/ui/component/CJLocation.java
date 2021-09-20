package oneview.ui.component;

import oneview.ui.RegisterListener;
import oneview.ui.icon.ImagePanel;
import oneview.util.FileUtil;
import oneview.util.StringUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.LINE_LGRAY_1THK_BORDER;
import static oneview.ui.constants.GuiConstants.TITLE_HEADER_2_FONT;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class CJLocation extends CJPanel implements RegisterListener {
    private CJTextField locationTF;
    private CJButton openButton;
    private CJButton downloadButton;
    private CJPanel parent;
    private JFileChooser fileChooser;
    private int fileChooserOption;
    private boolean download;
    private boolean isDownloading=false;
    private CJPanel processingPanel;
    private Map<String, Object> fileDetails = new HashMap<>();
    public CJLocation(){
        super();
        downloadButton = new CJButton(DOWNLOAD_DEFAULT_ICON, DOWNLOAD_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Download File");
        openButton = new CJButton(FOLDER_OPEN_DEFAULT_ICON, FOLDER_OPEN_HOVERED_ICON, ICON_WIDTH, ICON_HEIGHT, "Folder Open");
    }
    public CJLocation(String title, int width, int fileChooserOption) {
        this();
        locationTF = new CJTextField(width, TEXT_FIELD_HEIGHT);
        this.fileChooserOption = fileChooserOption;

        createUI();
        if (title != null) {
            this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_2_FONT));
        }
        registerListener();
    }

    public CJLocation(String title, int width, boolean download) {
        this();
        this.download = download;
        if(download){
            locationTF = new CJTextField(width, TEXT_FIELD_HEIGHT);
        } else {
            locationTF = new CJTextField(width, TEXT_FIELD_HEIGHT);
        }
        createUI();
        if (title != null) {
            this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_2_FONT));
        }
        registerListener();
    }

    public CJLocation(CJPanel parent, String name, int width, int fileChooserOption) {
        this(name, width, fileChooserOption);
        this.parent = parent;
    }

    private void createUI() {
        // gridBagLayout();
        flowLayout();
    }


    private void flowLayout() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
        this.add(new CJPanel(locationTF,Color.getColor("#ADD8E6")));
        if (download){
            this.processingPanel = newDummyPanelObj();
            this.add(new CJPanel(downloadButton,Color.getColor("#ADD8E6")));
            this.add(processingPanel);
        } else {
            this.add(new CJPanel(openButton,Color.getColor("#ADD8E6")));
        }
    }

    public JTextField getLocationTF() {
        return locationTF;
    }

    public CJButton getOpenButton() {
        return openButton;
    }

    public CJButton getDownloadButton() {
        return downloadButton;
    }

    public void setDownloadButton(CJButton downloadButton) {
        this.downloadButton = downloadButton;
    }

    @Override
    public void registerListener() {
        final CJLocation cjLocation = this;
        if (this.openButton != null) {
            this.openButton.addActionListener(e -> openButtonActionPerformed(cjLocation));
        }
        if (this.downloadButton != null) {
            this.downloadButton.addActionListener(e -> downloadActionPerformed(cjLocation));
        }
    }

    private void downloadActionPerformed(CJLocation cjLocation) {
        String url = cjLocation.getLocationTF().getText();
        if (StringUtil.isNotBlank(url)) {
            cjLocation.setProcessingPanel(DOWNLOADING_IP);
            new Thread(() -> {
                try {
                    File file = FileUtil.download(new URL(url), new File("."));
                    cjLocation.getLocationTF().setText(file.getCanonicalPath());
                    cjLocation.setProcessingPanel(OK_IP);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    private void setProcessingPanel(ImagePanel ip) {
        this.remove(processingPanel);
        this.revalidate();
        this.validate();
        this.processingPanel = ip;
        this.add(processingPanel);
    }

    private void openButtonActionPerformed(CJLocation cjLocation) {
        final String PWD = ".";
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(fileChooserOption);
        File currentDir = new File(StringUtil.isBlank(locationTF.getText()) ? PWD : locationTF.getText());
        fileChooser.setCurrentDirectory(currentDir);

        int option = fileChooser.showOpenDialog(cjLocation);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                locationTF.setText(file.getCanonicalPath());
            } catch (IOException ex) {
                JOptionPane.showConfirmDialog(cjLocation, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
        if(parent != null) {
            parent.fireContextListener();
        }
    }

    public Map<String, Object> getFileDetails() {
        return fileDetails;
    }
}
