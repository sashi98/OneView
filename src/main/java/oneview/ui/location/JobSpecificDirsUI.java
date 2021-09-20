package oneview.ui.location;

import oneview.ui.component.CJLocation;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.GuiConstants.*;


public class JobSpecificDirsUI extends CJPanel {
    private CJLocation onDemandLocation;
    private CJLocation successLocation;
    private CJLocation failureLocation;
    private CJLocation logLocation;

    public JobSpecificDirsUI(CJPanel parent){

        onDemandLocation = new CJLocation(parent,"On Demand Dir", LOCATION_UI_TXT_FIELD_WIDTH, JFileChooser.DIRECTORIES_ONLY);
        successLocation = new CJLocation(parent,"Success Dir",LOCATION_UI_TXT_FIELD_WIDTH, JFileChooser.DIRECTORIES_ONLY);
        failureLocation = new CJLocation(parent,"Failure Dir",LOCATION_UI_TXT_FIELD_WIDTH, JFileChooser.DIRECTORIES_ONLY);
        logLocation = new CJLocation(parent,"Log Dir",LOCATION_UI_TXT_FIELD_WIDTH, JFileChooser.DIRECTORIES_ONLY);
        this.setLayout(new GridLayout(2,2, 0,0));
        this.add(onDemandLocation);
        this.add(successLocation);
        this.add(failureLocation);
        this.add(logLocation);
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Job Specific Directories", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));
    }

    public CJLocation getOnDemandLocationUI() {
        return onDemandLocation;
    }

    public CJLocation getSuccessLocationUI() {
        return successLocation;
    }

    public CJLocation getFailureLocationUI() {
        return failureLocation;
    }

    public CJLocation getLogLocationUI() {
        return logLocation;
    }
}
