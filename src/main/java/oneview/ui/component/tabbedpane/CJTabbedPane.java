package oneview.ui.component.tabbedpane;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJPanel;
import oneview.util.StringUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.*;
import java.util.List;

import static oneview.ui.constants.GuiConstants.NORMAL_TXT_FONT;
import static oneview.ui.constants.IconConstants.*;

public class CJTabbedPane extends JTabbedPane implements RegisterListener {
    private List<CJTabbedPaneModel> tabbedPaneModelList;

    public CJTabbedPane() {
        super();
        this.tabbedPaneModelList = new ArrayList<>();
    }

    public CJTabbedPane(CJTabbedPaneUI ui) {
        super();
        this.tabbedPaneModelList = new ArrayList<>();
        this.setUI(ui);
        this.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        this.setFocusable(true);
        this.setFont(NORMAL_TXT_FONT);
        CJTabbedPaneModel addTabBean = new CJTabbedPaneModel(null, null, "Add New Tab");
        tabbedPaneModelList.add(addTabBean);
//        this.addTab(defaultTabBean.getTitle(), defaultTabBean.getComponent(), defaultTabBean.getToolTipText());
        this.add((String) null, null);
        registerListener();
        setFocusable(false);
        setBackground(Color.WHITE);
    }

    private Rectangle[] getAllTabs() {
        return ((CJTabbedPaneUI) getUI()).getRects();
    }


    private boolean isAddTab() {
        return (getTabCount() - 1) == getSelectedIndex();
    }


    private boolean isTitleBlankAtSelectedTab() {
        if (getSelectedIndex() < 0) return false;
        String str = getTitleAt(getSelectedIndex());
        return str == null || "".equals(str.trim()) ? true : false;
    }

    private boolean isMouseInCloseIconArea(Point p, Rectangle irect) {
        if (irect == null) return false;
        return irect.contains(p);
    }

    @Override
    public void addTab(String title, Component component) {
        addTab(title, component, title);
    }

    public void addTab(String title, Component component, String toolTipTxt) {
        super.addTab(title, component);
        this.setToolTipText(toolTipTxt);
    }

    @Override
    public Component add(String title, Component component) {
        int lastTabIndex = getTabCount();
        if (tabbedPaneModelList.size() == 1 && StringUtil.isBlank(title) && component == null) {
            addTab(null, null);
            this.setIconAt(lastTabIndex, ADD_TAB_DEFAULT_ICON);
            this.setToolTipTextAt(lastTabIndex, title);
        } else {
            add(component, lastTabIndex - 1);
            lastTabIndex = getTabCount();
            this.setTitleAt(lastTabIndex - 2, title);
            this.setIconAt(lastTabIndex - 2, CLOSE_TAB_DEFAULT_ICON);
            this.setToolTipTextAt(lastTabIndex - 2, title);
            tabbedPaneModelList.add(lastTabIndex - 2, new CJTabbedPaneModel(title, component));
            component.setName(title);
            ((CJPanel) component).fireContextListener();
        }
        this.setSelectedIndex(0);
        return component;
    }
//
//    private Component insertTabAtBeforeAddTab() {
//        removeAll();
//        AtomicReference<Component> c = new AtomicReference<>();
//        tabbedPaneModelList.forEach(tb -> {
//            if (StringUtil.isBlank(tb.getTitle())) {
//                addTab(null, null);
//                this.setToolTipTextAt(getTabCount() - 1, tb.getToolTipText());
//                this.setIconAt(getTabCount() - 1, ADD_TAB_DEFAULT_ICON);
//            } else {
//                c.set(super.add(tb.getComponent()));
//                this.setTitleAt(getTabCount() - 1, tb.getTitle());
//                this.setIconAt(getTabCount() - 1, CLOSE_TAB_DEFAULT_ICON);
//                this.setToolTipTextAt(getTabCount() - 1, tb.getToolTipText());
//            }
//
//        });
//        this.setSelectedIndex(getTabCount() - 2);
//        return c.get();
//    }

    private boolean isAddIconClicked(Rectangle[] tabRects, Point p, Rectangle irect) {
        boolean clicked = isIconClicked(tabRects, p, irect);
        return clicked;
    }

    public boolean isCloseIconClicked(Rectangle[] tabRects, Point p, Rectangle irect) {
        boolean clicked = isIconClicked(tabRects, p, irect);
        return clicked;
    }

