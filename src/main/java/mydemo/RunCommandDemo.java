package mydemo;

import oneview.script.ScriptRunner;

import java.io.IOException;

import static oneview.script.ScriptRunner.EXECUTING;
import static oneview.util.ThreadUtil.sleep;

public class RunCommandDemo {
    public static void main(String[] args) throws IOException {
        ScriptRunner scriptRunner = new ScriptRunner();
//        ///opt/oracle/weblogic12cR2/user_projects/domains/healthedge/stop_server.sh
//        //String[] cmd = {"sh", "/opt/oracle/weblogic12cR2/user_projects/domains/healthedge/stop_server.sh"};
        String cmd = "rm -v /home/weblogic/cprime/correspondences/Medicare-Welcome-Letter/output/abc.txt";
////        Process process = scriptRunner.runMVN(cmd);
        Process process = scriptRunner.runNormalCommand(cmd);
        new Thread(() -> {
            while (scriptRunner.getStatus().equals(EXECUTING)) {
                sleep(1000);
            }
            System.out.println(scriptRunner.captureOutput(process));//, cmd[1]));

        }).start();
    }
}
