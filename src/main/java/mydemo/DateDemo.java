package mydemo;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import oneview.ui.screens.jobtrigger.context.ESContext;
import oneview.ui.component.CJPanel;
import oneview.ui.screens.jobtrigger.table.TriggerLogTableUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static oneview.ui.constants.IconConstants.*;
import static oneview.util.GuiUtil.newDimentionObj;
import static oneview.util.GuiUtil.newDummyPanelObj;

public class DateDemo extends JFrame  {
    private final static int WIDTH = 700;
    private final static int HEIGHT = 500;
    TriggerLogTableUI tableUI;
    DateDemo() {
        super("Deployment Util GUI");
    }

    public static void main(String[] args) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        DateDemo mainGUI = new DateDemo();
        createWindow(mainGUI);
    }

    private static void createWindow(DateDemo mainGUI) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        mainGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainGUI.createUI();
        //mainGUI.setDataToTable();
    }

    private void setDataToTable() {
        ESContext md = new ESContext();
//        md.setEndDateTime("2020-07-31 01:11:56.000");
//        md.setTriggerFileName("1");
//        md.setStartDateTime("2020-07-31 01:11:56.000");
//        md.setCronJobName("member-medicaid-id-card");
        new Thread(new Runnable() {
            @Override
            public void run() {
                tableUI.getTriggerLogTable().updateTable(md);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                tableUI.getTriggerLogTable().updateTable(md);
            }
        }).start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                tableUI.getTriggerLogTable().updateTable(md);
            }
        }).start();
    }


    private void createUI() throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        this.setLayout(new BorderLayout(0, 0));
        CJPanel panel = newDummyPanelObj(new FlowLayout());
        panel.setBorder(new LineBorder(Color.GREEN,5));
        panel.add(createDateUI());
        //panel.add(createTableUI());
        this.add(panel, BorderLayout.CENTER);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setVisible(true);
        //this.pack();
    }



    private Component createDateUI() {
        DatePicker datePicker = new DatePicker();
        DatePickerSettings settings = datePicker.getSettings().copySettings();
        settings.setFormatForDatesCommonEra("dd/MM/yyyy");
        datePicker.setSettings(settings);
        int h = 20;
        datePicker.getComponentDateTextField().setPreferredSize(newDimentionObj(300, h));
        JButton button = datePicker.getComponentToggleCalendarButton();
        button.setPreferredSize(newDimentionObj(10,h));
        button.setIcon(CALLENDER_DEFAULT_ICON);
        button.setText("");
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(CALLENDER_HOVERED_ICON);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(CALLENDER_DEFAULT_ICON);
            }
        });
        return datePicker;
    }

    private TriggerLogTableUI createTableUI() {
        tableUI = new TriggerLogTableUI(800, 100);
        return tableUI;
    }

    public TriggerLogTableUI getTableUI() {
        return tableUI;
    }

    public void setTableUI(TriggerLogTableUI tableUI) {
        this.tableUI = tableUI;
    }
}
