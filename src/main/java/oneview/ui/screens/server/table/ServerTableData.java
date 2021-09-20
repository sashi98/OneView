package oneview.ui.screens.server.table;

import oneview.ui.screens.common.table.TableData;


import java.util.Date;

import static oneview.ui.constants.GuiConstants.*;


public class ServerTableData implements TableData {
    private String serverName;
    private String serverHome;
    private String startScriptPath;
    private String stopScriptPath;
    private Date startTime;
    private Date stopTime;
    private String startStop;
    private String restart;
    private String serverStatus;

    public ServerTableData() {
        this.serverStatus = STOPPED;
        this.startStop = BUSY_SPIN;
        this.restart = RESTART;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getStartScriptPath() {
        return startScriptPath;
    }

    public void setStartScriptPath(String startScriptPath) {
        this.startScriptPath = startScriptPath;
    }

    public String getStopScriptPath() {
        return stopScriptPath;
    }

    public void setStopScriptPath(String stopScriptPath) {
        this.stopScriptPath = stopScriptPath;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    public String getStartStop() {
        return startStop;
    }

    public void setStartStop(String startStop) {
        this.startStop = startStop;
    }

    public String getRestart() {
        return restart;
    }

    public void setRestart(String restart) {
        this.restart = restart;
    }

    public String getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(String serverStatus) {
        this.serverStatus = serverStatus;
    }

    public String getServerHome() {
        return serverHome;
    }

    public void setServerHome(String serverHome) {
        this.serverHome = serverHome;
    }
}
