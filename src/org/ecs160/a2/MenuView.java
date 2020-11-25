package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
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
    public Button delete;

    public Container botView;
    public Button inputpin;
    public Button outputpin;
    public Button andGate;
    public Button orGate;
    public Button xorGate;
    public Button nandGate;
    public Button norGate;
    public Button xnordGate;
    public Button notGate;
    public Button save;

    private Resources theme;


    MenuView(UserViewForm _simulator_) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        theme = UIManager.initFirstTheme("/theme");

        simulator = _simulator_;

        menu = new Container(new GridLayout(2, 1));

        //set top view
        topView = new Container(new GridLayout(1, 5));
        addWire = new Button("Wire");
        start = new Button("▶");
        stop = new Button("▌▌");
        edit = new Button("Edit");
        delete = new Button("Delete");

        botView = new Container(new GridLayout(2, 5));
        inputpin = new Button(theme.getImage("inputpin.jpg"));
        outputpin = new Button(theme.getImage("outputpin.jpg"));
        andGate = new Button(theme.getImage("and_gate.jpg"));
        orGate = new Button(theme.getImage("or_gate.jpg"));
        xorGate = new Button(theme.getImage("xor_gate.jpg"));
        nandGate = new Button(theme.getImage("nand_gate.jpg"));
        norGate = new Button(theme.getImage("nor_gate.jpg"));
        xnordGate = new Button(theme.getImage("xnor_gate.jpg"));
        notGate = new Button(theme.getImage("not_gate.jpg"));
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
        delete.addActionListener(new MenuOperationListener(simulator));

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



        topView.add(addWire);
        topView.add(start);
        topView.add(stop);
        topView.add(edit);
        topView.add(delete);
        this.add(BorderLayout.NORTH, topView);

        botView.add(inputpin);
        botView.add(outputpin);
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
