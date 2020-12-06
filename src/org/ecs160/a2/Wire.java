package org.ecs160.a2;

import com.codename1.components.ToastBar;
import com.codename1.ui.Container;
import com.codename1.ui.FontImage;

import java.util.ArrayList;

public class Wire {
    public static final int RED = 0xFF0000;
    public static final int GREEN = 0x00FF00;
    public static final int BLUE = 0x0000FF;
    public static final int DARK_GREEN = 0x0E8365;

    private ArrayList<Slot> connection;
    private Container wireMap;

    public Wire(Container f) {
        connection = new ArrayList<Slot>();
        wireMap = f;
    }

    public void addConnection(Slot s) {
        connection.add(s);
        connect();
    }


    // Since this is just moving, we should NEVER modify actual input & output connections here
    // Redraw here is specifically for the dropListener for each slots
    public void rearrangeWire(Slot s) {
        ArrayList<Input> inputs = s.getGate().inputs;
        ArrayList<Output> outputs = s.getGate().outputs;

        // For each inputs, remove their wireComponents, then reconnect them
        for (Input i : inputs) {
            redrawWire(s, i);
        }

        // For each outputs, reconnect its corresponding input
        for (Output o : outputs) {
            ArrayList<Input> Is = o.getConnectedInputs();
            if (Is != null) {
                for (Input i : Is) {
                    Slot s1 = i.parent.parent;
                    redrawWire(s1, i);
                }
            }
        }
    }

    private void connect() {
        // TODO: We might want to highlight gate to indicate which gate user had selected to connect from
        if (connection.size() == 1) {
            if (connection.get(0).getGate().gateType == GateType.SUBCIRCUIT) {
                connection.get(0).getGate().promptSCOuputPort();
                if (Subcircuit.outputInterest == -1) {
                    ToastBar.showMessage( "Cancelled Subcircuit Connection Initialization", FontImage.MATERIAL_INFO);
                    clearConnection(); // Clear connection if user choose cancel
                    return;
                }
            }
            ToastBar.showMessage( "Connection Initialized", FontImage.MATERIAL_INFO);
        } else if (connection.size() == 2) {
            // If two gate in connection, will need to perform one of two things; either:
            //  Two gates have not been connected, thus need connection
            //  Two gates had been connection, thus need need disconnection

            Gate gate1 = connection.get(0).getGate();
            Gate gate2 = connection.get(1).getGate();

            //System.out.println("Establishing Connection...");

            // We can only connect the two gate if:
            // gate1 and gate2 are different gates
            // gate1's output is available for gate2 && gate2's input is available for gate1
            if (sameGateConnection()) {
                clearConnection();
                return;
            }

            // Check if the two gates had already been connected. If so, delete connection and return
            if (gate1.isConnectedTo(gate2)) {   // NOTE: However, disconnection for subcircuits are handled while making connections
                disconnect(gate1, gate2);       //       Can handle two subcircuit connections in makeConnection
                clearConnection();
                return;
            }

            makeConnection(gate1, gate2);
            clearConnection();  // Regardless if the connection was established, reset our static wire
        }
    }

    // TODO: If we add highlight to our gate, unhighlight our gates right here
    //resetConnection was doing the same thing as clearConnection, so I deleted resetConnection.
    public void clearConnection() {
        connection.clear();
        CircuitView.simulator.show();
    }

    // drawWire should always have s1 house input, s2 house output
    private WireComponent drawWire(Slot s1, Slot s2, int color) {
        int offsetX = s1.getWidth();
        int offsetY = s1.getHeight()/2;
        //int s1offsetY = s1.getGate().getOffset(0);
        //int s2offsetY = s2.getGate().getOffset(1);

        int x1 = s1.getAbsoluteX();
        int y1 = s1.getAbsoluteY();
        int x2 = s2.getAbsoluteX();
        int y2 = s2.getAbsoluteY();

        return new WireComponent(x1, y1 - offsetY, x2 + offsetX, y2 - offsetY, color);
    }

    private WireComponent placeWire(int color) {
        WireComponent wire = drawWire(connection.get(1), connection.get(0), color);
        wireMap.addComponent(wire);
        return wire;
    }

    private void redrawWire(Slot s1, Input i) {
        // We don't need to rearrange anything if the gate being moved have no connection
        if (i.isConnected()) {
            //System.out.println("Connected Input detected. Redrawing Wire");
            Slot s2 = i.getPrevOutput().parent.parent;
            //Slot s2 = CircuitView.slots.get(i.getPrevOutput().getParent().slotID);
            int color = i.getWire().getColor();
            i.redrawWire(drawWire(s1, s2, color));
        }
    }

    // Copy wire but change its color
    private WireComponent drawWire(WireComponent wire, int color) {
        WireComponent copy = new WireComponent(wire, color);
        wire.getParent().removeComponent(wire);
        wireMap.addComponent(copy);
        return copy;
    }

    private boolean sameGateConnection() {
        if (connection.get(0).getId() == connection.get(1).getId()) {   // Cancel connection if attempt to connect same slot
            //System.out.println("Connection Fail. Attempt to connect same gate");
            return true;
        }
        return false;
    }

    private void disconnect(Gate gate1, Gate gate2) {
        //System.out.println("Found two connected gates, verifying connection...");
        gate1.disconnect(gate2);
    }

    private void makeConnection(Gate gate1, Gate gate2) {
        //System.out.println("Attempting to make connection");
        if (gate1.connectionPossible(gate2)) {
            WireComponent wire = placeWire(DARK_GREEN);
            wireMap.layoutContainer();
            gate1.connect(gate2, wire);
        }

    }
}