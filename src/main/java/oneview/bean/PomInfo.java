package oneview.bean;

import java.io.File;

import static oneview.ui.constants.GuiConstants.POM_RUN_SUCCESS;

public class PomInfo {
    private String command;
    private String artifactID;
    private File pom;
    private String pomRunLog;
    private String pomRunStatus;

    public PomInfo(){
        this.pomRunLog="";
        this.pomRunStatus=POM_RUN_SUCCESS;
    }

    public PomInfo(String command, String artifactID, File pom) {
        this();
        this.command = command;
        this.artifactID = artifactID;
        this.pom = pom;

    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public File getPom() {
        return pom;
    }

    public void setPom(File pom) {
        this.pom = pom;
    }

    public String getArtifactID() {
        return artifactID;
    }

    public void setArtifactID(String artifactID) {
        this.artifactID = artifactID;
    }

    public String getPomRunLog() {
        return pomRunLog;
    }

    public void setPomRunLog(String pomRunLog) {
        this.pomRunLog = pomRunLog;
    }

    public String getPomRunStatus() {
        return pomRunStatus;
    }

    public void setPomRunStatus(String pomRunStatus) {
        this.pomRunStatus = pomRunStatus;
    }
}