    private boolean isIconClicked(Rectangle[] tabRects, Point clickPoint, Rectangle actualIconRect) {
        int totalTabWidth = getTotalTabWidth(tabRects, 0, tabRects.length);
        int subTotal = 0;
        subTotal = getTotalTabWidth(getVisibleTabs(), 0, getVisibleTabs().length);

        int shift = totalTabWidth - subTotal;
        Point actualClickPoint = new Point(clickPoint.x + shift, clickPoint.y);
        return actualIconRect.contains(actualClickPoint);
    }

    private int getTotalTabWidth(Rectangle[] tabRects, int i, int n) {
        int totalTabWidth = 0;
        for (int k = i; k < n; k++) {
            totalTabWidth += tabRects[k].width;
        }
        return totalTabWidth;
    }

    private Rectangle getIconRectAt(int index) {
        return ((CJTabbedPaneUI) CJTabbedPane.this.getUI()).getIconRectsMap().get(index);
    }


    public static void printLog(String msg, Object... objects) {
        final String regex = "\\{}";
        for (Object object : objects) {
            msg = msg.replaceFirst(regex, String.valueOf(object));
        }
        System.out.println(msg);
    }

//    public void setTitleAt(int selectedIndex, String title) {
//        if (selectedIndex < 0 || selectedIndex > this.tabbedPaneModelList.size()) {
//            return;
//        }
//        CJTabbedPaneModel tb = this.tabbedPaneModelList.get(selectedIndex);
//        tb.setTitle(title);
//        //((CJPanel) this.getComponentAt(selectedIndex)).updateName(title);
//        super.setTitleAt(selectedIndex, title);
//    }

    @Override
    public int getTabRunCount() {
        return getUI().getTabRunCount(this);
    }


    private Rectangle[] getVisibleTabs() {
        Map<Integer, Rectangle> visibleTabs = ((CJTabbedPaneUI) CJTabbedPane.this.getUI()).getVisibleTabs();
        Rectangle[] rectangles = new Rectangle[visibleTabs.size()];
        int i = 0;
        for (Map.Entry<Integer, Rectangle> t : visibleTabs.entrySet()) {
            rectangles[i] = t.getValue();
            i++;
        }
        return rectangles;
    }

    @Override
    public void registerListener() {
        registerMouseListener();
        registerMouseMotionListener();
    }


    private void registerMouseMotionListener() {
        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {

            }

            @Override
            public void mouseMoved(MouseEvent e) {
                if (getSelectedIndex() < 0 || getSelectedIndex() == getTabCount() - 1) return;

                if (isMouseInCloseIconArea(e.getPoint(), ((CJTabbedPaneUI) CJTabbedPane.this.getUI()).getIconRectsMap().get(getSelectedIndex()))) {
                    setIconAt(getSelectedIndex(), CLOSE_TAB_HOVERED_ICON);
                } else {
                    setIconAt(getSelectedIndex(), CLOSE_TAB_DEFAULT_ICON);
                }

            }
        });
    }

    private void registerMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                Rectangle[] tabRects = getAllTabs();
                int totalVisibleTabsWidth = getTotalTabWidth(getVisibleTabs(), 0, getVisibleTabs().length);

                if (tabRects == null || tabRects.length == 0 || clickPoint.y > (tabRects[0].y + tabRects[0].height) || clickPoint.x > totalVisibleTabsWidth) {
                    return;
                }
                int clicks = e.getClickCount();
                if (clicks == 1) {
                    if ((getSelectedIndex() < (getTabCount() - 1)) && isCloseIconClicked(tabRects, clickPoint, getIconRectAt(getSelectedIndex()))) {
                        if (getTabCount() > 2) {
                            removeTabAt(getSelectedIndex());
                            tabbedPaneModelList.remove(getSelectedIndex());
                            setSelectedIndex(getTabCount() - 2);
                        }
                    } else if (getSelectedIndex() == getTabCount() - 1) {
                        try {
                            add("New Tab" + (tabbedPaneModelList.size() - 1), tabbedPaneModelList.get(tabbedPaneModelList.size() - 2).getComponent().getClass().newInstance());
                            setSelectedIndex(getTabCount() - 2);
                        } catch (InstantiationException ex) {
                            ex.printStackTrace();
                        } catch (IllegalAccessException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else if (clicks == 2 && !isAddTab()) {
                    String title = JOptionPane.showInputDialog(getComponentAt(e.getPoint()), "Set title");
                    if (StringUtil.isNotBlank(title)) {
                        setTitleAt(getSelectedIndex(), title);
                    }
                }

            }

        });
    }
}
