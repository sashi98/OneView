package oneview.util;


import oneview.archive.ArchiveExplorer;
import oneview.archive.ArchivedExplorerHelper;
import oneview.ui.screens.jobtrigger.context.ESContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

import static oneview.ui.constants.TriggerFileConstants.*;
import static oneview.ui.screens.jobtrigger.context.ESContext.*;


public class FileUtil {

    public static void onDemandTriggering(ESContext esContext) throws Exception {
        final String CRON_NAME = esContext.getStringValue(CRON_JOB_NAME);
        final String CRON_START_TAG = "<" + CRON_NAME + ">";
        final String CRON_END_TAG = "</" + CRON_NAME + ">";
        String input =
                XML_SPECIFICATION + NEW_LINE +
                        CRON_START_TAG + NEW_LINE +
                        TAB_SPACE + START_TIME_START_TAG + esContext.getStringValue(START_DATE_TIME) + START_TIME_END_TAG + NEW_LINE +
                        TAB_SPACE + END_TIME_START_TAG + esContext.getStringValue(END_DATE_TIME) + END_TIME_END_TAG + NEW_LINE +
                        CRON_END_TAG + NEW_LINE;

        try {
            File triggerFile = new File(esContext.getStringValue(ON_DEMAND_DIR_LOCATION), esContext.getStringValue(TRIGGER_FILE_NAME));
            Files.write(triggerFile.toPath(), input.getBytes());
        } catch (IOException e) {
            throw new Exception(e);
        }
    }

    public static ImageIcon getScaledIcon(ImageIcon icon, int w, int h, int scaleSmooth) {
        return new ImageIcon(icon.getImage().getScaledInstance(w, h, scaleSmooth));
    }

    public static ImageIcon getScaledIcon(String name, int w, int h, int scaleSmooth) {
        URL iconFile = ResourceLoader.getResource(name);
        try {
            Image img = ImageIO.read(iconFile).getScaledInstance(w, h, scaleSmooth);
            return new ImageIcon(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ImageIcon getIcon(String name) {
        URL iconFile = ResourceLoader.getResource(name);
        try {
            return new ImageIcon(ImageIO.read(iconFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getMatchedFile(String dir, String name) {
        File[] list = new File(dir).listFiles(pathname -> {
            return pathname.getName().contains(name.substring(0, (name.length() - ".XML".length())));
        });
        return list.length == 0 ? "" : list[0].getName();
    }

    public static void saveSettings(String filePath, List<ESContext> ESContextList) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(filePath);
        ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
        objectOut.writeObject(ESContextList);
        objectOut.close();
    }

    public static List<ESContext> loadSettings(String filePath) throws IOException, ClassNotFoundException {
        FileInputStream is = new FileInputStream(filePath);
        ObjectInputStream ois = new ObjectInputStream(is);
        List<ESContext> mdList = (List<ESContext>) ois.readObject();
        ois.close();
        is.close();
        return mdList;
    }

    public static String readFile(String dir, String fileName) {
        try {
            return new String(Files.readAllBytes(new File(dir, fileName).toPath()));
        } catch (IOException e) {
            return "Error:" + e.getMessage();
        }

    }


    public static File getPomFile(String command) throws IOException {
        String commandsStr = "mvn|clean|install|-DskipTests|-Pintegration-tests".toLowerCase();
        File pomFile = null;
        String[] cArray = StringUtils.normalizeSpace(command).trim().split("\\p{Blank}");
        int i = 0;
        for (i = 0; i < cArray.length; i++) {
            if (commandsStr.contains(cArray[i].toLowerCase())) {
                continue;
            }
            if (cArray[i].endsWith("pom.xml")) {
                pomFile = new File(cArray[i]);
                break;
            }
        }

        if (pomFile != null && pomFile.getCanonicalFile().exists()) {
            return pomFile;
        }
        return null;
    }

    public static File download(URL url, File toDir) throws IOException {
        JFileChooser fileChooser = new JFileChooser(toDir);
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showOpenDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            toDir = fileChooser.getSelectedFile();
        }
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(url.toString());
        HttpResponse response = client.execute(httpGet);
        InputStream in = response.getEntity().getContent();
        String fileName;
        if (response.getHeaders("Content-Disposition").length > 0) {
            fileName = response.getHeaders("Content-Disposition")[0].getElements()[0].getParameterByName("filename").getValue();
        } else {
            fileName = new File(url.getFile()).getName();
        }
        File destFile = new File(toDir, fileName);
        Files.copy(in, destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return destFile;
    }

    private static String getFileNameWithoutExt(String fn) {
        return null;
    }

    private static String getVersion(String fn) {
        return null;
    }

    public static void main(String[] args) throws IOException {
        System.out.println(download(new URL("http://nexus:8081/repository/releases/com/healthedge/customer/ucare/ucare-medicare-member-id-card-repos/20.3.0.6-202009211511-7f8e20b/ucare-medicare-member-id-card-repos-20.3.0.6-202009211511-7f8e20b.kar"),
                new File("/home/weblogic/software/grbg")));
    }
}
