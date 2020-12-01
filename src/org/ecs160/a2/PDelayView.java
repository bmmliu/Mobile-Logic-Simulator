package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;

public class PDelayView extends Container {
    UserViewForm simulator;

    private static Container appLayout;
    private static Container labelLayout;
    private static Container wireLayout;

    PDelayView(UserViewForm _simulator_) {
        simulator = _simulator_;
        Container[] containers = _simulator_.circuitDisplay.getContainers();
        appLayout = containers[0];
        labelLayout = containers[1];
        wireLayout = containers[2];

        this.setLayout(new LayeredLayout());
    }

    private void updateView() {
        simulator.circuitDisplay.mode = UserMode.PDELAY;

    }

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
