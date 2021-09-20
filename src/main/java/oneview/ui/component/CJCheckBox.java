package oneview.ui.component;

import oneview.util.FileUtil;
import oneview.util.StringUtil;

import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.Image.SCALE_SMOOTH;

public class CJCheckBox extends JCheckBox {
    private ImageIcon onIcon, offIcon;

    public CJCheckBox(ImageIcon onIcon, ImageIcon offIcon, int iconWidth, int iconHeight, String toolTipTxt){
        super();
        this.onIcon = FileUtil.getScaledIcon(onIcon, iconWidth, iconHeight, SCALE_SMOOTH);
        this.offIcon = FileUtil.getScaledIcon(offIcon, iconWidth, iconHeight, SCALE_SMOOTH);
        this.setBorderPainted(false);
        this.setContentAreaFilled(false);
        this.setFocusPainted(false);
        setIcon(this.offIcon);
        if (StringUtil.isNotBlank(toolTipTxt)) {
            this.setToolTipText(toolTipTxt);
        }
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (isSelected()) {
                    setIcon(onIcon);
                } else {
                    setIcon(offIcon);
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (isSelected()) {
                    setIcon(onIcon);
                } else {
                    setIcon(offIcon);
                }
            }
        });
    }

    public CJCheckBox() {
        super();
    }
}
