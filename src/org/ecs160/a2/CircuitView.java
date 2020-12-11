package org.ecs160.a2;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.spinner.Picker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

enum UserMode {
    WIRE,
    DELETE,
    EDIT,
    PDELAY,
    RUNNING
}

public class CircuitView extends Container implements Externalizable {
    public static UserViewForm simulator;

    public static UserMode mode;
    public static Wire wire;
    public Container circuitBoardContainer = new Container(new GridLayout(10, 10));
    public ArrayList<Slot> slots = new ArrayList<Slot>();
    


    private Container appLayout = new Container(new BorderLayout());
    private Container labelLayout = new Container(new LayeredLayout());
    private Container wireLayout = new Container(new LayeredLayout());

    public CircuitBoard circuitBoard;

    CircuitView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;
        this.circuitBoard = circuitBoard;

        initCircuitView();
    }

    CircuitView(CircuitView circuitView){
        this.circuitBoard = new CircuitBoard(circuitView.circuitBoard);
    }


    private void initCircuitView() {
        mode = UserMode.EDIT;
        wire = new Wire(wireLayout);

        this.setLayout(new LayeredLayout());
        add(labelLayout);
        add(wireLayout);
        add(appLayout);

        // To initialize the circuitBoard, place empty slots to all of them
        for (int i = 0; i < 100; i++) {
            slots.add(new Slot());
            Slot s = slots.get(slots.size()-1).getSlot();
            circuitBoardContainer.addComponent(s);
            s.setId(i);
            s.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    evt.consume();

                    if (!s.isEmpty()) {
                        switch(mode) {
                            case WIRE:
                                wire.addConnection(s);
                                break;
                            case DELETE:
                                circuitBoard.removeGate(s.getGate());
                                break;
                            case RUNNING:
                                if (s.getGate().gateType == GateType.INPUT_PIN)
                                    circuitBoard.toggleInput(s.getGate().getLabelName());
                                break;
                            case PDELAY:
                                if (s.getGate().gateType != GateType.INPUT_PIN && s.getGate().gateType != GateType.OUTPUT_PIN)
                                    simulator.pDelayDisplay.updatePView(s);
                                break;
                            case EDIT:
                                break;
                            default:
                                System.out.println("Error @CircuitView for s.addActionListener");
                                break;
                        }

                        simulator.show();
                    }
                }
            });
        }

        appLayout.addComponent(BorderLayout.CENTER, circuitBoardContainer);
    }

    public Container getLabelLayout() {
        return labelLayout;
    }

    public void addLabel(LabelComponent name) {
        labelLayout.addComponent(name);
    }
    public void moveLabel(LabelComponent from, LabelComponent to) {
        labelLayout.replace(from, to, null);
    }

//    static public void enableDrag(ArrayList<Slot> slots) {
//        for (int i = 0; i < slots.size(); i++) {
//            slots.get(i).turnOnDrag();
//        }
//        CircuitView.wire.clearConnection();
//    }

    public void enableDrag(){
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOnDrag();
        }
        CircuitView.wire.clearConnection();
    }

//    public void disableDrag(ArrayList<Slot> slots) {
//        for (int i = 0; i < slots.size(); i++) {
//            slots.get(i).turnOffDrag();
//        }
//    }

    public void disableDrag(){
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOffDrag();
        }
    }

    /*
    static public Slot findSlot(ArrayList<Slot> slots, int id) {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getId() == id) {
                return slots.get(i);
            }
        }
        return null;
    }

     */

    public Container[] getContainers() {
        return new Container[] {appLayout, labelLayout, wireLayout};
    }

    public void swapView() {
        mode = UserMode.PDELAY;
        disableDrag();
        removeComponent(appLayout);
        removeComponent(labelLayout);
        removeComponent(wireLayout);
        simulator.pDelayDisplay.toView();
    }

    public void toView() {
        mode = UserMode.EDIT;
        enableDrag();
        add(labelLayout);
        add(wireLayout);
        add(appLayout);
        simulator.show();
    }
    /*
    public static UserViewForm simulator;

    public static UserMode mode;
    public static Wire wire;
    public static Container circuitBoardContainer = new Container(new GridLayout(10, 10));
    public static ArrayList<Slot> slots = new ArrayList<Slot>();

    private static Container appLayout = new Container(new BorderLayout());
    private static Container labelLayout = new Container(new LayeredLayout());
    private static Container wireLayout = new Container(new LayeredLayout());

    public CircuitBoard circuitBoard;

     */

    static {Util.register("CircuitView", CircuitView.class);}

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void externalize(DataOutputStream dataOutputStream) throws IOException {
//        Util.writeObject(simulator, dataOutputStream);

//        Util.writeObject(mode, dataOutputStream);
//        dataOutputStream.writeInt(mode.ordinal());
//        Util.writeObject(wire, dataOutputStream);
//        Util.writeObject(circuitBoardContainer, dataOutputStream);

//        Util.writeObject(slots, dataOutputStream);
//
//
//        Util.writeObject(appLayout, dataOutputStream);
//        Util.writeObject(labelLayout, dataOutputStream);
//        Util.writeObject(wireLayout, dataOutputStream);
        //CircuitBoard newCircuitBoard = new CircuitBoard(this.circuitBoard);
        Util.writeObject(circuitBoard, dataOutputStream);
    }

    @Override
    public void internalize(int i, DataInputStream dataInputStream) throws IOException {
//        simulator = (UserViewForm) Util.readObject(dataInputStream);
//
//        mode = UserMode.values()[dataInputStream.readInt()];
//        wire = (Wire) Util.readObject(dataInputStream);
//        circuitBoardContainer = (Container) Util.readObject(dataInputStream);
//        slots = (ArrayList<Slot>) Util.readObject(dataInputStream);

//        appLayout = (Container) Util.readObject(dataInputStream);
//        labelLayout = (Container) Util.readObject(dataInputStream);
//        wireLayout = (Container) Util.readObject(dataInputStream);
        this.circuitBoard = (CircuitBoard) Util.readObject(dataInputStream);
    }

    @Override
    public String getObjectId() {
        return "CircuitView";
    }
}
