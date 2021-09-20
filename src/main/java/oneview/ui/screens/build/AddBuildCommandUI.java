package oneview.ui.screens.build;

import oneview.ui.RegisterListener;
import oneview.ui.component.CJLocation;
import oneview.ui.component.CJPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;

public class AddBuildCommandUI extends CJPanel implements RegisterListener {
    private JToggleButton cleanButton;
    private JToggleButton installButton;
    private JToggleButton skipTestsButton;
    private JToggleButton skipITButton;
    private CJLocation pomLocation;

    private boolean cleanSelected;
    private boolean installSelected;
    private boolean skipTestSelected;
    private boolean skipITSelected;


    public AddBuildCommandUI() {
        super(new BorderLayout(0, 0));
        cleanButton = new JToggleButton("CLEAN");
        installButton = new JToggleButton("INSTALL");
        skipTestsButton = new JToggleButton("SKIP TESTS");
        skipITButton = new JToggleButton("SKIP IT");
        pomLocation = new CJLocation(null, 280, JFileChooser.FILES_AND_DIRECTORIES);
        pomLocation.getLocationTF().setText("/home/weblogic/PROJECT/MASTER/ucare-core/pom.xml");
        {
            CJPanel northPanel = new CJPanel(new FlowLayout(FlowLayout.LEFT));
            northPanel.add(cleanButton);
            northPanel.add(installButton);
            northPanel.add(skipTestsButton);
            northPanel.add(skipITButton);
            this.add(northPanel, BorderLayout.NORTH);
        }

        {
            CJPanel centerPanel = new CJPanel(new FlowLayout(FlowLayout.LEFT));
            centerPanel.add(pomLocation);
            this.add(centerPanel, BorderLayout.CENTER);
        }

        registerListener();
    }

    @Override
    public void registerListener() {
        cleanButton.addItemListener(ie -> cleanSelected = ie.getStateChange() == ItemEvent.SELECTED);
        installButton.addItemListener(ie -> installSelected = ie.getStateChange() == ItemEvent.SELECTED);
        skipTestsButton.addItemListener(ie -> skipTestSelected = ie.getStateChange() == ItemEvent.SELECTED);
        skipITButton.addItemListener(ie -> skipITSelected = ie.getStateChange() == ItemEvent.SELECTED);
    }

    public boolean isCleanSelected() {
        return cleanSelected;
    }

    public boolean isInstallSelected() {
        return installSelected;
    }

    public boolean isSkipTestSelected() {
        return skipTestSelected;
    }

    public boolean isSkipITSelected() {
        return skipITSelected;
    }

    public String getPOM(){
        return pomLocation.getLocationTF().getText();
    }
}
