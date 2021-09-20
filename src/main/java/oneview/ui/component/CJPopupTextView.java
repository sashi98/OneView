package oneview.ui.component;

import javax.swing.*;

import java.awt.*;

import static oneview.ui.constants.GuiConstants.LINE_GRAY_1THK_BORDER;
import static oneview.ui.constants.GuiConstants.NORMAL_TXT_FONT;
import static oneview.util.GuiUtil.newDimentionObj;

public class CJPopupTextView extends JPopupMenu {
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private int x;
    private int y;
    private Component invoker;
    public CJPopupTextView(String txt, int x, int y, Component invoker, int w, int h){
        this.textPane = new JTextPane();
        this.x= x;
        this.y =y;
        this.invoker=invoker;
        this.textPane.setText(txt);
        this.setFont(NORMAL_TXT_FONT);
        this.scrollPane = new JScrollPane(textPane);
        this.scrollPane.setPreferredSize(newDimentionObj(w, h));
        this.setBorder(LINE_GRAY_1THK_BORDER);
        this.add(scrollPane);
        this.setPreferredSize(newDimentionObj(w,h));
    }

    public CJPopupTextView(CJPanel component, int x, int y, Component invoker, int w, int h){
        this.x= x;
        this.y =y;
        this.invoker=invoker;
        this.setFont(NORMAL_TXT_FONT);
        this.setBorder(LINE_GRAY_1THK_BORDER);
        this.add(component);
        this.setPreferredSize(newDimentionObj(w,h));
    }



    public void showIt(){
        this.show(invoker, x, y);
    }
}
