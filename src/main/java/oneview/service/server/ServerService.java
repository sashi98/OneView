package oneview.service.server;

import java.io.IOException;

public interface ServerService {
    String stop(String exePath) throws IOException;
    String start(String exePath) throws IOException;
    String restart(String stopExe, String startExe) throws IOException;
    String healthCheck(String serverHome) throws IOException;
    String forceKill(String serverHome);
}
