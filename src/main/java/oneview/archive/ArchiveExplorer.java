package oneview.archive;

import java.io.File;
import java.util.Map;

public interface ArchiveExplorer {


    Map<String, Object> getArchiveFileDetails();

    String getExt(File karFile);

    void fillArchiveFileDetailsMap(File karFile);


}