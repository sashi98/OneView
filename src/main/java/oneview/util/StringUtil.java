package oneview.util;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Locale;

public class StringUtil {
    public static final String EMPTY_STRING = "";
    private static final String NEW_LINE = "\n";

    private StringUtil(){}

    public static boolean isBlank(String string) {
        return StringUtils.isBlank(string) || string.equals("null");
    }

    public static boolean isNotBlank(String string) {
        return !isBlank(string);
    }


    public static String getArtifactID(File pomFile) {
        File dir = pomFile.getParentFile();
        return dir.getName();
    }


    public static String toCamelCase(String str) {
        str = str.toLowerCase(Locale.ENGLISH);
        return str.substring(0,1).toUpperCase(Locale.ENGLISH)+str.substring(1).toLowerCase(Locale.ENGLISH);
    }

    public static String newLineSeparator(String s, int length) {
        StringBuilder sb = new StringBuilder(NEW_LINE);
        for (int i=0; i<=length; i++){
            sb.append(s);
        }
        sb.append(NEW_LINE);
        return sb.toString();
    }
}
