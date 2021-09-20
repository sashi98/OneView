package oneview.archive;

import oneview.util.EnvironmentVariables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static oneview.archive.ArchieveConstants.*;

public class KarExplorer extends AbstractArchiveExplorer {


    private static String karafEtcDir = EnvironmentVariables.getEnvVars("CONNECTOR_SERVER")+"/etc";
    private static String karafLastRunDir = EnvironmentVariables.getEnvVars("CONNECTOR_SERVER")+"/last-run-json";
    private static String karafBlueprintDir = EnvironmentVariables.getEnvVars("CONNECTOR_SERVER")+"/custom-blueprints-deploy";
    private static String karafInstallDir = EnvironmentVariables.getEnvVars("CONNECTOR_SERVER")+"/install";

    public KarExplorer() {
        super();
    }

    public KarExplorer(File karFile) {
        super(karFile);
    }

    @Override
    public void fillArchiveFileDetailsMap(File karFile) {
        try {
            getFeatureDetails(karFile);
            getOtherFilesDetails(karFile);
            getArchiveFileDetails().put(KAR_INSTALLED_PATH_KEY, new File(karafInstallDir,karFile.getName()).getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getOtherFilesDetails(File karFile) throws IOException {
        FileInputStream fis = new FileInputStream(karFile);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        String featureName = String.valueOf(getArchiveFileDetails().get(FEATURE_NAME_KEY));

        while (ze != null) {
            String name = new File(ze.getName()).getName();
            String bundle = (String) getArchiveFileDetails().get(BUNDLE_NAME_KEY);
            String version = (String) getArchiveFileDetails().get(VERSION_KEY);
            int start = name.indexOf(bundle+"-"+version+"-")+(bundle+"-"+version+"-").length();

            if (name.contains(".cfg")) {
                String cfg = name.substring(start);
                getArchiveFileDetails().put(CFG_KEY, new File(karafEtcDir,cfg).getCanonicalPath());
            }
            if (name.contains(featureName+".json")) {
                String json = name.substring(start);
                getArchiveFileDetails().put(LAST_RUN_JSON_KEY, new File(karafLastRunDir,json).getCanonicalPath());
            }
            if (name.contains(featureName+".xml")) {
                String blueprint = name.substring(start);
                getArchiveFileDetails().put(BLUEPRRINT_KEY, new File(karafBlueprintDir,blueprint).getCanonicalPath());
            }
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        //close last ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();
    }

    private void getFeatureDetails(File karFile) throws IOException {
        FileInputStream fis = new FileInputStream(karFile);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        while (ze != null) {
            if (ze.getName().contains("pom.properties")) {
                Properties p = new Properties();
                p.load(zis);
                getArchiveFileDetails().put(VERSION_KEY, p.getProperty("version"));
                String artifactId = p.getProperty("artifactId");
                String featureName = artifactId.replace("-repos", "");
                getArchiveFileDetails().put(FEATURE_NAME_KEY, featureName);
                getArchiveFileDetails().put(REPO_NAME_KEY, artifactId);
                getArchiveFileDetails().put(BUNDLE_NAME_KEY, featureName+"-bundle");
                getArchiveFileDetails().put(KAR_NAME_KEY, artifactId+"-"+p.getProperty("version"));
                break;
            }
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        //close last ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();
    }

    public static void main(String[] args) {
        File f = new File("/home/weblogic/PROJECT/MASTER/ucare-ifp-member-id-card/ucare-ifp-member-id-card-repos/target/ucare-ifp-member-id-card-repos-20.3.0.6-SNAPSHOT.kar");
        ArchiveExplorer ae = new KarExplorer(f);
        ae.getArchiveFileDetails().entrySet().forEach(e-> System.out.println(e.getKey()+" : "+ e.getValue()));
    }

}
