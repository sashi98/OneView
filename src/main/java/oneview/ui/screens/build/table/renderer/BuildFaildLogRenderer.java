package oneview.ui.screens.build.table.renderer;


import oneview.bean.PomInfo;
import oneview.ui.component.CJPanel;
import oneview.ui.icon.ImagePanel;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.List;

import static oneview.ui.constants.GuiConstants.POM_RUN_FAILURE;
import static oneview.ui.constants.IconConstants.BUILD_FAILED_LOG_IP;
import static oneview.util.GuiUtil.newDummyPanelObj;


public class BuildFaildLogRenderer extends CJPanel implements TableCellRenderer {
    private CJPanel component;

    public BuildFaildLogRenderer() {
        this.setLayout(new FlowLayout(FlowLayout.CENTER,0,0));
        this.setBackground(Color.WHITE);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        List<PomInfo> pomInfoList = (List<PomInfo>) value;

        if (component != null) {
            this.remove(component);
            this.revalidate();
            this.validate();
        }
        component = new CJPanel(new GridLayout(1,pomInfoList.size(),0,0));
        pomInfoList.forEach(pomInfo -> {
            if (pomInfo.getPomRunStatus().equals(POM_RUN_FAILURE)) {
                System.out.println(pomInfo.getArtifactID()+":"+pomInfo.getPomRunStatus());
                ImagePanel ip = BUILD_FAILED_LOG_IP.clone();
                ip.setComponentId(pomInfo.getArtifactID());
                component.add(ip);
            }
        });
        if (isSelected) {
            component.setBackground(Color.LIGHT_GRAY);
        } else {
            component.setBackground(Color.WHITE);
        }
        this.add(component);
        return this;
    }

    public CJPanel getComponent() {
        return component;
    }
}

