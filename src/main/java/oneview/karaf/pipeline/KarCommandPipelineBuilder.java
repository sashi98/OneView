package oneview.karaf.pipeline;

import com.jcraft.jsch.JSchException;
import oneview.karaf.PipelineUnit;
import oneview.karaf.ssh.client.SshClient;
import oneview.script.ScriptRunner;
import oneview.service.server.CprimeServer;
import oneview.ui.screens.deploy.table.DeployTableData;
import oneview.util.StringUtil;
import org.apache.commons.lang3.BooleanUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static oneview.ui.constants.GuiConstants.FEATURE_UNINSTALLED;
import static oneview.util.StringUtil.EMPTY_STRING;

public class KarCommandPipelineBuilder extends AbstractCommandPipelineBuilder {
    //ucare-medicare-member-id-card-repos

    private final String listFeature = "list|grep {FEATURE}";
    private final String installFeature = "feature:install {FEATURE}-repos";
    private final String uninstallFeature = "feature:uninstall {FEATURE}-repos";
    private final String addDistribution = "hrc:add-distribution {PATH}";
    private final String addDistributionByForce = "hrc:add-distribution --force {PATH}";

    private final String listKar = "kar:list|grep {FEATURE_PART}";
    private final String uninstallKar = "kar:uninstall {KAR_NAME}";
    private final DeployTableData data;

    private SshClient client;
    private String listFeatureLog = "[Feature List]:\n";
    private String installFeatureLog = "[Install Feature]:\n";
    private String uninstallFeatureLog = "[Uninstall Feature]:\n";
    private String addDistributionLog = "[Add Distribution]:\n";
    private String addDistributionByForceLog = "[Add Distribution Forcefully]:\n";
    private String listKarLog = "[Kar(s) List]:\n";
    private String cprimeStoppedLog = "[Crime Server]:\n";
    private String cprimeStartedLog = "[Crime Server]:\n";

    private String uninstallKarLog;
    private String consolidatedLogs;
    private List<PipelineUnit> pipeline;

    public KarCommandPipelineBuilder(SshClient client, DeployTableData data) {
        super();
        this.client = client;
        this.pipeline = new ArrayList<>();
        this.consolidatedLogs = EMPTY_STRING;
        this.data = data;
    }

    @Override
    public KarCommandPipelineBuilder listFeature() throws IOException, JSchException {
        String feature = data.getFeatureName();
        String command = listFeature.replace("{FEATURE}", feature);
        String log = client.execute(command);
        if (StringUtil.isBlank(log)) {
            log = "'" + feature + "' is not installed.";
        }
        listFeatureLog += log;
        appendToConsolidatedLogs(listFeatureLog);
        pipeline.add(new PipelineUnit("Feature List", listFeatureLog));
        return this;
    }


    @Override
    public KarCommandPipelineBuilder installFeature() throws IOException, JSchException {
        String feature = data.getFeatureName();
        String command = installFeature.replace("{FEATURE}", feature);
        installFeatureLog = client.execute(command);
        appendToConsolidatedLogs(installFeatureLog);
        pipeline.add(new PipelineUnit("Feature Install", installFeatureLog));
        return this;
    }

    @Override
    public KarCommandPipelineBuilder uninstallFeature() throws IOException, JSchException {
        String feature = data.getFeatureName();
        String command = uninstallFeature.replace("{FEATURE}", feature);
        uninstallFeatureLog = client.execute(command);
        if (StringUtil.isBlank(uninstallFeatureLog)) {
            uninstallFeatureLog = "'" + feature + "' uninstalled successfully.";
            data.getArtifactDetails().put(FEATURE_UNINSTALLED, true);
        } else {
            data.getArtifactDetails().put(FEATURE_UNINSTALLED, false);
        }
        appendToConsolidatedLogs(uninstallFeatureLog);
        pipeline.add(new PipelineUnit("Feature Uninstall", uninstallFeatureLog));
        return this;
    }

    @Override
    public KarCommandPipelineBuilder uninstallArchives() throws IOException, JSchException {
        if((Boolean) data.getArtifactDetails().get(FEATURE_UNINSTALLED)) {
            String featureNamePart = data.getFeatureName();
            List<String> kars = getKarList(featureNamePart);
            for (String kar : kars) {
                uninstallKarLog += "Uninstalling KAR(" + kar + "):\n" + uninstallKar(kar) + "\n";
            }
            appendToConsolidatedLogs(uninstallKarLog);
            pipeline.add(new PipelineUnit("Kar(s) Uninstall", uninstallKarLog));
        }
        return this;
    }

    @Override
    public String uninstallKar(String kar) throws IOException, JSchException {
        String command = uninstallKar.replace("{KAR_NAME}", kar);
        String log = client.execute(command);
        if (StringUtil.isBlank(log)) {
            return "'" + kar + "' uninstalled successfully.";
        } else {
            return log;
        }
    }

