package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.spinner.Picker;

enum PDelayState {
    IN_USE,
    FREE
}

public class PDelayView extends Container {
    UserViewForm simulator;

    private static Container appLayout;
    private static Container labelLayout;
    private static Container wireLayout;
    private static PDelayState state;

    // TODOPDELAY START
    private String[] delay_array = new String[100];
    private Picker pDelayPicker = new Picker();
    private ActionListener pDelayListener;
    int pDelay = 0;
    // TODOPDELAY END

    PDelayView(UserViewForm _simulator_) {
        simulator = _simulator_;
        Container[] containers = _simulator_.circuitDisplay.getContainers();
        appLayout = containers[0];
        labelLayout = containers[1];
        wireLayout = containers[2];

        for (int a = 0; a < delay_array.length; a++) {
            delay_array[a] = Integer.toString(a);
        }

        pDelayPicker.setType(Display.PICKER_TYPE_STRINGS);
        pDelayPicker.setStrings(delay_array);

        state = PDelayState.FREE;

        pDelayListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {}
        };

                this.setLayout(new LayeredLayout());
    }

    public void setState(PDelayState s) {
        state = s;
    }

    public PDelayState getState(PDelayState s) {
        return state;
    }

    // TODOPDELAY START
    public void updatePView(Slot s) {
        if (state == PDelayState.FREE) {

            pDelayListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    e.consume();
                    pDelay = Integer.parseInt(pDelayPicker.getSelectedString());
                    simulator.menuDisplay.removeComponent(pDelayPicker);
                    simulator.menuDisplay.revalidate();

                    s.getGate().setPDelay(pDelay);
                    state = PDelayState.FREE;

                    pDelayPicker.removeActionListener(pDelayListener);
                }
            };

            pDelayPicker.addActionListener(pDelayListener);
            simulator.menuDisplay.add(BorderLayout.SOUTH, pDelayPicker);
            state = PDelayState.IN_USE;
            simulator.menuDisplay.revalidate();
            //pDelayPicker.pressed();
            //pDelayPicker.released();
        }
    }
    // TODOPDELAY END

    public void swapView() {
        if (state == PDelayState.IN_USE) {
            simulator.menuDisplay.removeComponent(pDelayPicker);
            pDelayPicker.removeActionListener(pDelayListener);
            state = PDelayState.FREE;
        }

        hidePDelay();
        removeComponent(appLayout);
        removeComponent(labelLayout);
        removeComponent(wireLayout);
        simulator.circuitDisplay.toView();
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
