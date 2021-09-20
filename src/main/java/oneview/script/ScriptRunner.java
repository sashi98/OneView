package oneview.script;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static oneview.util.ThreadUtil.sleep;

public class ScriptRunner {
    public final static String EXECUTING = "EXECUTING";
    public final static String EXECUTED = "EXECUTED";
    public final static String NOT_EXECUTED = "NOT_EXECUTED";

    private static String status = NOT_EXECUTED;

    public Process runShellScript(String[] cmd) {
        System.out.println(cmd[1]);
        status = EXECUTING;
        final Process[] process = new Process[1];
        try {
            process[0] = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {

                    while (true) {
                        process[0].waitFor();
                        break;
                    }
                    status = EXECUTED;
                } catch (Exception e) {

                }
            }
        }).start();

        return process[0];
    }

    public String getStatus() {
        return status;
    }

    /*public static void main(String[] args) {

        String[] cmd = {"sh",
                "/home/weblogic/cprime/healthrules-connector-server-4.0.4/bin/client"};
        new ScriptRunner().runShellScript(cmd);
    }*/


    public String captureOutput(Process process) {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                output.append(line +"\n");
            }
        }catch (Exception e){

        }

       return output.toString();
    }


    public String captureGrepOutput(Process process, String cmd) {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        try {
            if (reader.ready()) {
                while ((line = reader.readLine()) != null) {
                    if (line.contains(cmd)) {
                        continue;
                    }
                    output.append(line+"\n");
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            process.destroyForcibly();
        }
        return output.toString();
    }


    public boolean determineBuildSuccess(String log) {
        String buildSuccess = "BUILD SUCCESS";
        //String buildFailed = "BUILD FAILURE";

        boolean buildStatus = false;
        if (log.contains(buildSuccess)){
            buildStatus = true;
        }
        //System.out.println("BUILD STATUS:"+buildStatus);
        return buildStatus;
    }


    public Process runNormalCommand(String cmd) {
        status = EXECUTING;
        final Process[] process = new Process[1];
        try {
            process[0] = Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public synchronized void run() {
                try {

                    while (true) {
                        process[0].waitFor();
                        break;
                    }
                    status = EXECUTED;
                } catch (Exception e) {

                }
            }
        }).start();

        return process[0];
    }

    public Process runMVN(String cmd) {
        return runNormalCommand(cmd);
    }

    public String deleteFile(String filePath) {
        String rmCmd = "rm -v "+filePath;
        Process p = runNormalCommand(rmCmd);
        return captureOutput(p);
    }

}
