package oneview.ui.component;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static oneview.ui.constants.GuiConstants.*;
import static oneview.util.GuiUtil.newDimentionObj;

public class CJTextField extends JFormattedTextField {
    private AbstractFormatter formatter;
    private Border border = COMPOUND_TXT_FLD_BORDER;

    public CJTextField(int w, int h, AbstractFormatter formatter) {
        super(formatter);
        this.formatter = formatter;
        this.setPreferredSize(newDimentionObj(w, h));
        this.setBorder(border);
        this.setFont(NORMAL_TXT_FONT);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (KeyEvent.VK_CONTROL + KeyEvent.VK_V)) {
                    paste();
                }
            }
        });
    }

    public CJTextField() {
        super();
        this.setBorder(border);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (KeyEvent.VK_CONTROL + KeyEvent.VK_V)) {
                    paste();
                }
            }
        });
    }

    public CJTextField(int w, int h) {
        super();
        this.setPreferredSize(newDimentionObj(w, h));
        this.setBorder(border);
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == (KeyEvent.VK_CONTROL + KeyEvent.VK_V)) {
                    paste();
                }
            }
        });
    }


    @Override
    public AbstractFormatter getFormatter() {
        return formatter;
    }
}
