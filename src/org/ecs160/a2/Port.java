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
    private Output prevOutput;

    public Input(Gate g) {
        super(g);
        wire = null;
        prevOutput = null;
    }

    public void setConnection(Output output, WireComponent with) {
        this.prevOutput = output;
        wire = with;
    }


    public Output getPrevOutput(){
        return prevOutput;
    }

    public boolean isConnected() {
        return prevOutput != null;
    }

    public void disconnect() {
        if (prevOutput == null) return;
        prevOutput = null;
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


class Output extends Port {
    //The input of another gate that this output is connected to.
    protected Input connectedInput;

    public Output(Gate g) {
        super(g);
        connectedInput = null;
    }

    public void setConnectedInput(Input ti) {
        connectedInput = ti;
    }

    public Input getConnectedInput() {
        return connectedInput;
    }

    public boolean isConnected(){
        return connectedInput != null && connectedInput.getPrevOutput() == this;
    }

    public void disconnect(){
        connectedInput = null;
    }
}
