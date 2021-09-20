package oneview.ui.date;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import oneview.ui.RegisterListener;
import oneview.ui.component.CJCheckBox;
import oneview.ui.component.CJLabel;
import oneview.ui.component.CJPanel;
import oneview.ui.component.CJTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.util.Calendar;

import static oneview.ui.constants.DimensionConstants.*;
import static oneview.ui.constants.IconConstants.*;
import static oneview.util.DatePatterns.DP_YYYY_MM_DD;
import static oneview.util.DateUtil.*;
import static oneview.util.GuiUtil.*;
import static oneview.util.ThreadUtil.sleep;

public class DatePanel2 extends CJPanel implements RegisterListener {
    private DatePicker datePicker;
    private CJLabel nameLabel;
    private CJTextField hhText;
    private CJTextField mmText;
    private CJTextField ssText;
    private CJCheckBox eodCheckBox;
    private CJCheckBox sodCheckBox;
    private boolean needEODCheckBox;
    private boolean needSODCheckBox;
    private String defaultHH;
    private String defaultMM;
    private String defaultSS;
    //private CJPanel parent;
    public DatePanel2(/*CJPanel parent,*/ String name, int width, int height, boolean needEODCheckBox, boolean needSODCheckBox) {
        super(width,height);
        this.needEODCheckBox = needEODCheckBox;
        this.needSODCheckBox = needSODCheckBox;
        //this.parent=parent;
        nameLabel = new CJLabel(name, 80, TEXT_FIELD_HEIGHT);
        TimeTextFormator hrTextFormator = new TimeTextFormator(TimeTextFormator.TIME_TYPE.HOUR);
        TimeTextFormator minTextFormator = new TimeTextFormator(TimeTextFormator.TIME_TYPE.MIN);
        TimeTextFormator secTextFormator = new TimeTextFormator(TimeTextFormator.TIME_TYPE.SEC);
        hhText = new CJTextField(percent(width, 5), TEXT_FIELD_HEIGHT, hrTextFormator);
        mmText = new CJTextField(percent(width, 5), TEXT_FIELD_HEIGHT, minTextFormator);
        ssText = new CJTextField(percent(width, 5), TEXT_FIELD_HEIGHT, secTextFormator);
        sodCheckBox = new CJCheckBox(ON_ICON, OFF_ICON, ICON_WIDTH, ICON_HEIGHT, "Start of Day Check");
        eodCheckBox = new CJCheckBox(ON_ICON, OFF_ICON, ICON_WIDTH, ICON_HEIGHT, "End Of Day Check");

        datePicker = createCustomizedDatePickerUI();
        addComponentsWithFlowLayout();
        registerListener();
        setDateTime();

    }

