package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;

public class MenuView extends Container {

    UserViewForm simulator;
    public Container menu;

    public Container topView;
    public Button addWire;
    public Button start;
    public Button stop;
    public Button edit;

    public Container botView;
    public Button andGate;
    public Button orGate;
    public Button xorGate;
    public Button nandGate;
    public Button norGate;
    public Button xnordGate;
    public Button notGate;
    public Button save;

    MenuView(UserViewForm _simulator_) {
        super(new BoxLayout(BoxLayout.Y_AXIS));

        simulator = _simulator_;

        menu = new Container(new GridLayout(2, 1));

        //set top view
        topView = new Container(new GridLayout(1, 3));
        addWire = new Button("Add Wire");
        start = new Button("▶");
        stop = new Button("▌▌");
        edit = new Button("Edit");

        botView = new Container(new GridLayout(2, 4));
        andGate = new Button("AND");
        orGate = new Button("OR");
        xorGate = new Button("XOR");
        nandGate = new Button("NAND");
        norGate = new Button("NOR");
        xnordGate = new Button("XNOR");
        notGate = new Button("NOT");
        save = new Button("save");
        addEventListeners();
        addButtons();
    }

    public void addEventListeners(){



        //Top buttons
        addWire.addActionListener(new MenuOperationListener(simulator));
        start.addActionListener(new MenuOperationListener(simulator));
        stop.addActionListener(new MenuOperationListener(simulator));
        edit.addActionListener(new MenuOperationListener(simulator));

        //Bot buttons
        andGate.addActionListener(new MenuGateListener(simulator));
        orGate.addActionListener(new MenuGateListener(simulator));
        xorGate.addActionListener(new MenuGateListener(simulator));
        nandGate.addActionListener(new MenuGateListener(simulator));
        norGate.addActionListener(new MenuGateListener(simulator));
        xnordGate.addActionListener(new MenuGateListener(simulator));
        notGate.addActionListener(new MenuGateListener(simulator));
        save.addActionListener(new MenuGateListener(simulator));

    }

    public void addButtons() {
        this.setLayout(new BorderLayout());

        Container startStopButtonCell = new Container(new GridLayout(1, 2));
        startStopButtonCell.add(start).add(stop);
        topView.add(addWire);
        topView.add(startStopButtonCell);
        topView.add(edit);
        this.add(BorderLayout.NORTH, topView);

        botView.add(andGate);
        botView.add(orGate);
        botView.add(xorGate);
        botView.add(nandGate);
        botView.add(norGate);
        botView.add(xnordGate);
        botView.add(notGate);
        botView.add(save);
        this.add(BorderLayout.CENTER, botView);

    }

}
