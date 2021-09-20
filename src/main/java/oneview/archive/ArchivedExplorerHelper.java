package oneview.archive;

import oneview.util.StringUtil;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ArchivedExplorerHelper {
    private static List<String> extList = new ArrayList<>();

    static {
        extList.add("KAR");
    }

    public ArchivedExplorerHelper() {
    }


    public static ArchiveExplorer getArchiveExplorer(String ext, File archiveFile) {
        if (extList.contains(ext.toUpperCase())) {
            try {
                return (ArchiveExplorer) Class.forName("oneview.archive." + StringUtil.toCamelCase(ext) + "Explorer").getConstructor(File.class).newInstance(archiveFile);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(StringUtil.toCamelCase("kasr"));
    }
}

