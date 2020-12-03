package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.spinner.Picker;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PDelayView extends Container {
    UserViewForm simulator;

    private static Container appLayout;
    private static Container labelLayout;
    private static Container wireLayout;

    // TODOPDELAY START
    private Picker pDelayPicker = new Picker();
    String pDelay = "0";
    // TODOPDELAY END

    PDelayView(UserViewForm _simulator_) {
        simulator = _simulator_;
        Container[] containers = _simulator_.circuitDisplay.getContainers();
        appLayout = containers[0];
        labelLayout = containers[1];
        wireLayout = containers[2];

        this.setLayout(new LayeredLayout());
    }

    // TODOPDELAY START
    public int updatePView() {
        return 0;
    }
    // TODOPDELAY END

    public void swapView() {
        hidePDelay();
        removeComponent(appLayout);
        removeComponent(labelLayout);
        removeComponent(wireLayout);
    }

    public void toView() {
        add(labelLayout);
        add(wireLayout);
        add(appLayout);
        showPDelay();
        simulator.show();
    }

    private void showPDelay() {
        for (Gate g : simulator.circuitBoard.gates.values()) {
            g.swapLabel(UserMode.PDELAY);
        }
    }

    private void hidePDelay() {
        for (Gate g : simulator.circuitBoard.gates.values()) {
            g.swapLabel(UserMode.EDIT);
        }
    }
}

// TODOPDELAY START
class PDelayListener implements ActionListener{
    private UserViewForm simulator;
    private Picker pDelayPicker = new Picker();
    String pDelay = "0";

    public PDelayListener(UserViewForm _simulator_) {
        simulator = _simulator_;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        pDelayPicker.setType(Display.PICKER_TYPE_STRINGS);
        String [] delay_array = new String[100];
        for (int a = 0; a < delay_array.length; a++) {
            delay_array[a] = Integer.toString(a);
        }
        pDelayPicker.setStrings(delay_array);
        pDelayPicker.addActionListener((event1)->{
            pDelay = pDelayPicker.getSelectedString();
        });
        simulator.menuDisplay.removeComponent(pDelayPicker);
        simulator.menuDisplay.revalidate();
    }
}
// TODOPDELAY END

