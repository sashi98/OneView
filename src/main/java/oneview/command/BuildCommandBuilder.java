package oneview.command;

import oneview.util.StringUtil;

public class BuildCommandBuilder implements CommandBuilder {
    private String command;

    @Override
    public void construct() {

    }

    public BuildCommandBuilder mvn() {
        command = "mvn ";
        return this;
    }

    public BuildCommandBuilder pom(String pom) {
        if (StringUtil.isNotBlank(pom)){
            command +=" -f "+pom;
        }
        return this;
    }

    public BuildCommandBuilder clean(boolean cleanSelected) {
        if (cleanSelected){
            command +=" clean ";
        }
        return this;
    }

    public BuildCommandBuilder install(boolean installSelected) {
        if (installSelected){
            command +=" install ";
        }
        return this;
    }

    public BuildCommandBuilder skipTests(boolean skipTestSelected) {
        if (skipTestSelected){
            command +=" -DskipTests=true ";
        }
        return this;
    }

    public BuildCommandBuilder skipIT(boolean skipITSelected) {
        if (!skipITSelected){
            command +=" -Pintegration-tests ";
        }
        return this;
    }


    public String getCommand() {
        System.out.println(this);
        return command;
    }

    @Override
    public String toString() {
        return "BuildCommandBuilder{" +
                "command='" + command + '\'' +
                '}';
    }
}
