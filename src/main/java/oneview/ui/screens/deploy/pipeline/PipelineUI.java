package oneview.ui.screens.deploy.pipeline;

import oneview.karaf.PipelineUnit;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJPanel;
import oneview.ui.component.CJPopupTextView;
import oneview.ui.screens.deploy.pipeline.table.PipelineTable;
import oneview.ui.screens.deploy.pipeline.table.PipelineTableData;
import oneview.ui.screens.deploy.pipeline.table.PipelineTableDataModel;
import oneview.ui.screens.deploy.pipeline.table.renderer.PipelineCellRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import static oneview.util.GuiUtil.newDimentionObj;

public class PipelineUI extends CJPanel implements RegisterListener {
    private PipelineTable table;
    private PipelineTableDataModel model;
    private JTextArea logArea;

    public PipelineUI(int w, int h, List<PipelineUnit> pipeline) {
        super(w,h);
        this.setLayout(new BorderLayout(0,0));
        model = new PipelineTableDataModel(pipeline);
        table = new PipelineTable(model);
        logArea = new JTextArea();

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setLayout(new ScrollPaneLayout());
        tableScrollPane.setPreferredSize(newDimentionObj(w,((h/5)+8)));
        this.add(tableScrollPane, BorderLayout.NORTH);

        JScrollPane logAreaScrollPane = new JScrollPane(logArea);
        logAreaScrollPane.setLayout(new ScrollPaneLayout());
        this.add(logAreaScrollPane, BorderLayout.CENTER);

        registerListener();
    }

    @Override
    public void registerListener() {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int row = table.rowAtPoint(clickPoint);
                int col = table.columnAtPoint(clickPoint);
                Rectangle cellRect = table.getCellRect(row, col, true);
                performLogIconClick(cellRect,clickPoint,row, col);
            }
        });
    }

    private void performLogIconClick(Rectangle cellRect, Point clickPoint, int row, int col) {
        PipelineCellRenderer cellRenderer = (PipelineCellRenderer) table.getCellRenderer(row,col);
        JComponent ip = cellRenderer.getComponent();
        Rectangle ipRect = ip.getBounds();
        Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
        if (ipBounds.contains(clickPoint)) {
            logArea.setText(((PipelineTableDataModel)table.getModel()).getLog(row,col));
        }
    }
}
