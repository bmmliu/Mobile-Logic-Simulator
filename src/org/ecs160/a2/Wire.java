package org.ecs160.a2;

import com.codename1.ui.Container;
import java.util.ArrayList;

public class Wire {
    private static final int RED = 0xFF0000;
    private static final int GREEN = 0x00FF00;
    private static final int BLUE = 0x0000FF;

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

    public void clearConnection() {
        connection.clear();
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

        // For each outputs, get their list of inputs, then reconnect them
        for (Output o : outputs) {
            for (Input i : o.getConnectedPorts()) {
                Slot s1 = i.getParent().parent;
                redrawWire(s1, i);
            }
        }
    }

    private void connect() {
        // TODO: We might want to highlight gate to indicate which gate user had selected to connect from
        if (connection.size() == 1) {   // If one gate in connection, we are connecting from this gate's output
            //System.out.println("Initialized Connection");
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
                resetConnection();
                return;
            }

            // Check if the two gates had already been connected. If so, delete connection and return
            if (IsConnected(gate1, gate2)) {
                resetConnection();
                return;
            }

            makeConnection(gate1, gate2);
            resetConnection();  // Regardless if the connection was established, reset our static wire
        }
    }

    // TODO: If we add highlight to our gate, unhighlight our gates right here
    private void resetConnection() {
        connection.clear();
    }

    // TODO: Verify that XY offsets works
    // drawWire should always have s1 house input, s2 house output
    private wireComponent drawWire(Slot s1, Slot s2, int color) {
        int offsetX = s1.getWidth();
        int offsetY = s1.getHeight()/2;

        int x1 = s1.getAbsoluteX();
        int y1 = s1.getAbsoluteY();
        int x2 = s2.getAbsoluteX();
        int y2 = s2.getAbsoluteY();

        return new wireComponent(x1, y1-offsetY, x2+offsetX, y2-offsetY, color);
    }

    private wireComponent placeWire(int color) {
        wireComponent wire = drawWire(connection.get(1), connection.get(0), color);
        wireMap.addComponent(wire);
        return wire;
    }

    private void redrawWire(Slot s1, Input i) {
        // We don't need to rearrange anything if the gate being moved have no connection
        if (i.isConnected()) {
            //System.out.println("Connected Input detected. Redrawing Wire");
            Slot s2 = i.getConnectedPort().getParent().parent;
            int color = i.getWire().getColor();
            i.redrawWire(drawWire(s1, s2, color));
        }
    }

    // Copy wire but change its color
    private wireComponent drawWire(wireComponent wire, int color) {
        wireComponent copy = new wireComponent(wire, color);
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

    private boolean IsConnected(Gate gate1, Gate gate2) {
        Output output;
        Input input;

        for (int i = 0; i < gate2.inputs.size(); i++) {
            if (gate2.inputs.get(i).isConnectedTo(gate1)) {
                input = gate2.inputs.get(i);
                output = gate2.inputs.get(i).getConnectedPort();
                //System.out.println("Two gates are connected. Disconnecting...");
                input.reset();
                output.reset(gate2, input);
                return true;
            }
        }

        return false;
    }

    private void makeConnection(Gate gate1, Gate gate2) {
        Output output = null;
        Input input = null;
        boolean connectionAvailable = false;


        //System.out.println("Verifying Each Gate's Availability...");
        // Check if gate2 have available inputs
        for (int i = 0; i < gate2.inputs.size(); i++) {
            //System.out.print("Evaluating gate2's input number ");
            //System.out.println(i);

            if (!gate2.inputs.get(i).isConnected()) {
                //System.out.println("Gate2 is available");
                connectionAvailable = true;
                output = gate1.outputs.get(0);
                input = gate2.inputs.get(i);
                break;
            }
        }

        // Assuming gates only have 1 output for now. gate1 output should always be available
        // Connect output of first gate to the second and input of the second gate to first
        if (connectionAvailable) {
            // TODO: Make the connection drawn match with port the two gates are connecting with
            //       I propose that each port stores the information of where they are no the circuitBoard
            int x1 = connection.get(0).getAbsoluteX();
            int y1 = connection.get(0).getAbsoluteY();
            int x2 = connection.get(1).getAbsoluteX();
            int y2 = connection.get(1).getAbsoluteY();

            //System.out.println("Wire drawn");
            wireComponent wire = placeWire(GREEN);   // Perhaps different color of wire if the future?
            wireMap.layoutContainer();

            output.setConnection(gate2, input);
            input.setConnection(gate1, output, wire);

            //System.out.println("Connection is established");

        } else {    // TODO: We can simply delete this when integrating this code into main code
            //System.out.println("No available ports to make connection");
        }
    }
}
