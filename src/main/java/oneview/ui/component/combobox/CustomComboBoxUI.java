package oneview.ui.component.combobox;

import oneview.ui.component.CJButton;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;

import static oneview.ui.constants.GuiConstants.NORMAL_TXT_FONT;
import static oneview.ui.constants.IconConstants.ARROW_DEFAULT_ICON;
import static oneview.ui.constants.IconConstants.ARROW_HOVERED_ICON;

public class CustomComboBoxUI extends BasicComboBoxUI {

    public static ComponentUI createUI(JComponent c) {
        return new CustomComboBoxUI();
    }

    @Override
    protected JButton createArrowButton() {
        CJButton arrow = new CJButton(ARROW_DEFAULT_ICON, ARROW_HOVERED_ICON, 12, 11);
        return arrow;
    }

    @Override
    protected ComboBoxEditor createEditor() {
        ComboBoxEditor editor = new BasicComboBoxEditor();
        editor.getEditorComponent().setFont(NORMAL_TXT_FONT);
        return editor;
    }
}