    @Override
    public List<String> getKarList(String featureNamePart) throws IOException, JSchException {
        List<String> list = new ArrayList<>();
        String command = listKar.replace("{FEATURE_PART}", featureNamePart);
        String listLogStr = client.execute(command);
        if (StringUtil.isNotBlank(listLogStr)) {
            String[] listLog = listLogStr.split("");
            list.addAll(Arrays.asList(listLog));
        }

        return list;
    }

    @Override
    public String getListFeatureLog() {
        return listFeatureLog;
    }

    @Override
    public String getInstallFeatureLog() {
        return installFeatureLog;
    }

    @Override
    public String getUninstallFeatureLog() {
        return uninstallFeatureLog;
    }

    @Override
    public String getAddDistributionLog() {
        return addDistributionLog;
    }

    @Override
    public String getAddDistributionByForceLog() {
        return addDistributionByForceLog;
    }

    @Override
    public String getListKarLog() {
        return listKarLog;
    }

    @Override
    public String getUninstallKarLog() {
        return uninstallKarLog;
    }

    @Override
    public void appendToConsolidatedLogs(String log) {
        if (StringUtil.isBlank(log)) log = EMPTY_STRING;
        consolidatedLogs += log;
        consolidatedLogs += StringUtil.newLineSeparator("-", 100);
    }

    public String getConsolidatedLogs() {
        System.out.println(consolidatedLogs);
        return consolidatedLogs;
    }

    public List<PipelineUnit> getPipeline() {
        return pipeline;
    }


    @Override
    public KarCommandPipelineBuilder addDistribution() throws IOException, JSchException {
        File artifactPath = data.getArtifactPath();
        String command = addDistribution.replace("{PATH}", artifactPath.getPath());
        addDistributionLog = client.execute(command);
        appendToConsolidatedLogs(addDistributionLog);
        pipeline.add(new PipelineUnit("Add Distribution", addDistributionLog));
        return this;
    }

    @Override
    public KarCommandPipelineBuilder addDistributionByForce() throws IOException, JSchException {
        File artifactPath = data.getArtifactPath();
        String command = addDistributionByForce.replace("{PATH}", artifactPath.getPath());
        addDistributionByForceLog = client.execute(command);
        appendToConsolidatedLogs(addDistributionByForceLog);
        pipeline.add(new PipelineUnit("Add Distribution By Force", addDistributionByForceLog));
        return this;
    }

    @Override
    public KarCommandPipelineBuilder cleanUpFiles() {
        if((Boolean) data.getArtifactDetails().get(FEATURE_UNINSTALLED)) {
            ScriptRunner runner = new ScriptRunner();
            String log1 = runner.deleteFile(data.getBlueprint());
            String log2 = runner.deleteFile(data.getLastRunJson());
            String log3 = runner.deleteFile(data.getInstalledKarPath());
            appendToConsolidatedLogs("[Clean Up Files]\n" + log1);
            appendToConsolidatedLogs("[Clean Up Files]\n" + log2);
            appendToConsolidatedLogs("[Clean Up Files]\n" + log3);
            pipeline.add(new PipelineUnit("Clean Up Files", log1 + log2 + log3));
        }
        return this;
    }

    @Override
    public boolean isFeatureInstalled() {
        String msg = "'" + data.getFeatureName() + "' is not installed.".toLowerCase(Locale.ENGLISH);
        if (listFeatureLog.contains(msg)) {
            return false;
        }
        return true;
    }

    @Override
    public boolean successfulRun() {
        return false;
    }


    @Override
    public CommandPipelineBuilder stopServer(String stopScript) throws IOException {
        CprimeServer cprimeServer = new CprimeServer();
        cprimeStoppedLog += cprimeServer.stop(stopScript);
        appendToConsolidatedLogs(cprimeStoppedLog);
        pipeline.add(new PipelineUnit("Cprime Server Stop", cprimeStoppedLog));
        return this;
    }

    @Override
    public CommandPipelineBuilder startServer(String startScript) throws IOException {
        CprimeServer cprimeServer = new CprimeServer();
        cprimeStartedLog += cprimeServer.start(startScript);
        appendToConsolidatedLogs(cprimeStartedLog);
        pipeline.add(new PipelineUnit("Cprime Server Start", cprimeStartedLog));
        return this;
    }

    public static void main(String[] args) throws JSchException, IOException {
        CommandPipelineBuilder cpb = new KarCommandPipelineBuilder(new SshClient(), null);
        cpb.stopServer("/bin/stop").startServer("/bin/start");
        System.out.println(cpb.getConsolidatedLogs());
    }
}
