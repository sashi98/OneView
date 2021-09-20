package oneview.ui.component.combobox;


import javax.swing.*;

import static oneview.ui.constants.GuiConstants.NORMAL_TXT_FONT;

public class CJComboBox extends JComboBox{
    public CJComboBox(ComboBoxModel comboBoxModel) {
        super(comboBoxModel);
        setFont(NORMAL_TXT_FONT);
        setUI(CustomComboBoxUI.createUI(this));
    }
}
