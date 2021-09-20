package oneview.ui.screens.deploy.pipeline.table;

public class PipelineTableData {
    public static final String START_SERVER = "START_SERVER";
    public static final String STOP_SERVER = "STOP_SERVER";
    public static final String UNINSTALL = "UNINSTALL";
    public static final String CLEANUP = "CLEANUP";
    public static final String ADD_DISTRIBUTION = "ADD_DISTRIBUTION";
    public static final String INSTALL = "INSTALL";


    private String uninstall;
    private String stopServer;
    private String cleanup;
    private String startServer;
    private String addDistribution;
    private String install;

    private String uninstallLog;
    private String stopServerLog;
    private String cleanupLog;
    private String startServerLog;
    private String addDistributionLog;
    private String installLog;

    public PipelineTableData() {
        this.uninstall = UNINSTALL;
        this.stopServer = STOP_SERVER;
        this.cleanup = CLEANUP;
        this.startServer = START_SERVER;
        this.addDistribution = ADD_DISTRIBUTION;
        this.install = INSTALL;
    }

    public String getUninstall() {
        return uninstall;
    }

    public void setUninstall(String uninstall) {
        this.uninstall = uninstall;
    }

    public String getStopServer() {
        return stopServer;
    }

    public void setStopServer(String stopServer) {
        this.stopServer = stopServer;
    }

    public String getCleanup() {
        return cleanup;
    }

    public void setCleanup(String cleanup) {
        this.cleanup = cleanup;
    }

    public String getStartServer() {
        return startServer;
    }

    public void setStartServer(String startServer) {
        this.startServer = startServer;
    }

    public String getAddDistribution() {
        return addDistribution;
    }

    public void setAddDistribution(String addDistribution) {
        this.addDistribution = addDistribution;
    }

    public String getInstall() {
        return install;
    }

    public void setInstall(String install) {
        this.install = install;
    }

    public String getUninstallLog() {
        return uninstallLog;
    }

    public void setUninstallLog(String uninstallLog) {
        this.uninstallLog = uninstallLog;
    }

    public String getStopServerLog() {
        return stopServerLog;
    }

    public void setStopServerLog(String stopServerLog) {
        this.stopServerLog = stopServerLog;
    }

    public String getCleanupLog() {
        return cleanupLog;
    }

    public void setCleanupLog(String cleanupLog) {
        this.cleanupLog = cleanupLog;
    }

    public String getStartServerLog() {
        return startServerLog;
    }

    public void setStartServerLog(String startServerLog) {
        this.startServerLog = startServerLog;
    }

    public String getAddDistributionLog() {
        return addDistributionLog;
    }

    public void setAddDistributionLog(String addDistributionLog) {
        this.addDistributionLog = addDistributionLog;
    }

    public String getInstallLog() {
        return installLog;
    }

    public void setInstallLog(String installLog) {
        this.installLog = installLog;
    }
}
