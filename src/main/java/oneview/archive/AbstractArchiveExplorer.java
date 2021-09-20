package oneview.archive;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static oneview.archive.ArchieveConstants.ARCHIVE_FILE_KEY;
import static oneview.archive.ArchieveConstants.EXTENSION_KEY;

abstract class AbstractArchiveExplorer implements ArchiveExplorer {
    private Map<String, Object> archiveFileDetails = new HashMap<>();

    public AbstractArchiveExplorer(File file) {
        archiveFileDetails.put(EXTENSION_KEY,getExt(file));
        archiveFileDetails.put(ARCHIVE_FILE_KEY, file);
        fillArchiveFileDetailsMap(file);
    }

    public AbstractArchiveExplorer() {

    }

    @Override
    public String getExt(File karFile) {
        return FilenameUtils.getExtension(karFile.getName());
    }

    @Override
    public Map<String, Object> getArchiveFileDetails() {
        return archiveFileDetails;
    }

}
