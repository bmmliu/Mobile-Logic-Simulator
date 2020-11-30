package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
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

    public CircuitBoard circuitBoard;


    MenuView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        theme = UIManager.initFirstTheme("/theme");

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
        addTopViewEventListeners();
        addBotViewEventListeners();
        addButtons();
    }

    public void addTopViewEventListeners() {
        // When clicked "Wire" button, user can add wire to connect each gates
        // This will switch all components to nondraggable
        addWire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                CircuitView.mode = UserMode.WIRE;
                CircuitView.disableDrag(CircuitView.slots);
            }
        });

        // Edit will enable user to move object around
        // This will switch all appropriate components to draggable
        // Upon user clicking any buttons to add new gates, it will switch back to edit mode
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                CircuitView.mode = UserMode.EDIT;
                CircuitView.enableDrag(CircuitView.slots);
            }
        });

        // Delete can will enable user to delete object on click
        // This will switch all components to nondraggable
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                CircuitView.mode = UserMode.DELETE;
                CircuitView.disableDrag(CircuitView.slots);
            }
        });
    }

    public void addBotViewEventListeners() {
        // When gate buttons are clicked, object should be spawned at the first available slot
        inputpin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                CircuitView.mode = UserMode.EDIT;
                CircuitView.enableDrag(CircuitView.slots);
                for (int i = 0; i < CircuitView.slots.size(); i++) {
                    Slot s = CircuitView.slots.get(i);
                    if (s.isEmpty()) {
                        InputPin inputPin = new InputPin(s.getId());
                        circuitBoard.addInputPin(inputPin);
                        s.setSlot(inputPin);
                        simulator.show();
                        break;
                    }
                }
                simulator.show();
            }
        });

        outputpin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                CircuitView.mode = UserMode.EDIT;
                CircuitView.enableDrag(CircuitView.slots);
                for (int i = 0; i < CircuitView.slots.size(); i++) {
                    Slot s = CircuitView.slots.get(i);
                    if (s.isEmpty()) {
                        OutputPin outputPin = new OutputPin(s.getId());
                        circuitBoard.addOutputPin(outputPin);
                        s.setSlot(outputPin);
                        simulator.show();
                        break;
                    }
                }
                simulator.show();
            }
        });

        andGate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                CircuitView.mode = UserMode.EDIT;
                CircuitView.enableDrag(CircuitView.slots);
                for (int i = 0; i < CircuitView.slots.size(); i++) {
                    Slot s = CircuitView.slots.get(i);
                    if (s.isEmpty()) {
                        AndGate andGate = new AndGate(s.getId());
                        circuitBoard.addGate(andGate);
                        s.setSlot(andGate);
                        simulator.show();
                        break;
                    }
                }
                simulator.show();
            }
        });
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
