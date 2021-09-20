package oneview.ui.component;

import oneview.ui.ContextListener;

import javax.swing.*;
import javax.swing.border.AbstractBorder;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static oneview.ui.constants.DimensionConstants.TEXT_FIELD_HEIGHT;
import static oneview.util.GuiUtil.newDimentionObj;

public class CJPanel extends JPanel{
    protected ContextListenerSupport contextListenerSupport;
    private boolean doGradientPaint;
    private Color color1;
    private Color color2;
    private String componentId;

    public CJPanel(){
        super();
        contextListenerSupport = new ContextListenerSupport();
        this.setBackground(Color.getColor("#ADD8E6"));
    }

    public CJPanel(int w, int h) {
        this();
        this.setLayout(null);
        this.setPreferredSize(newDimentionObj(w,h));
    }

    public CJPanel(LayoutManager layoutManager) {
        this();
        this.setLayout(layoutManager);

    }

    public CJPanel(LayoutManager layoutManager, Color bgc) {
        this(layoutManager);
        this.setBackground(bgc);
    }

    public CJPanel(JComponent jComponent, Color bgc) {
        this();
        if (jComponent != null) {
            this.add(jComponent);
        }
        if (bgc !=null) {
            this.setBackground(bgc);
        }
    }

    public CJPanel(AbstractBorder border) {
        this.setBorder(border);
        this.setBackground(Color.getColor("#ADD8E6"));

    }

    public CJPanel(boolean doGradientPaint){
        this.doGradientPaint = doGradientPaint;
        this.setBackground(Color.getColor("#ADD8E6"));

    }

    public CJPanel(JComponent jComponent, boolean doGradientPaint, Color c1, Color c2) {
        this(jComponent, (Color) null);
        this.doGradientPaint = doGradientPaint;
        color1 = c1;
        color2 = c2;
    }

    public CJPanel(boolean doGradientPaint, Color c1, Color c2) {
        this(null, doGradientPaint,c1,c2);
    }

    public CJPanel(CJLabel label, JComponent c) {
        label.setPreferredSize(newDimentionObj(100, TEXT_FIELD_HEIGHT));
        this.setLayout(new BorderLayout(0,0));
        this.add(label, BorderLayout.WEST);
        this.add(c, BorderLayout.CENTER);
    }

    public CJPanel(JComponent component) {
        this.add(component);
    }

    public CJPanel(JComponent component, LayoutManager layoutManager) {
        this(layoutManager);
        this.add(component);
    }

    public CJPanel(CJLabel label, JComponent component, LayoutManager layoutManager) {
        label.setPreferredSize(newDimentionObj(100, TEXT_FIELD_HEIGHT));
        this.setLayout(layoutManager);
        this.add(label);
        this.add(component);
    }

    public CJPanel(Color bg) {
        this.setBackground(bg);
    }

    protected class ContextListenerSupport{
        private List<ContextListener> listeners;

        public void add(ContextListener cl) {
            if (listeners == null){
                listeners = new ArrayList<>();
            }
            listeners.add(cl);
        }

        public void fire() {
            listeners.forEach(cl->cl.contextUpdate());
        }
    }

    public ContextListenerSupport getContextListenerSupport() {
        return contextListenerSupport;
    }

    public void addContextListener(ContextListener cl) {
        contextListenerSupport.add(cl);
    }


    public void fireContextListener(){
        contextListenerSupport.fire();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(doGradientPaint) {
            paintGradientPaint(g);
        }
    }

    private void paintGradientPaint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h/2, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }

    public boolean isDoGradientPaint() {
        return doGradientPaint;
    }

    public void setDoGradientPaint(boolean doGradientPaint) {
        this.doGradientPaint = doGradientPaint;
    }

    public Color getColor1() {
        return color1;
    }

    public void setColor1(Color color1) {
        this.color1 = color1;
    }

    public Color getColor2() {
        return color2;
    }

    public void setColor2(Color color2) {
        this.color2 = color2;
    }

    public String getComponentId() {
        return componentId;
    }

    public void setComponentId(String componentId) {
        this.componentId = componentId;
    }
}
