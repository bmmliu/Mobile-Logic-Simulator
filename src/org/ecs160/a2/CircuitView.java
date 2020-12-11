package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.spinner.Picker;

import java.util.ArrayList;

enum UserMode {
    WIRE,
    DELETE,
    EDIT,
    PDELAY,
    RUNNING
}

public class CircuitView extends Container {
    public static UserViewForm simulator;

    public static UserMode mode;
    public static Wire wire;

    // These are things needed to be saved
    public static Container circuitBoardContainer = new Container(new GridLayout(10, 10));
    public static ArrayList<Slot> slots = new ArrayList<Slot>();
    private static Container appLayout = new Container(new BorderLayout());
    private static Container labelLayout = new Container(new LayeredLayout());
    private static Container wireLayout = new Container(new LayeredLayout());

    public CircuitBoard circuitBoard;

    // Copy target of needed to save things
    // When saving, make a copy of these, save the copy, and empty these containers
    public ArrayList<Slot> CopySlots;                   // When making copy of slots, finish initalizing all the slots first, then loop through each slots in real slots, and check the connection with each gate, based on their connects, we can get the slotID connected to, and connect them
    public Container CopyCircuitBoardContainer;
    public Container CopyAppLayout;
    public Container CopyLabelLayout;
    public Container CopyWireLayout;
    public CircuitBoard copyCircuitBoard;

    CircuitView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;
        this.circuitBoard = circuitBoard;

        initCircuitView();
    }

    // TODO: SAVE/LOAD
    // SAVE:
    // Call in this order to make a copy of current CircuitView state: copySlots() -> setCopyAppLayout() -> copyConnections()
    // Call those functions right before saving. After calling those function, all 6 of those "copy Containers" will be filled
    // Right after save is called, point all "copy Containers" to new Containers. This is to avoid the next SAVE modifying what was being saved by referring back to their address
    // LOAD:
    // While loading, just set the "actual Containers" to Util.readObject()
    // If there are still problems, I will try to help

    // This one is without connections
    public void copySlots() {
        CopySlots = new ArrayList<>();
        CopyLabelLayout = new Container(new LayeredLayout());
        CopyWireLayout = new Container(new LayeredLayout());

        for (int i = 0; i < 100; i++) {
            CopySlots.add(new Slot(slots.get(i)));
            CopySlots.get(i).setGridPostion(slots.get(i).getParent().getComponentIndex(slots.get(i)));
        }


    }

    public void setCopyAppLayout() {
        CopyAppLayout = new Container(new BorderLayout());
        CopyCircuitBoardContainer = new Container(new GridLayout(10, 10));
        for (Slot s : CopySlots) {
            CopyCircuitBoardContainer.addComponent(s.getGridPostion(), s);
        }

        CopyAppLayout.addComponent(BorderLayout.CENTER, CopyCircuitBoardContainer);
    }

    // This is where we establish connections for the copies
    public void copyConnections() {
        for (int i = 0; i < 100; i++) {
            Gate oldGate = slots.get(i).getGate();
            Gate copyGate = CopySlots.get(i).getGate();
            if (oldGate != null)
                for (int j = 0; j < copyGate.inputs.size(); j++) {
                    Input newI = copyGate.inputs.get(j);
                    Output newO = CopySlots.get(oldGate.inputs.get(j).getPrevOutput().getPortParent().slotID).getGate().outputs.get(0);
                    if (!newO.isConnected(newI)) newI.setCopyConnection(newO);
                }
        }
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

    static public void addLabel(LabelComponent name) {
        labelLayout.addComponent(name);
    }
    static public void moveLabel(LabelComponent from, LabelComponent to) {
        labelLayout.replace(from, to, null);
    }

    static public void enableDrag(ArrayList<Slot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOnDrag();
        }
        CircuitView.wire.clearConnection();
    }

    static public void disableDrag(ArrayList<Slot> slots) {
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
        disableDrag(slots);
        removeComponent(appLayout);
        removeComponent(labelLayout);
        removeComponent(wireLayout);
        simulator.pDelayDisplay.toView();
    }

    public void toView() {
        mode = UserMode.EDIT;
        enableDrag(slots);
        add(labelLayout);
        add(wireLayout);
        add(appLayout);
        simulator.show();
    }

}
