package oneview.ui.screens.deploy;

import oneview.archive.ArchiveExplorer;
import oneview.archive.ArchivedExplorerHelper;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJCheckBox;
import oneview.ui.component.CJLabel;
import oneview.ui.component.CJLocation;
import oneview.ui.component.CJPanel;
import org.apache.commons.io.FilenameUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Map;

import static oneview.ui.constants.DimensionConstants.ICON_HEIGHT;
import static oneview.ui.constants.DimensionConstants.ICON_WIDTH;
import static oneview.ui.constants.IconConstants.OFF_ICON;
import static oneview.ui.constants.IconConstants.ON_ICON;

public class ArtifactUI extends CJPanel implements RegisterListener {
    private CJPanel artifactPanel;
    private final CJCheckBox needToDownload;
    private final CJLocation artifactLocation;
    private final CJLocation artifactDownloadLocation;
    private final CJPanel downloadArtifactCheckPanel;
    private final CJLabel artifactPathLabel;
    public ArtifactUI() {
        super();
        this.artifactDownloadLocation = new CJLocation(null, 500, true);
        this.artifactLocation = new CJLocation(null, 550, JFileChooser.FILES_AND_DIRECTORIES);
        this.needToDownload = new CJCheckBox(ON_ICON, OFF_ICON, ICON_WIDTH, ICON_HEIGHT, "Check to download artifact.");
        this.downloadArtifactCheckPanel = new CJPanel(new CJLabel("Download Artifact"), needToDownload);
        this.artifactPathLabel = new CJLabel("Artifact Path");
        createUI();
    }

    private void createUI() {
        this.setLayout(new GridLayout(2,1,0,0));
        this.add(downloadArtifactCheckPanel);
        addArtifactPanel();
        registerListener();
    }

    private void addArtifactPanel() {
        if (needToDownload.isSelected()){
            this.artifactPathLabel.setText("Artifact URL");
            this.artifactPanel = new CJPanel(artifactPathLabel, artifactDownloadLocation);
        } else {
            this.artifactPathLabel.setText("Artifact Path");
            this.artifactPanel = new CJPanel(artifactPathLabel, artifactLocation);
        }
        this.add(artifactPanel);
    }

    @Override
    public void registerListener() {
        ArtifactUI artifactUI = this;
        needToDownload.addActionListener(e -> {
            artifactUI.remove(artifactUI.artifactPanel);
            artifactUI.revalidate();
            artifactUI.validate();
            artifactUI.createUI();
        });
    }

    public File getArtifactPath(){
        File f;
        if (needToDownload.isSelected()){
            f = new File(artifactDownloadLocation.getLocationTF().getText());
        } else {
            f = new File(artifactLocation.getLocationTF().getText());
        }
        return f;
    }

    public CJLocation getArtifactLocation() {
        return artifactLocation;
    }

    public CJLocation getArtifactDownloadLocation() {
        return artifactDownloadLocation;
    }

    public Map<String, Object> getFileDetails(String filePath) {
        File f = new File(filePath);
        ArchiveExplorer archiveExlorer = ArchivedExplorerHelper.getArchiveExplorer(FilenameUtils.getExtension(f.getName()), f);
        return archiveExlorer.getArchiveFileDetails();
    }
}
