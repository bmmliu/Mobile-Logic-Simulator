package org.ecs160.a2;

import java.util.ArrayList;

// TODO: Add the XY coordinate of Ports here?
//       This can easily done by getting the AbsoluteX() of the host gate,
//       then take factor out the amount of inputs and output the host gate have
public class Port {
    private Gate parent;
    protected State state;

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
        if (state == State.ONE) {
            this.parent.currentImage = this.parent.onImage;
        } else {
            this.parent.currentImage = this.parent.offImage;    // TODO: For now, set image to offImage regardless
        }
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
        output.setConnectedInput(this);
        wire = with;
    }


    public Output getPrevOutput(){
        return prevOutput;
    }

    public boolean isConnected() {
        //System.out.println(prevOutput);
        return prevOutput != null;
    }

    public void disconnect() {
        if (prevOutput == null) return;
        prevOutput.getConnectedInputs().remove(this);
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
    //Any output ports can connect to multiple input ports
    //protected Input connectedInput;
    protected ArrayList<Input> connectedInputs;

    public Output(Gate g) {
        super(g);
        connectedInputs = new ArrayList<>();
    }

    public void setConnectedInput(Input to) {
        connectedInputs.add(to);
    }

    public ArrayList<Input> getConnectedInputs() {
        return connectedInputs;
    }

    public boolean isConnected(Input to){
        return !connectedInputs.isEmpty() && connectedInputs.indexOf(to) != -1;
    }

    public void disconnect(Input with){
        connectedInputs.remove(with);
    }
    public void disconnectAll() {
        for (Input i : connectedInputs) {
            if (i.isConnected()) {
                Output o = i.getPrevOutput();
                o = null;
                WireComponent w = i.getWire();
                w.getParent().removeComponent(w);
                w = null;
                i.getParent().inputs.remove(i);
            }
        }
        connectedInputs.clear();
    }

    public void updateConnectedInput(){
        if(!connectedInputs.isEmpty()){
            for (Input i : connectedInputs) {
                i.setState(this.state);
            }
        }
    }
}
