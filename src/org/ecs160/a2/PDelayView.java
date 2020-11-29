package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;

public class PDelayView extends Container {
    UserViewForm simulator;

    private static Container p_DelayLayout = new Container(new LayeredLayout());

    private static Container appLayout;
    private static Container labelLayout;
    private static Container wireLayout;

    PDelayView(UserViewForm _simulator_) {
        simulator = _simulator_;
        Container[] containers = _simulator_.circuitDisplay.getContainers();
        appLayout = containers[0];
        labelLayout = containers[1];
        p_DelayLayout = containers[2];
        wireLayout = containers[3];

        this.setLayout(new LayeredLayout());
    }

    private void updateView() {
        simulator.circuitDisplay.mode = UserMode.PDELAY;
    }

    public void swapView() {
        removeComponent(appLayout);
        removeComponent(labelLayout);
        removeComponent(p_DelayLayout);
        removeComponent(wireLayout);
    }

    public void toView() {
        add(labelLayout);
        add(p_DelayLayout);
        add(wireLayout);
        add(appLayout);
    }
}
