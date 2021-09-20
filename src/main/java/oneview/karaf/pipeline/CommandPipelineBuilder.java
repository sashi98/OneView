package oneview.karaf.pipeline;

import com.jcraft.jsch.JSchException;
import oneview.karaf.PipelineUnit;

import java.io.IOException;
import java.util.List;

public interface CommandPipelineBuilder {

    CommandPipelineBuilder addDistribution() throws IOException, JSchException;

    CommandPipelineBuilder installFeature() throws IOException, JSchException;

    String uninstallKar(String kar) throws IOException, JSchException;

    List<String> getKarList(String featureNamePart) throws IOException, JSchException;

    void appendToConsolidatedLogs(String log);

    String getConsolidatedLogs();

    CommandPipelineBuilder uninstallFeature() throws IOException, JSchException;

    CommandPipelineBuilder uninstallArchives() throws IOException, JSchException;

    CommandPipelineBuilder addDistributionByForce() throws IOException, JSchException;

    CommandPipelineBuilder cleanUpFiles();

    CommandPipelineBuilder listFeature() throws IOException, JSchException;

    List<PipelineUnit> getPipeline();

    String getListFeatureLog();
    String getInstallFeatureLog();

    String getUninstallFeatureLog();

    String getAddDistributionLog();

    String getAddDistributionByForceLog();

    String getListKarLog();

    String getUninstallKarLog();

    boolean isFeatureInstalled();

    boolean successfulRun();


    CommandPipelineBuilder stopServer(String stopScript) throws IOException;

    CommandPipelineBuilder startServer(String startScript) throws IOException;
}
