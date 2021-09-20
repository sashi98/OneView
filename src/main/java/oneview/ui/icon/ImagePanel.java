package oneview.ui.icon;

import oneview.ui.component.CJPanel;
import oneview.util.ResourceLoader;

import javax.swing.*;
import java.awt.*;

import static oneview.util.GuiUtil.newDimentionObj;
import static java.awt.Image.SCALE_SMOOTH;

public class ImagePanel extends CJPanel {
    private Image image;
    private ImageIcon imageIcon;
    private int w;
    private int h;
    private boolean scaleNeeded;
    private Color bg;
    private String icon;

    public ImagePanel() {
        super();
        bg = Color.WHITE;
        setBackground(bg);
    }
    public ImagePanel(Color bg) {
        super();
        bg = Color.WHITE;
        setBackground(bg);
    }
    public ImagePanel(int w, int h, boolean scaleNeeded, Color bg, String icon) {
        this.w = w;
        this.h = h;
        this.scaleNeeded = scaleNeeded;
        this.bg = bg;
        this.icon = icon;
        init();
    }

    private void init(){
        if (bg != null){
            setBackground(bg);
        }
        image = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getResource(icon).getPath());
        imageIcon = new ImageIcon(image);
        if (scaleNeeded) {
            imageIcon.setImage(imageIcon.getImage().getScaledInstance(w, h, SCALE_SMOOTH));
        }
        setPreferredSize(newDimentionObj(w,h));
    }

    public ImagePanel(String icon) {
        this.icon = icon;
        this.image = Toolkit.getDefaultToolkit().createImage(ResourceLoader.getResource(icon));
        this.imageIcon = new ImageIcon(image);
        setPreferredSize(newDimentionObj(imageIcon.getIconWidth(), imageIcon.getIconHeight()));
    }

    public ImagePanel(String icon, String toolTipText) {
        this(icon);
        setToolTipText(toolTipText);

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(imageIcon.getImage(), getWidth()/2-imageIcon.getIconWidth()/2, getHeight()/2-imageIcon.getIconHeight()/2, this);
        }
    }

    @Override
    public ImagePanel clone() {
        if(w>0 &&  h>0){
            return new ImagePanel(w,h,scaleNeeded,bg,icon);
        }
        return new ImagePanel(this.icon);
    }
}