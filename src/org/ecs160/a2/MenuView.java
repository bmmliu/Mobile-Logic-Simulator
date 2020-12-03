package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Tabs;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
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

    public Container botView1;
    public Container botView2;
    public Button inputpin;
    public Button outputpin;
    public Button andGate;
    public Button orGate;
    public Button xorGate;
    public Button nandGate;
    public Button norGate;
    public Button xnorGate;
    public Button notGate;
    public Button save;
    public Button load;
    public Button sload;

    private Resources theme;

    public CircuitBoard circuitBoard;

    MenuView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        theme = AppMain.theme;

        simulator = _simulator_;
        this.circuitBoard = circuitBoard;

        menu = new Container(new GridLayout(2, 1));

        //set top view
        topView = new Container(new GridLayout(1, 5));
        addWire = new Button("Wire");
        start = new Button("▶");
        stop = new Button("▌▌");
        edit = new Button("Edit");
        delete = new Button("Delete");

        botView1 = new Container(new GridLayout(2, 5));
        botView2 = new Container(new GridLayout(2, 5));

        inputpin = new Button(theme.getImage("inputpin.jpg"));
        inputpin.setName("inputPin");

        outputpin = new Button(theme.getImage("outputpin.jpg"));
        outputpin.setName("outputPin");

        andGate = new Button(theme.getImage("and_gate.jpg"));
        andGate.setName("andGate");

        orGate = new Button(theme.getImage("or_gate.jpg"));
        orGate.setName("orGate");

        xorGate = new Button(theme.getImage("xor_gate.jpg"));
        xorGate.setName("xorGate");

        nandGate = new Button(theme.getImage("nand_gate.jpg"));
        nandGate.setName("nandGate");

        norGate = new Button(theme.getImage("nor_gate.jpg"));
        norGate.setName("norGate");

        xnorGate = new Button(theme.getImage("xnor_gate.jpg"));
        xnorGate.setName("xnorGate");

        notGate = new Button(theme.getImage("not_gate.jpg"));
        notGate.setName("notGate");

        save = new Button("save");
        load = new Button("load");
        sload = new Button("sload");
        addTopViewEventListeners();
        addBotViewEventListeners();
        addButtons();
        addEventListeners();
    }

    public void addTopViewEventListeners() {
        // When clicked "Wire" button, user can add wire to connect each gates
        // This will switch all components to nondraggable
        addWire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.WIRE;
                    CircuitView.disableDrag(CircuitView.slots);
                }
            }
        });

        // Edit will enable user to move object around
        // This will switch all appropriate components to draggable
        // Upon user clicking any buttons to add new gates, it will switch back to edit mode
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.EDIT;
                    CircuitView.enableDrag(CircuitView.slots);
                }
            }
        });

        // Delete can will enable user to delete object on click
        // This will switch all components to nondraggable
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.DELETE;
                    CircuitView.disableDrag(CircuitView.slots);
                }
            }
        });

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.RUNNING;
                    circuitBoard.runSimulation();
                    simulator.show();
                }
            }
        });

        stop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.EDIT;
                }
            }
        });
    }

    // TODO: Try to make this generic

    public void addBotViewEventListeners() {
        // When gate buttons are clicked, object should be spawned at the first available slot
        inputpin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                //Button button = (Button)evt.getSource();
                //String buttonText = button.getText();
                //System.out.println(buttonText);
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.EDIT;
                    CircuitView.enableDrag(CircuitView.slots);
                    for (int i = 0; i < CircuitView.slots.size(); i++) {
                        Slot s = CircuitView.slots.get(i);
                        if (s.isEmpty()) {
                            InputPin inputPin = new InputPin(s);
                            circuitBoard.addInputPin(inputPin);
                            s.setSlot(inputPin);
                            simulator.show();
                            break;
                        }
                    }
                    simulator.show();
                }
            }
        });

        outputpin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                if (CircuitView.mode != UserMode.PDELAY) {
                    CircuitView.mode = UserMode.EDIT;
                    CircuitView.enableDrag(CircuitView.slots);
                    for (int i = 0; i < CircuitView.slots.size(); i++) {
                        Slot s = CircuitView.slots.get(i);
                        if (s.isEmpty()) {
                            OutputPin outputPin = new OutputPin(s);
                            circuitBoard.addOutputPin(outputPin);
                            s.setSlot(outputPin);
                            simulator.show();
                            break;
                        }
                    }
                    simulator.show();
                }
            }
        });

        andGate.addActionListener(new GateListener(circuitBoard, simulator));
        nandGate.addActionListener(new GateListener(circuitBoard, simulator));
        norGate.addActionListener(new GateListener(circuitBoard, simulator));
        notGate.addActionListener(new GateListener(circuitBoard, simulator));
        orGate.addActionListener(new GateListener(circuitBoard, simulator));
        xnorGate.addActionListener(new GateListener(circuitBoard, simulator));
        xorGate.addActionListener(new GateListener(circuitBoard, simulator));
    }

    public void addEventListeners(){
        save.addActionListener(new MenuGateListener(simulator));
        load.addActionListener(new MenuGateListener(simulator));
        sload.addActionListener(new MenuGateListener(simulator));
    }

    public void addButtons() {
        this.setLayout(new BorderLayout());

        topView.add(addWire);
        topView.add(start);
        topView.add(stop);
        topView.add(edit);
        topView.add(delete);
        this.add(BorderLayout.NORTH, topView);


        botView1.add(inputpin);
        botView1.add(outputpin);
        botView1.add(andGate);
        botView1.add(orGate);
        botView1.add(xorGate);
        botView1.add(nandGate);
        botView1.add(norGate);
        botView1.add(xnorGate);
        botView1.add(notGate);
        botView2.add(save);
        botView2.add(load);
        botView2.add(sload);

        Tabs t = new Tabs();
        t.hideTabs();

        //t.setSwipeActivated(false);
        t.addTab("1", botView1);
        t.addTab("2", botView2);


        this.add(BorderLayout.CENTER, t);
    }

}
