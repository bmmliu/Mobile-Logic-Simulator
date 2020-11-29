package org.ecs160.a2;

import java.util.ArrayList;

// TODO: Add the XY coordinate of Ports here?
//       This can easily done by getting the AbsoluteX() of the host gate,
//       then take factor out the amount of inputs and output the host gate have
public class Port {
    protected ArrayList<Gate> connected;
    private Gate parent;

    public Port(Gate g) {
        connected = new ArrayList<Gate>();
        parent = g;
    }

    public Gate getParent() {
        return parent;
    }

    public void setConnection(Gate to) {
        connected.add(to);
    }

    public void reset() {
        connected.clear();
    }

    public ArrayList<Gate> getConnectedGates() {
        return connected;
    }

    public boolean isConnectedTo(Gate to) {
        for (Gate g : connected) {
            if (g == to) {
                return true;
            }
        }
        return false;
    }
}

// Input can only connect to one other Port at anytime
class Input extends Port {
    private wireComponent wire;
    private Output output;

    public Input(Gate g) {
        super(g);
        wire = null;
    }

    public void setConnection(Gate to, Output ti, wireComponent with) {
        if (connected.size() > 0) {
            connected.set(0, to);
        } else {
            connected.add(to);
        }
        output = ti;
        wire = with;
    }

    public Gate getConnectedGate() {
        return connected.get(0);
    }

    public Output getConnectedPort() {
        return output;
    }

    public boolean isConnected() {
        if (connected.size() == 0) {
            return false;
        }
        return true;
    }

    public void reset() {
        if (output == null) return;
        connected.clear();
        output = null;
        wire.getParent().removeComponent(wire);
        wire = null;
    }

    public void redrawWire(wireComponent newWire) {
        wire.getParent().replace(wire, newWire, null);
        wire = newWire;
    }

    public wireComponent getWire() {
        return wire;
    }
}

// Output can connect with multiple port at anytime
class Output extends Port {
    protected ArrayList<Input> connectedTo;

    public Output(Gate g) {
        super(g);
        connectedTo = new ArrayList<Input>();
    }

    public void setConnection(Gate to, Input ti) {
        connected.add(to);
        connectedTo.add(ti);
    }

    public ArrayList<Input> getConnectedPorts() {
        return connectedTo;
    }

    public void reset(Gate g, Input i) {
        connected.remove(g);
        connectedTo.remove(i);
    }

    public void reset() {
        connected.clear();
        connectedTo.clear();
    }
}
