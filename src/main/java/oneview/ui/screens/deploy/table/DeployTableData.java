package oneview.ui.screens.deploy.table;

import oneview.karaf.PipelineUnit;
import oneview.ui.screens.common.table.TableData;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static oneview.archive.ArchieveConstants.*;
import static oneview.archive.ArchiveExplorer.*;
import static oneview.ui.constants.GuiConstants.*;


public class DeployTableData implements TableData {
    private Date deploymentTime;
    private String installOrUninstall;
    private String reinstall;
    private String fullLog;
    private String viewPipeline;
    private File artifactPath;
    private List<PipelineUnit> pipeline;
    private Map<String, Object> artifactDetails;


    public DeployTableData() {
        installOrUninstall = BUSY_SPIN;
        reinstall = FEATURE_REINSTALL;
        viewPipeline = VIEW_PIPELINE_STATE;
    }

    public Date getDeploymentTime() {
        return deploymentTime;
    }

    public void setDeploymentTime(Date deploymentTime) {
        this.deploymentTime = deploymentTime;
    }

    public String getInstallOrUninstall() {
        return installOrUninstall;
    }

    public void setInstallOrUninstall(String installOrUninstall) {
        this.installOrUninstall = installOrUninstall;
    }

    public String getReinstall() {
        return reinstall;
    }

    public void setReinstall(String reinstall) {
        this.reinstall = reinstall;
    }

    public String getFullLog() {
        return fullLog;
    }

    public void setFullLog(String fullLog) {
        this.fullLog = fullLog;
    }

    public String getViewPipeline() {
        return viewPipeline;
    }

    public void setViewPipeline(String viewPipeline) {
        this.viewPipeline = viewPipeline;
    }

    public File getArtifactPath() {
        return artifactPath;
    }

    public void setArtifactPath(File artifactPath) {
        this.artifactPath = artifactPath;
    }

    public List<PipelineUnit> getPipeline() {
        return pipeline;
    }

    public void setPipeline(List<PipelineUnit> pipeline) {
        this.pipeline = pipeline;
    }

    public Map<String, Object> getArtifactDetails() {
        return artifactDetails;
    }

    public void setArtifactDetails(Map<String, Object> artifactDetails) {
        this.artifactDetails = artifactDetails;
    }

    @Override
    protected DeployTableData clone() {
        DeployTableData cloned = new DeployTableData();
        cloned.setArtifactPath(getArtifactPath());
        cloned.setReinstall(getReinstall());
        cloned.setInstallOrUninstall(getInstallOrUninstall());
        cloned.setDeploymentTime(getDeploymentTime());
        cloned.setFullLog(getFullLog());
        cloned.setViewPipeline(getViewPipeline());
        cloned.setArtifactDetails(new HashMap<>(getArtifactDetails()));
        return cloned;
    }

    public String getFeatureName() {
        return (String) getArtifactDetails().get(FEATURE_NAME_KEY);
    }

    public String getVersion() {
        return (String) getArtifactDetails().get(VERSION_KEY);
    }

    public String getRepoName() {
        return (String) getArtifactDetails().get(REPO_NAME_KEY);
    }

    public String getKarName() {
        return (String) getArtifactDetails().get(KAR_NAME_KEY);
    }

    public String getBlueprint() {
        return (String) getArtifactDetails().get(BLUEPRRINT_KEY);
    }

    public String getLastRunJson() {
        return (String) getArtifactDetails().get(LAST_RUN_JSON_KEY);
    }

    public String getInstalledKarPath() {
        return (String) getArtifactDetails().get(KAR_INSTALLED_PATH_KEY);
    }
}
