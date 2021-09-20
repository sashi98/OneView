package oneview.karaf.pipeline;

import oneview.archive.ArchiveExplorer;
import oneview.karaf.ssh.client.SshClient;
import oneview.ui.screens.deploy.table.DeployTableData;
import oneview.util.StringUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static oneview.archive.ArchieveConstants.EXTENSION_KEY;


public class CommandPipelineBuilderHelper {
    private static List<String> extList = new ArrayList<>();

    static {
        extList.add("KAR");
    }

    public CommandPipelineBuilderHelper() {
    }


    public static CommandPipelineBuilder getCommandPipelineBuilderHelper(SshClient client, DeployTableData data) {
        String ext = (String) data.getArtifactDetails().get(EXTENSION_KEY);
        if (extList.contains(ext.toUpperCase())) {
            try {
                return (CommandPipelineBuilder) Class.forName("oneview.karaf.pipeline." + StringUtil.toCamelCase(ext) + "CommandPipelineBuilder").getConstructor(SshClient.class, DeployTableData.class).newInstance(client, data);
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.toCamelCase("kasr"));
    }
}

