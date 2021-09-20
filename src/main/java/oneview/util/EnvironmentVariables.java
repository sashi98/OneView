package oneview.util;

import java.util.Map;

public class EnvironmentVariables {
    private static Map<String, String> envVars = System.getenv();

    public static String getEnvVars(String key) {
        return envVars.get(key);
    }
}
