package oneview.karaf.ssh.client;

import com.jcraft.jsch.*;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static oneview.util.StringUtil.EMPTY_STRING;
import static oneview.util.ThreadUtil.sleep;

public class SshClient {
    String host = "heeng-sprasad";
    String user = "connector";
    String password = "Connector123";
    private ChannelExec channel;
    private Session session;

    public SshClient() throws JSchException {
        JSch jsch = new JSch();
        session = jsch.getSession(user, host, 8101);
        session.setUserInfo(new UserInfo() {
            @Override
            public String getPassphrase() {
                throw new UnsupportedOperationException("getPassphrase Not supported yet.");
            }

            @Override
            public String getPassword() {
                return password;
            }

            @Override
            public boolean promptPassword(String s) {
                return true;
            }

            @Override
            public boolean promptPassphrase(String s) {
                throw new UnsupportedOperationException("promptPassphrase Not supported yet.");
            }

            @Override
            public boolean promptYesNo(String s) {
                return true;
            }

            @Override
            public void showMessage(String s) {

            }
        });
        session.connect();
        channel = (ChannelExec) session.openChannel("exec");
    }

    public static void main(String[] args) throws JSchException, IOException {
        SshClient client = new SshClient();
        ChannelExec channel = client.getChannel();
        String command = "list|grep ucare-medi";
        String output = client.execute(command);
        int exitStatus = client.close();
        System.out.println(output);
        System.out.print("Exit status of the execution: " + exitStatus);
        if (exitStatus == 0) {
            System.out.print(" (OK)\n");
        } else {
            System.out.print(" (NOK)\n");
        }
    }

    private void connect() throws JSchException {
        channel.connect();
    }

    public String execute(String command) throws JSchException, IOException {
        System.out.println(command);
        channel.setCommand(command);
        connect();
        channel.setOutputStream(System.out);
        InputStream in = channel.getInputStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        StringBuilder outBuff = new StringBuilder();

        String line;
        String[] junks = new String[]{"\\[m","\\[43;30m", "\\[1;32m","\\[36m","\\[1;42;30m","\\[39;22m"};
        String[] replacements = new String[]{EMPTY_STRING,EMPTY_STRING, EMPTY_STRING,EMPTY_STRING,EMPTY_STRING,EMPTY_STRING};
        while ((line = br.readLine()) != null) {
            StringUtils.replaceEachRepeatedly(line, junks, replacements);
            outBuff.append(line + "\n");
            System.out.println(line);
        }

        return outBuff.toString();
    }

    public int close() {
        int exitStatus = 0;

        channel.disconnect();
        session.disconnect();
        if (channel.isClosed()) {
            exitStatus = channel.getExitStatus();
        }
        return exitStatus;
    }

    public ChannelExec getChannel() {
        return channel;
    }
}
