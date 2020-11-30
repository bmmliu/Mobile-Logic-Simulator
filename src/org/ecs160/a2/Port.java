package org.ecs160.a2;

import java.util.ArrayList;

// TODO: Add the XY coordinate of Ports here?
//       This can easily done by getting the AbsoluteX() of the host gate,
//       then take factor out the amount of inputs and output the host gate have
public class Port {
    private Gate parent;
    private State state;

    public Port(Gate g) {
        parent = g;
        state = State.NOT_CONNECTED;
    }

    public Gate getParent() {
        return parent;
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
    }
}

// Input can only connect to one other Port at anytime
class Input extends Port {
    private WireComponent wire;
    private Output output;
    private Output prevOutput;

    public Input(Gate g) {
        super(g);
        wire = null;
        output = null;
        prevOutput = null;
    }

    public void setConnection(Output output, WireComponent with) {
        output = ti;
        wire = with;
    }

    public Output getNextOutput() {
        return output;
    }

    public Output getPrevOutput(){
        return prevOutput;
    }

    public void setPrevOutput(Output prevOutput){
        this.prevOutput = prevOutput;
    }

    public boolean isConnected() {
        return output != null;
    }

    public void disconnect() {
        if (output == null) return;
        output = null;
        wire.getParent().removeComponent(wire);
        wire = null;
    }

    public void redrawWire(WireComponent newWire) {
        wire.getParent().replace(wire, newWire, null);
        wire = newWire;
    }

    public WireComponent getWire() {
        return wire;
    }

}

// Output can connect with multiple port at anytime
class Output extends Port {
    protected ArrayList<Input> connectedInputs;

    public Output(Gate g) {
        super(g);
        connectedInputs = new ArrayList<Input>();
    }

    public void setConnection(Input ti) {
        connectedInputs.add(ti);
        ti.setPrevOutput(this);
    }

    public ArrayList<Input> getConnectedInputs() {
        return connectedInputs;
    }

    public boolean isConnected(){
        return !connectedInputs.isEmpty();
    }

    public void disconnectFromInput(Input i) {
        connectedInputs.remove(i);
    }

    public void disconnect() {
        connectedInputs.clear();
    }
}
