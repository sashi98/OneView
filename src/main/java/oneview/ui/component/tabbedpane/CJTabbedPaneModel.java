package oneview.ui.component.tabbedpane;

import java.awt.*;

public class CJTabbedPaneModel {
    private String title;
    private Component component;
    private String toolTipText;

    public CJTabbedPaneModel(String title, Component component) {
        this(title, component, title);
    }

    public CJTabbedPaneModel(String title, Component component, String toolTipText) {
        this.title = title;
        this.component = component;
        this.toolTipText = toolTipText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getToolTipText() {
        return toolTipText;
    }

    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }

    @Override
    public String toString() {
        return "TabBean{" +
                "title='" + title + '\'' +
                ", tab=" + ((component != null) ? component.toString() : null) +
                '}';
    }
}
