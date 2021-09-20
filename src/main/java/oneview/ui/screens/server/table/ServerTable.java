package oneview.ui.screens.server.table;

import oneview.ui.screens.common.context.OneViewContext;
import oneview.ui.screens.server.context.ServerContext;
import oneview.script.ScriptRunner;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJTable;
import oneview.ui.screens.server.table.renderer.RestartCellRenderer;
import oneview.ui.screens.server.table.renderer.StartStopCellRenderer;
import oneview.ui.screens.common.table.renderer.*;
import oneview.util.DateUtil;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicReference;

import static oneview.script.ScriptRunner.EXECUTING;
import static oneview.ui.constants.GuiConstants.*;
import static oneview.ui.screens.server.ServersScreen.NEW_SERVER_ADDED;
import static oneview.util.DatePatterns.DP_YYYY_MM_DD_HH_MM_SS;
import static oneview.util.ThreadUtil.sleep;

public class



ServerTable extends CJTable implements RegisterListener {
    private ServerTableDataModel serverTableDataModel;
    private StartStopCellRenderer startStopCellRenderer;
    private TimeCellRenderer timeCellRenderer;
    private RestartCellRenderer restartCellRenderer;
    private SortingColumnHeaderRenderer sortingColumnHeaderRenderer;
    private ColumnHeaderRenderer columnHeaderRenderer;
    private int defaultSortingColumn = 1;
    private boolean DESC = true;
    private ScriptRunner scriptRunner;

    public ServerTable(ServerTableDataModel dataModel) {
        super(dataModel);
        this.serverTableDataModel = dataModel;
        this.sortingColumnHeaderRenderer = new SortingColumnHeaderRenderer(DESC);
        this.columnHeaderRenderer = new ColumnHeaderRenderer();
        this.startStopCellRenderer = new StartStopCellRenderer();
        this.restartCellRenderer = new RestartCellRenderer();
        this.timeCellRenderer = new TimeCellRenderer();
        getTableHeader().setPreferredSize(new Dimension(100, 25));
        setRowSelectionAllowed(false);
        setSelectionBackground(Color.white);
        setCellRenderer();
        setHeaderRenderer();
        registerListener();
        setRowHeight(30);

        scriptRunner = new ScriptRunner();
    }

    @Override
    public CJTable getInvoker() {
        return this;
    }

    private void setCellRenderer() {
        NormalCellRenderer defaultTableCellRenderer = new NormalCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        defaultTableCellRenderer.setBorder(new LineBorder(Color.BLACK));

        getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.SERVER_NAME_INDEX.ordinal()).setCellRenderer(defaultTableCellRenderer);
        getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.START_TIME_INDEX.ordinal()).setCellRenderer(timeCellRenderer);
        getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.STOP_TIME_INDEX.ordinal()).setCellRenderer(timeCellRenderer);
        getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.START_STOP_INDEX.ordinal()).setCellRenderer(startStopCellRenderer);
        getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.RESTART_INDEX.ordinal()).setCellRenderer(restartCellRenderer);

    }

    private void setHeaderRenderer() {
        Enumeration<TableColumn> tableColumns = this.getTableHeader().getColumnModel().getColumns();
        while (tableColumns.hasMoreElements()) {
            TableColumn tc = tableColumns.nextElement();
            if (tc.getModelIndex() == getDefaultSortingColumn()) {
                tc.setHeaderRenderer(sortingColumnHeaderRenderer);
            } else {
                tc.setHeaderRenderer(columnHeaderRenderer);
            }
        }
    }

    public ServerTableDataModel getServerTableDataModel() {
        return serverTableDataModel;
    }

    @Override
    public void updateTable(OneViewContext context) {
        final ServerTable serverTable = this;
        if (context instanceof ServerContext) {
            ServerContext serverContext = (ServerContext) context;
            AtomicReference<String> msg = new AtomicReference<>("");
            ServerTableData data = (ServerTableData) serverContext.getObject(NEW_SERVER_ADDED);
            checkServerHealth(data, serverTable);

            serverTableDataModel.getDataList().add(data);
            serverTableDataModel.fireTableDataChanged();
            serverTable.repaint();
        }
    }


    private void sort() {

    }

    public int getDefaultSortingColumn() {
        return defaultSortingColumn;
    }

    public void setDefaultSortingColumn(int defaultSortingColumn) {
        this.defaultSortingColumn = defaultSortingColumn;
    }

    public void clearTable() {
        serverTableDataModel.getDataList().removeAll(serverTableDataModel.getDataList());
        serverTableDataModel.fireTableDataChanged();
    }

    public void sortingPerformed(MouseEvent e) {
        DESC = !DESC;
        sortingColumnHeaderRenderer.setDesc(DESC);
        TableColumnModel colModel = ((JTableHeader) e.getSource()).getColumnModel();
        int columnModelIndex = colModel.getColumnIndexAtX(e.getX());
        setDefaultSortingColumn(columnModelIndex);
        setHeaderRenderer();
        sort();
    }

    @Override
    public void registerListener() {
        final ServerTable serverTable = this;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                int row = rowAtPoint(clickPoint);
                int col = columnAtPoint(clickPoint);
                Rectangle cellRect = getCellRect(row, col, true);
                if (col == ServerTableDataModel.ColumnIndex.START_STOP_INDEX.ordinal()) {
                    StartStopCellRenderer cellRenderer = (StartStopCellRenderer) getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.START_STOP_INDEX.ordinal()).getCellRenderer();
                    JComponent ip = cellRenderer.getComponent();
                    Rectangle ipRect = ip.getBounds();
                    Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
                    if (ipBounds.contains(clickPoint)) {
                        ServerTableData data = serverTableDataModel.getDataList().get(row);
                        data.setStartStop(BUSY_SPIN);
                        serverTableDataModel.fireTableDataChanged();
                        new Thread(() -> {
                            if (data.getServerStatus().equals(STOPPED)) {
                                startServer(data, serverTable);
                            } else {
                                stopServer(data, serverTable);
                            }
                            serverTableDataModel.fireTableDataChanged();
                        }).start();

                    }
                }
                if (col == ServerTableDataModel.ColumnIndex.RESTART_INDEX.ordinal()) {
                    RestartCellRenderer cellRenderer = (RestartCellRenderer) getColumnModel().getColumn(ServerTableDataModel.ColumnIndex.RESTART_INDEX.ordinal()).getCellRenderer();
                    JComponent ip = cellRenderer.getComponent();
                    Rectangle ipRect = ip.getBounds();
                    Rectangle ipBounds = new Rectangle(cellRect.x + ipRect.x, cellRect.y + ipRect.y, ipRect.width, ipRect.height);
                    if (ipBounds.contains(clickPoint)) {
                        ServerTableData data = serverTableDataModel.getDataList().get(row);
                        data.setRestart(RESTARTING);
                        serverTableDataModel.fireTableDataChanged();
                        new Thread(() -> {
                            restartServer(data, serverTable);
                            serverTableDataModel.fireTableDataChanged();
                        }).start();

                    }
                }
                serverTable.repaint();
            }

        });

        getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                sortingPerformed(e);
            }
        });
    }

    private void restartServer(ServerTableData data, ServerTable serverTable) {
        stopServer(data, serverTable);
        data.setRestart(RESTARTING);
        serverTable.waitFor(3000);
        startServer(data, serverTable);
    }


    private void stopServer(ServerTableData data, ServerTable serverTable) {
        String[] cmd = new String[]{"sh", data.getStopScriptPath()};
        System.out.println("CURRENT COMMAND GOING TO RUN:" + data.getStopScriptPath());
        serverTable.getServerTableDataModel().fireTableDataChanged();

        Process process = scriptRunner.runShellScript(cmd);

        new Thread(() -> {
            System.out.println("COMMAND STATUS " + scriptRunner.getStatus());
            while (scriptRunner.getStatus().equals(EXECUTING)) {
                serverTable.waitFor(2000);
            }
            serverTable.waitFor(5000);
            checkServerHealth(data, serverTable);
        }).start();
    }

    private void startServer(ServerTableData data, ServerTable serverTable) {
        String[] cmd = new String[]{"sh", data.getStartScriptPath()};
        System.out.println("CURRENT COMMAND GOING TO RUN:" + data.getStartScriptPath());
        serverTable.getServerTableDataModel().fireTableDataChanged();

        Process process = scriptRunner.runShellScript(cmd);

        new Thread(() -> {
            System.out.println("COMMAND STATUS " + scriptRunner.getStatus());
            while (scriptRunner.getStatus().equals(EXECUTING)) {
                serverTable.waitFor(4000);
            }
            //String output = scriptRunner.captureOutput(process);
            checkServerHealth(data, serverTable);
        }).start();
    }

    public void waitFor(long timeout) {
        while (timeout >= 0) {
            sleep(100);
            timeout -= 100;
            repaint();
        }
    }

    private void checkServerHealth(ServerTableData data, final ServerTable serverTable) {
        String cmd = "ps aux |grep java |grep " + data.getServerHome();
        System.out.println("CURRENT COMMAND GOING TO RUN:" + cmd);
        serverTable.getServerTableDataModel().fireTableDataChanged();

        Process process = scriptRunner.runShellScript(new String[]{"sh", "-c", cmd});

        new Thread(() -> {
            System.out.println("COMMAND STATUS " + scriptRunner.getStatus());
            while (scriptRunner.getStatus().equals(EXECUTING)) {
                serverTable.waitFor(2000);
            }
            String output = scriptRunner.captureGrepOutput(process, cmd);
            System.out.println("OUTPUT: " + output);
            if (!output.equals("")) {
                setServerDataForStarting(data);
            } else {
                setServerDataForStopping(data);
            }
        }).start();
    }

    private void setServerDataForStarting(ServerTableData data) {
        data.setStartTime(DateUtil.getCurrentDateObj(DP_YYYY_MM_DD_HH_MM_SS));
        data.setStopTime(null);
        data.setStartStop(STARTED);
        data.setServerStatus(STARTED);
        data.setRestart(RESTARTED);
        serverTableDataModel.fireTableDataChanged();
    }

    private void setServerDataForStopping(ServerTableData data) {
        data.setStartTime(null);
        data.setStopTime(DateUtil.getCurrentDateObj(DP_YYYY_MM_DD_HH_MM_SS));
        data.setStartStop(STOPPED);
        data.setServerStatus(STOPPED);
        data.setRestart(RESTART);
        serverTableDataModel.fireTableDataChanged();

    }
}


/*

        new Thread(()->{
            System.out.println("checkServerHealth: "+ scriptRunner.getStatus());
            while (scriptRunner.getStatus().equals(EXECUTING)){
                sleep(100);
                serverTable.repaint();
            }
            System.out.println("Hiii:::"+msg);
        }).start();

System.out.println(msg);
        if (msg.contains("SCRIPT_FAILURE")){
            JOptionPane.showMessageDialog(serverTable, msg, "Dialog", JOptionPane.ERROR_MESSAGE);
            setServerDataForStopping(data);
            return;
        }
        if (msg.equals("RUNNING")){
            setServerDataForStarting(data);
        } else {
            setServerDataForStopping(data);
        }
 */