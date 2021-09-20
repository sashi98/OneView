package oneview.service.server;

import oneview.script.ScriptRunner;
import oneview.util.EnvironmentVariables;

import java.io.File;
import java.io.IOException;

import static oneview.ui.screens.deploy.DeploymentScreen.CONNECTOR_SERVER;
import static oneview.util.ThreadUtil.sleep;

public class CprimeServer implements ServerService {
    private ScriptRunner runner;
    private String homeDir;
    static final long WAITING_TIMEOUT = 30000L;// 30 seconds
    static final long INTERVAL = 2000L;

    public CprimeServer() {
        this.runner = new ScriptRunner();
        this.homeDir = EnvironmentVariables.getEnvVars(CONNECTOR_SERVER);
    }

    @Override
    public String stop(String exePath) throws IOException {
        long timeout = WAITING_TIMEOUT;
        while (!healthCheck("/bin/status").equalsIgnoreCase("Not Running ...")) {
            waitFor(INTERVAL);
            runner.runShellScript(new String[]{"sh", new File(homeDir, exePath).getCanonicalPath()});

            timeout -= INTERVAL;
            if (timeout <= 0) break;
            System.out.println("WAITING FOR SERVER TO STOP");
        }
        String log = "";
        if (timeout > 0) {
            log = "SERVER STOPPED";
        } else {
            log += "[ERROR] UNABLE TO STOP SERVER. FORCE KILL NEEDED";

        }
        return log+"\n";
    }


    @Override
    public String start(String exePath) throws IOException {
        long timeout = WAITING_TIMEOUT;
        while (!healthCheck("/bin/status").equalsIgnoreCase("Running ...")) {
            waitFor(INTERVAL);
            runner.runShellScript(new String[]{"sh", new File(homeDir, exePath).getCanonicalPath()});
            timeout -= INTERVAL;
            if (timeout <= 0) break;

            System.out.println("WAITING FOR SERVER TO START");
        }
        String log = "";
        if (timeout > 0) {
            log = "SERVER STARTED";
        } else {
            log += "[ERROR] UNABLE TO START SERVER. TROUBLESHOOT NEEDED";
        }

        return log+"\n";
    }

    @Override
    public String restart(String stopExe, String startExe) throws IOException {
        StringBuilder log = new StringBuilder();
        String status = healthCheck("");
        if (status.equalsIgnoreCase("Not Running ...")) {
            log.append(start(startExe));
            log.append(stop(startExe));

        } else {
            log.append(stop(startExe));
            log.append(start(startExe));
        }
        return log.toString();
    }

    @Override
    public String healthCheck(String exePath) throws IOException {
        Process process = runner.runShellScript(new String[]{"sh", new File(homeDir, exePath).getCanonicalPath()});
        String output = runner.captureOutput(process);
        output = output.trim();
        System.out.println("HEALTH CHECK : " + output);
        return output;
    }

    @Override
    public String forceKill(String serverHome) {
        return null;
    }


    public void waitFor(long timeout) {
        while (timeout >= 0) {
            sleep(100);
            timeout -= 100;
        }
    }
}
