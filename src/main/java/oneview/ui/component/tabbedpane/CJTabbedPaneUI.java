package oneview.ui.component.tabbedpane;

import oneview.util.StringUtil;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;

public class CJTabbedPaneUI extends BasicTabbedPaneUI {
    private Color selectColor;
    private Color deSelectColor;
    private int inclTab = 3;
    private int anchoFocoV = inclTab;
    private int anchoFocoH = 4;
    private int anchoCarpetas = 1;
    private Polygon shape;
    private ICON_ALIGN align;
    private HashMap<Integer, Rectangle> iconRectsMap;
    private HashMap<Integer, Rectangle> visibleTabs;
    private int totalTabsWidth = 0;

    public CJTabbedPaneUI(ICON_ALIGN align) {
        this.align = align;
        iconRectsMap = new HashMap<>();
    }


    public enum ICON_ALIGN {
        RIGHT,
        LEFT
    }


    @Override
    protected void installDefaults() {
        super.installDefaults();
        selectColor = new Color(250, 192, 192);
        deSelectColor = new Color(197, 193, 168);
        tabAreaInsets.right = anchoCarpetas;
        textIconGap = 5;
        uninstallListeners();
    }


    @Override
    protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {
        visibleTabs = new HashMap<>();
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (runCount > 1) {
            int lines[] = new int[runCount];
            for (int i = 0; i < runCount; i++) {
                lines[i] = rects[tabRuns[i]].y + (tabPlacement == TOP ? maxTabHeight : 0);
            }
            Arrays.sort(lines);
            if (tabPlacement == TOP) {
                int fila = runCount;
                for (int i = 0; i < lines.length - 1; i++, fila--) {
                    Polygon carp = new Polygon();
                    carp.addPoint(0, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 2, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila, lines[i] + 3);
                    if (i < lines.length - 2) {
                        carp.addPoint(tabPane.getWidth() - 2 * fila, lines[i + 1]);
                        carp.addPoint(0, lines[i + 1]);
                    } else {
                        carp.addPoint(tabPane.getWidth() - 2 * fila, lines[i] + rects[selectedIndex].height);
                        carp.addPoint(0, lines[i] + rects[selectedIndex].height);
                    }
                    carp.addPoint(0, lines[i]);
                    g2d.setColor(hazAlfa(fila));
                    g2d.fillPolygon(carp);
                    g2d.setColor(darkShadow.darker());
                    g2d.drawPolygon(carp);
                }
            } else {
                int fila = 0;
                for (int i = 0; i < lines.length - 1; i++, fila++) {
                    Polygon carp = new Polygon();
                    carp.addPoint(0, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 1, lines[i]);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 1, lines[i + 1] - 3);
                    carp.addPoint(tabPane.getWidth() - 2 * fila - 3, lines[i + 1]);
                    carp.addPoint(0, lines[i + 1]);
                    carp.addPoint(0, lines[i]);
                    g2d.setColor(hazAlfa(fila + 2));
                    g2d.fillPolygon(carp);
                    g2d.setColor(darkShadow.darker());
                    g2d.drawPolygon(carp);
                }
            }
        }
        super.paintTabArea(g2d, tabPlacement, selectedIndex);
    }

    @Override
    protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradientShadow;
        int xp[] = null; // Para la forma
        int yp[] = null;
        switch (tabPlacement) {
            case LEFT:
                xp = new int[]{x, x, x + w, x + w, x};
                yp = new int[]{y, y + h - 3, y + h - 3, y, y};
                gradientShadow = new GradientPaint(x, y, new Color(100, 100, 255), x, y + h, Color.ORANGE);
                break;
            case RIGHT:
                xp = new int[]{x, x, x + w - 2, x + w - 2, x};
                yp = new int[]{y, y + h - 3, y + h - 3, y, y};
                gradientShadow = new GradientPaint(x, y, new Color(100, 100, 255), x, y + h, new Color(153, 186, 243));
                break;
            case BOTTOM:
                xp = new int[]{x, x, x + 3, x + w - inclTab - 6, x + w - inclTab - 2, x + w - inclTab, x + w - 3, x};
                yp = new int[]{y, y + h - 3, y + h, y + h, y + h - 1, y + h - 3, y, y};
                gradientShadow = new GradientPaint(x, y, new Color(100, 100, 255), x, y + h, Color.ORANGE);
                break;
            case TOP:
            default:
                xp = new int[]{x, x, x + 3, x + w - inclTab - 6, x + w - inclTab - 2, x + w - inclTab, x + w - inclTab, x};
                yp = new int[]{y + h, y + 3, y, y, y + 1, y + 3, y + h, y + h};
                gradientShadow = new GradientPaint(0, 0, Color.WHITE, 0, y + h / 1, Color.RED);
                break;
        }
        // ;
        shape = new Polygon(xp, yp, xp.length);
        if (isSelected) {
            g2D.setColor(selectColor);
            g2D.setPaint(gradientShadow);
        } else {
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                g2D.setColor(deSelectColor);
                GradientPaint gradientShadowTmp = new GradientPaint(0, 0, new Color(255, 255, 200), 0, y + h / 2, new Color(240, 255, 210));
                g2D.setPaint(gradientShadowTmp);
            } else {
                GradientPaint gradientShadowTmp = new GradientPaint(0, 0, new Color(240, 255, 210), 0, y + 15 + h / 2, new Color(204, 204, 204));
                g2D.setPaint(gradientShadowTmp);
            }
        }
        //selectColor = new Color(255, 255, 200);
        //deSelectColor = new Color(240, 255, 210);
        g2D.fill(shape);
        if (runCount > 1) {
            g2D.setColor(hazAlfa(getRunForTab(tabPane.getTabCount(), tabIndex) - 1));
            g2D.fill(shape);
        }
        g2D.fill(shape);
    }

    @Override
    protected void paintText(Graphics g, int tabPlacement, Font font, FontMetrics metrics, int tabIndex, String title, Rectangle textRect, boolean isSelected) {
        if (StringUtil.isBlank(title)) {
            return;
        }
        Graphics2D g2D = (Graphics2D) g;
        //g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setFont(font);
        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            // html
            v.paint(g2D, textRect);
        } else {
            // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);
            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                g2D.setColor(tabPane.getForegroundAt(tabIndex));
                BasicGraphicsUtils.drawStringUnderlineCharAt(g2D, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
            } else { // tab disabled
                g2D.setColor(Color.BLACK);
                BasicGraphicsUtils.drawStringUnderlineCharAt(g2D, title, mnemIndex, textRect.x, textRect.y + metrics.getAscent());
                g2D.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                BasicGraphicsUtils.drawStringUnderlineCharAt(g2D, title, mnemIndex, textRect.x - 1, textRect.y + metrics.getAscent() - 1);
            }
        }
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        int w = 0;
        if (tabIndex == tabPane.getTabCount() - 1) {// Calculate w for add tab
            Icon icon = tabPane.getIconAt(tabIndex);
            w = icon == null ? totalTabsWidth : icon.getIconWidth() + 5;//per.calculateTabWidth(tabPlacement, tabIndex, metrics) - 20;
        } else {// Calculate w for normal tab
            w = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
        }
        return w;
    }


    @Override
    protected int calculateTabHeight(int tabPlacement, int tabIndex, int fontHeight) {
        if (tabIndex == tabPane.getTabCount() - 1) {
            return super.calculateTabHeight(tabPlacement, tabIndex, 0);
        }
        if (tabPlacement == LEFT || tabPlacement == RIGHT) {
            return super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        } else {
            return anchoFocoH + super.calculateTabHeight(tabPlacement, tabIndex, fontHeight);
        }
    }

    @Override
    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        super.paintTabBorder(g,tabPlacement,tabIndex,x,y,w,h,isSelected);
    }

    @Override
    protected void paintFocusIndicator(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        Graphics2D g2D = (Graphics2D) g;
        //g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (tabPane.hasFocus() && isSelected) {
            g2D.setColor(UIManager.getColor("ScrollBar.thumbShadow"));
            g2D.drawPolygon(shape);
        }
    }

    protected Color hazAlfa(int fila) {
        int alfa = 0;
        if (fila >= 0) {
            alfa = 50 + (fila > 7 ? 70 : 10 * fila);
        }
        return new Color(0, 0, 0, alfa);
    }

    public static void print(String msg, Object... objects) {
        final String regex = "\\{}";
        for (Object object : objects) {
            msg = msg.replaceFirst(regex, object.toString());
        }
        System.out.println(msg);
    }

    @Override
    protected void layoutLabel(int tabPlacement, FontMetrics metrics, int tabIndex, String title, Icon icon, Rectangle tabRect, Rectangle iconRect, Rectangle textRect, boolean isSelected) {
        if (align == ICON_ALIGN.LEFT) {
            super.layoutLabel(tabPlacement, metrics, tabIndex, title, icon, tabRect, iconRect, textRect, isSelected);
        }
        textRect.x = textRect.y = iconRect.x = iconRect.y = 0;
        View v = getTextViewForTab(tabIndex);
        if (v != null) {
            tabPane.putClientProperty("html", v);
        }

        if (StringUtil.isBlank(title)) {
            SwingUtilities.layoutCompoundLabel(tabPane,
                    metrics, title, icon,
                    SwingUtilities.CENTER,
                    SwingUtilities.LEFT,
                    SwingUtilities.CENTER,
                    SwingUtilities.RIGHT,
                    tabRect,
                    iconRect,
                    textRect,
                    textIconGap);
        } else {
            SwingUtilities.layoutCompoundLabel(tabPane,
                    metrics, title, icon,
                    SwingUtilities.CENTER,
                    SwingUtilities.CENTER,
                    SwingUtilities.CENTER,
                    SwingUtilities.LEFT,
                    tabRect,
                    iconRect,
                    textRect,
                    textIconGap);
        }
        tabPane.putClientProperty("html", null);

        int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
        int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
        iconRect.x += xNudge + 5;
        iconRect.y += yNudge;
        textRect.x += xNudge;
        textRect.y += yNudge;
    }

    @Override
    protected void paintIcon(Graphics g, int tabPlacement, int tabIndex, Icon icon, Rectangle iconRect, boolean isSelected) {
        if (icon != null) {
            icon.paintIcon(tabPane, g, iconRect.x, iconRect.y);
            iconRectsMap.put(tabIndex, new Rectangle(iconRect));
        }
    }

    public HashMap<Integer, Rectangle> getIconRectsMap() {
        return iconRectsMap;
    }


    @Override
    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        int selectedIndex = tabPane.getSelectedIndex();
        boolean isSelected = selectedIndex == tabIndex;
        visibleTabs.put(tabIndex,rects[tabIndex]);
        if (!StringUtil.isBlank(tabPane.getTitleAt(tabIndex))) {
            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
        } else {
            FontMetrics metrics = SwingUtilities2.getFontMetrics(tabPane, g, tabPane.getFont());
            Icon icon = getIconForTab(tabIndex);

            layoutLabel(tabPlacement, metrics, tabIndex, null, icon,
                    rects[tabIndex], iconRect, textRect, isSelected);
            paintText(g, tabPlacement, tabPane.getFont(), metrics, tabIndex, null, textRect, isSelected);
            paintIcon(g, tabPlacement, tabIndex, getIconForTab(tabIndex), iconRect, isSelected);
        }
    }

    @Override
    public int getTabRunCount(JTabbedPane pane) {
        return super.getTabRunCount(pane);
    }

    @Override
    public int getRunForTab(int tabCount, int tabIndex) {
        return super.getRunForTab(tabCount, tabIndex);
    }

    public Rectangle[] getRects() {
        return rects;
    }

    public HashMap<Integer, Rectangle> getVisibleTabs() {
        return visibleTabs;
    }
}
