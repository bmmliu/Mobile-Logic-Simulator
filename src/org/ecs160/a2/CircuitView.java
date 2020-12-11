package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Modes in which the user can interact with the circuit.
 */
enum UserMode {
    WIRE,
    DELETE,
    EDIT,
    PDELAY,
    RUNNING
}

/**
 * View for displaying a circuit.
 * Stores a circuitBoard containing the logic of the circuit, and an array of Slots determining the placement of gates in the view
 */
public class CircuitView extends Container implements Externalizable {
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
    public CircuitBoard CopyCircuitBoard;

    CircuitView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;
        this.circuitBoard = circuitBoard;

        initCircuitView();
    }

    public void prelimSave(){
        copySlots();
        setCopyAppLayout();
        copyConnections();
    }

    public void postSave(){
        CopySlots = new ArrayList<Slot>();
        CopyCircuitBoardContainer = new Container();
        CopyAppLayout = new Container();
        CopyLabelLayout = new Container();
        CopyWireLayout = new Container();
        CopyCircuitBoard = new CircuitBoard();
    }

    // TODO: SAVE/LOAD
    // SAVE:
    // Call in this order to make a copy of current CircuitView state: copySlots() -> setCopyAppLayout() -> copyConnections()
    // Call those functions right before saving. After calling those function, all 6 of those "copy Containers" will be filled
    // Right after save is called, point all "copy Containers" to new Containers. This is to avoid the next SAVE modifying what was being saved by referring back to their address
    // LOAD:
    // While loading, just set the "actual Containers" to Util.readObject()
    // If there are still problems, I will try to help
    static {Util.register("CircuitView", CircuitView.class);}

    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void externalize(DataOutputStream dataOutputStream) throws IOException {
        Util.writeObject(CopyCircuitBoardContainer, dataOutputStream);
        Util.writeObject(CopySlots, dataOutputStream);
        Util.writeObject(CopyAppLayout, dataOutputStream);
        Util.writeObject(CopyLabelLayout, dataOutputStream);
        Util.writeObject(CopyWireLayout, dataOutputStream);
        Util.writeObject(CopyCircuitBoard, dataOutputStream);
    }

    @Override
    public void internalize(int i, DataInputStream dataInputStream) throws IOException {
        circuitBoardContainer = (Container) Util.readObject(dataInputStream);
        slots = (ArrayList<Slot>)Util.readObject(dataInputStream);
        appLayout = (Container) Util.readObject(dataInputStream);
        labelLayout = (Container) Util.readObject(dataInputStream);
        wireLayout = (Container) Util.readObject(dataInputStream);
        this.circuitBoard = (CircuitBoard) Util.readObject(dataInputStream);
    }

    @Override
    public String getObjectId() {
        return "CircuitView";
    }



    // This one is without connections
    public void copySlots() {
        CopySlots = new ArrayList<Slot>();
        CopyLabelLayout = new Container(new LayeredLayout());
        CopyWireLayout = new Container(new LayeredLayout());
        CopyCircuitBoard = new CircuitBoard();

        for (int i = 0; i < 100; i++) {
            Slot s = new Slot(slots.get(i));
            CopySlots.add(s);
            CopySlots.get(i).setGridPostion(slots.get(i).getParent().getComponentIndex(slots.get(i)));
        }


    }

    public void setCopyAppLayout() {
        CopyAppLayout = new Container(new BorderLayout());
        CopyCircuitBoardContainer = new Container(new GridLayout(10, 10));
        //TODO: ON SECOND ITERATION THIS RETURNS A NULL POINTER EXCEPTION
        for (Slot s : CopySlots) {
            //CopyCircuitBoardContainer.addComponent(s.getGridPostion(), s);
            CopyCircuitBoardContainer.addComponent(s);
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

    public Container[] getContainers() {
        return new Container[] {appLayout, labelLayout, wireLayout};
    }

    //The following functions enable transitions between the Circuit and Propagation Delay tabs
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
