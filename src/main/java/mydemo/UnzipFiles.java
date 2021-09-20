package mydemo;


import oneview.archive.ArchiveExplorer;
import oneview.archive.ArchivedExplorerHelper;

import java.io.File;

public class UnzipFiles {

    public static void main(String[] args) {
        String zipFilePath = "/home/weblogic/PROJECT/OneView/grbg/KAR_D/ucare-core-repos-20.3.0.6-SNAPSHOT.kar";

        ArchiveExplorer archiveExploder = ArchivedExplorerHelper.getArchiveExplorer("kar", new File(zipFilePath));
        System.out.println(archiveExploder.getArchiveFileDetails());

    }


}