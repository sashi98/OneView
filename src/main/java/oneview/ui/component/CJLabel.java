package oneview.ui.component;

import javax.swing.*;

import static oneview.ui.constants.GuiConstants.NORMAL_TXT_FONT;
import static oneview.util.GuiUtil.newDimentionObj;
import static oneview.util.GuiUtil.percent;

public class CJLabel extends JLabel {
    public CJLabel(String lName, int h) {
        this(lName, percent(lName.length(), 540), h);
    }

    public CJLabel(String lName, int w, int h, int align) {
        super(lName);
        //this.setOpaque(true);
        this.setHorizontalAlignment(align);
        this.setPreferredSize(newDimentionObj(w, h));
        this.setFont(NORMAL_TXT_FONT);
    }

    public CJLabel(String lName, int w, int h) {
        this(lName, w, h, JLabel.LEFT);
    }

    public CJLabel(String lName){
        super(lName);
        this.setFont(NORMAL_TXT_FONT);
    }
    public CJLabel() {

    }
}
