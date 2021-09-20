package oneview.karaf;

public class PipelineUnit {
    private String pipelineUnitName;
    private String log;

    public PipelineUnit(String pipelineUnitName, String log) {
        this.pipelineUnitName = pipelineUnitName;
        this.log = log;
        System.out.println(log);
    }

    public String getPipelineUnitName() {
        return pipelineUnitName;
    }

    public String getLog() {
        return log;
    }
}
