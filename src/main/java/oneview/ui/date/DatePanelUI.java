package oneview.ui.date;

import oneview.ui.component.CJPanel;

import javax.swing.border.TitledBorder;
import java.awt.*;

import static oneview.ui.constants.DimensionConstants.DATE_PANEL_HEIGHT;
import static oneview.ui.constants.DimensionConstants.DATE_PANEL_WIDTH;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.util.GuiUtil.newDummyPanelObj;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DatePanelUI extends CJPanel {
    private DatePanelHeader header;
    private DatePanel2[] datePanels;

    public DatePanelUI(/*CJPanel parent*/) {
        super();
        this.header = new DatePanelHeader(DATE_PANEL_WIDTH, DATE_PANEL_HEIGHT);
        this.datePanels = new DatePanel2[2];
        DatePanel2 startDatePanel = new DatePanel2(/*parent,*/"Start Date", DATE_PANEL_WIDTH, DATE_PANEL_HEIGHT, FALSE, TRUE);
        DatePanel2 endDatePanel = new DatePanel2(/*parent,*/"End Date", DATE_PANEL_WIDTH, DATE_PANEL_HEIGHT, TRUE, FALSE);
        this.datePanels[0] = startDatePanel;
        this.datePanels[1] = endDatePanel;
        LayoutManager lm = new GridLayout(3,1,0,0);
        this.setLayout(lm);

        this.add(header);
        this.add(datePanels[0]);
        this.add(datePanels[1]);
        this.setBorder(new TitledBorder(LINE_LGRAY_1THK_BORDER, "Cron Timings", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, TITLE_HEADER_1_FONT));

    }

    public DatePanel2[] getDatePanels() {
        return datePanels;
    }

}
