package oneview.ui.component.tabbedpane;

import oneview.util.StringUtil;

import sun.swing.SwingUtilities2;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.HashMap;

public class CJTabbedPaneUI1 extends BasicTabbedPaneUI {

    private ICON_ALIGN align;
    private HashMap<Integer, Rectangle> iconRectsMap;
    public CJTabbedPaneUI1(ICON_ALIGN align) {
        this.align = align;
        iconRectsMap = new HashMap<>();
    }

    public enum ICON_ALIGN {
        RIGHT,
        LEFT
    }

    @Override
    protected int calculateTabWidth(int tabPlacement, int tabIndex, FontMetrics metrics) {
        if (tabIndex == tabPane.getTabCount()-1){
            return super.calculateTabWidth(tabPlacement, tabIndex, metrics)-20;
        }
        return super.calculateTabWidth(tabPlacement, tabIndex, metrics);
    }

//    public static void print(String msg, Object... objects) {
//        final String regex = "\\{}";
//        for (Object object : objects) {
//            msg = msg.replaceFirst(regex, object.toString());
//        }
//        System.out.println(msg);
//    }

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

        if (StringUtil.isBlank(title)){
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
        iconRectsMap.put(tabIndex, iconRect);
        if (icon != null) {
            icon.paintIcon(tabPane, g, iconRect.x, iconRect.y);
        }
    }

    public HashMap<Integer, Rectangle> getIconRectsMap() {
        return iconRectsMap;
    }

    public void setIconRectsMap(HashMap<Integer, Rectangle> iconRectsMap) {
        this.iconRectsMap = iconRectsMap;
    }

    @Override
    protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect, Rectangle textRect) {
        int selectedIndex = tabPane.getSelectedIndex();
        boolean isSelected = selectedIndex == tabIndex;

        if (!StringUtil.isBlank(tabPane.getTitleAt(tabIndex))){
            super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
        } else {
            FontMetrics metrics = SwingUtilities2.getFontMetrics(tabPane, g, tabPane.getFont());
            Icon icon = getIconForTab(tabIndex);

            layoutLabel(tabPlacement, metrics, tabIndex, null, icon,
                    rects[tabIndex], iconRect, textRect, isSelected);
            paintText(g, tabPlacement, tabPane.getFont(), metrics, tabIndex, null, textRect, isSelected);
            paintIcon(g,tabPlacement,tabIndex,getIconForTab(tabIndex),iconRect, isSelected);
        }
    }
}
