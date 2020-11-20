package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;

public class CircuitView extends Container {

    UserViewForm simulator;
    CircuitView(UserViewForm _simulator_) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;

        Label temp1 = new Label("circuit placeholder");
        Label temp2 = new Label("circuit placeholder");
        Label temp3 = new Label("circuit placeholder");
        Label temp4 = new Label("circuit placeholder");
        Label temp5 = new Label("circuit placeholder");
        Label temp6 = new Label("circuit placeholder");
        Label temp7 = new Label("circuit placeholder");
        add(temp1);
        add(temp2);
        add(temp3);
        add(temp4);
        add(temp5);
        add(temp6);
        add(temp7);

    }
}
