package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.GridLayout;

public class TopView {

    public TopView() {

    }

    public Container initTopView() {
        return buildButtons();
    }

    // Creates and returns the buttons located in the top view
    private Container buildButtons() {
        Button addWire = new Button("Add Wire");
        Button start = new Button("▶");
        Button stop = new Button("▌▌");
        Button edit = new Button("Edit");

        addWire.addActionListener((e) -> {

        });

        start.addActionListener((e) -> {

        });

        stop.addActionListener((e) -> {

        });

        edit.addActionListener((e) -> {

        });

        Container row = new Container(new GridLayout(1, 3));
        Container startStopButtonCell = new Container(new GridLayout(1, 2));

        startStopButtonCell.add(start).add(stop);
        row.add(addWire).add(startStopButtonCell).add(edit);
        return row;
    }
}