    private DatePicker createCustomizedDatePickerUI() {
        DatePicker datePicker = new DatePicker();
        DatePickerSettings settings = datePicker.getSettings().copySettings();
        settings.setFormatForDatesCommonEra(DP_YYYY_MM_DD);
        datePicker.setSettings(settings);
        datePicker.getComponentDateTextField().setPreferredSize(newDimentionObj(100, oneview.ui.constants.DimensionConstants.TEXT_FIELD_HEIGHT));
        JButton button = datePicker.getComponentToggleCalendarButton();
        button.setPreferredSize(newDimentionObj(oneview.ui.constants.DimensionConstants.TEXT_FIELD_HEIGHT, oneview.ui.constants.DimensionConstants.TEXT_FIELD_HEIGHT));
        button.setIcon(CALLENDER_DEFAULT_SICON);
        button.setText("");
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setIcon(CALLENDER_HOVERED_SICON);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(CALLENDER_DEFAULT_SICON);
            }
        });
        return datePicker;
    }

    public void setDateTime() {
        try {
            sleep(100);
            Calendar currentTime = Calendar.getInstance();
            datePicker.getComponentDateTextField().setText(format(currentTime, DP_YYYY_MM_DD));
            if (needEODCheckBox && eodCheckBox.isSelected()) {
                setTimeTextFieldValues(String.valueOf(23), String.valueOf(59), String.valueOf(59));
                setTimeTextFieldEditable(false);
            } else if (needEODCheckBox && !eodCheckBox.isSelected()) {
                setTimeTextFieldValues(defaultHH, defaultMM, defaultSS);
                setTimeTextFieldEditable(true);
            } else if (needSODCheckBox && sodCheckBox.isSelected()) {
                setTimeTextFieldValues(defaultHH, defaultMM, defaultSS);
                setTimeTextFieldEditable(true);
            } else {
                setTimeTextFieldValues(String.valueOf(currentTime.get(Calendar.HOUR_OF_DAY)),
                        String.valueOf(currentTime.get(Calendar.MINUTE)), String.valueOf(currentTime.get(Calendar.SECOND)));
                setTimeTextFieldEditable(false);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setTimeTextFieldValues(String hh, String mm, String ss) throws ParseException {
        hhText.setText(hhText.getFormatter().valueToString(hh));
        mmText.setText(mmText.getFormatter().valueToString(mm));
        ssText.setText(ssText.getFormatter().valueToString(ss));
    }

    private void setTimeTextFieldEditable(boolean b) {
        this.hhText.setEditable(b);
        this.mmText.setEditable(b);
        this.ssText.setEditable(b);
    }


    private void addComponentsWithFlowLayout() {
        LayoutManager lm = new FlowLayout(FlowLayout.LEFT, 0, 0);
        this.setLayout(lm);
        this.add(nameLabel);
        this.add(newDummyPanelObj(5, 1));
        this.add(datePicker);
        this.add(newDummyPanelObj(5, 1));
        this.add(hhText);
        this.add(newDummyPanelObj(5, 1));
        this.add(mmText);
        this.add(newDummyPanelObj(5, 1));
        this.add(ssText);
        if (needEODCheckBox) {
            this.add(newDummyPanelObj(5, 1));
            this.add(eodCheckBox);
        }
        if (needSODCheckBox) {
            this.add(newDummyPanelObj(5, 1));
            this.add(sodCheckBox);
        }
    }


    public String getDateTime() {
        String date = this.datePicker.getText();
        String hh = this.hhText.getText();
        String mm = this.mmText.getText();
        String ss = this.ssText.getText();
        return date + " " + hh + ":" + mm + ":" + ss + ".000";
    }

    public CJTextField getHhText() {
        return hhText;
    }

    public CJTextField getMmText() {
        return mmText;
    }

    public CJTextField getSsText() {
        return ssText;
    }

    public JCheckBox getEodCheckBox() {
        return eodCheckBox;
    }

    public CJCheckBox getSodCheckBox() {
        return sodCheckBox;
    }


    @Override
    public void registerListener() {
        if (needEODCheckBox) {
            this.eodCheckBox.addActionListener(actionEvent -> {
                setDateTime();
                //parent.fireContextListener();
            });
        }
        if (needSODCheckBox) {
            this.sodCheckBox.addActionListener(actionEvent -> {
                setDateTime();
                //parent.fireContextListener();
            });
        }
        this.hhText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                formatTimeWhenKeyTyped(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setDefaultTime();
                //parent.fireContextListener();
            }
        });

        this.mmText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                formatTimeWhenKeyTyped(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setDefaultTime();
            }
        });
        this.ssText.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                formatTimeWhenKeyTyped(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                setDefaultTime();
            }
        });
    }

    public void setDefaultTime() {
        if (needEODCheckBox && !eodCheckBox.isSelected()) {
            defaultHH = this.getHhText().getText();
            defaultMM = this.getMmText().getText();
            defaultSS = this.getSsText().getText();
        }
        if (needSODCheckBox && sodCheckBox.isSelected()) {
            defaultHH = this.getHhText().getText();
            defaultMM = this.getMmText().getText();
            defaultSS = this.getSsText().getText();
        }
    }

    public void formatTimeWhenKeyTyped(KeyEvent e) {
        try {
            if (e.getSource().equals(this.getHhText())) {
                this.getHhText().getFormatter().valueToString(this.getHhText().getText());
            }
            if (e.getSource().equals(this.getMmText())) {
                this.getMmText().getFormatter().valueToString(this.getMmText().getText());
            }
            if (e.getSource().equals(this.getSsText())) {
                this.getSsText().getFormatter().valueToString(this.getSsText().getText());
            }

        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }
}
