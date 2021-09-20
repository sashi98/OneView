package oneview.ui.screens.build.table;

import oneview.ui.screens.common.table.TableData;
import oneview.bean.PomInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static oneview.ui.constants.GuiConstants.*;


public class BuildTableData implements TableData {
    private String project;
    private String buildStatus;
    private List<PomInfo> commands;
    private Date buildTime;
    private String edit;

    public BuildTableData() {
        this.project="";
        buildStatus = BUILD_FAILED;
        edit= BUILD_EDIT;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public List<PomInfo> getCommands() {
        return commands;
    }

    public void setCommands(List<PomInfo> commands) {
        this.commands = commands;
    }

    public Date getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(Date buildTime) {
        this.buildTime = buildTime;
    }

    public String getEdit() {
        return edit;
    }

    public void setEdit(String edit) {
        this.edit = edit;
    }


    @Override
    protected BuildTableData clone() {
        BuildTableData cloned = new BuildTableData();
        cloned.setBuildStatus(getBuildStatus());
        cloned.setBuildTime(getBuildTime());
        cloned.setProject(getProject());
        List<PomInfo> list = new ArrayList<>(getCommands());
        cloned.setCommands(list);
        cloned.setEdit(getEdit());

        return cloned;
    }

    public String getLog(String artifactId) {
        for (PomInfo p: commands){
            if (p.getArtifactID().equals(artifactId)){
                return p.getPomRunLog();
            }
        }
        return "";
    }
}
