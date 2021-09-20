package oneview.ui.component;

import oneview.ui.RegisterListener;
import oneview.util.FileUtil;
import oneview.util.StringUtil;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static oneview.util.GuiUtil.newDimentionObj;
import static java.awt.Image.SCALE_SMOOTH;

public class CJButton extends JButton  implements RegisterListener {
    private ImageIcon defaultIcon, hoveredIcon;

    public CJButton(String name){
        super(name);
    }
    public CJButton(ImageIcon defaultIcon, ImageIcon hoveredIcon, int w, int h, String toolTipTxt) {
        super();
        if (defaultIcon != null) {
            this.defaultIcon = FileUtil.getScaledIcon(defaultIcon, w, h, SCALE_SMOOTH);
            this.setIcon(this.defaultIcon);
        }
        if (hoveredIcon != null) {
            this.hoveredIcon = FileUtil.getScaledIcon(hoveredIcon, w, h, SCALE_SMOOTH);
        }
        if (StringUtil.isNotBlank(toolTipTxt)) {
            this.setToolTipText(toolTipTxt);
        }
        this.setPreferredSize(newDimentionObj(w,h));
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        registerListener();
    }

    public CJButton(ImageIcon defaultIcon,int w, int h, String name) {
        this.defaultIcon = FileUtil.getScaledIcon(defaultIcon, w, h, SCALE_SMOOTH);
        this.setText(name);
        this.setIcon(this.defaultIcon);
        this.setHorizontalTextPosition(JLabel.LEFT);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        registerListener();
    }

    public CJButton(ImageIcon defaultIcon, ImageIcon hoveredIcon, int w, int h) {
        this(defaultIcon,hoveredIcon, w, h, null);
    }

    public CJButton(int w, int h, String tooltip) {
        this(null, null, w, h, tooltip);
        this.setOpaque(true);
        this.setBorderPainted(true);
        this.setContentAreaFilled(true);
        this.setFocusPainted(true);
    }


    @Override
    public void registerListener() {
        this.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (hoveredIcon!=null) {
                    setIcon(hoveredIcon);
                }
            }

            public void mouseExited(MouseEvent e) {
                if (defaultIcon!=null) {
                    setIcon(defaultIcon);
                }
            }
        });
    }
}
