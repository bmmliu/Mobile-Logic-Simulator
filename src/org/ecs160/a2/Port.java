package org.ecs160.a2;

import com.codename1.ui.Component;

import java.util.ArrayList;

/**
 * A port represents a point of entry or exit from a circuit.
 * Ports are connected by wires, which are represented in the UI.
 * The state of ports is determined by the state of their predecessors, or the state of their parent gate.
 */
public class Port{
    protected String parent;
    protected State state;

    public Port(Gate g) {
        parent = g.getLabelName();
        state = State.NOT_CONNECTED;
    }

    public Port(Port p) {
        parent = p.parent;
        state = p.state;
    }

    public Gate getPortParent() {
        return CircuitView.simulator.circuitBoard.gates.get(parent);
    }

    public State getState(){
        return state;
    }

    public void setState(State state){
        this.state = state;
        if (state == State.ONE) {
            this.getPortParent().currentImage = this.getPortParent().onImage;
        } else {
            this.getPortParent().currentImage = this.getPortParent().offImage;    // TODO: For now, set image to offImage regardless
        }
    }
}

/**
 * Input ports represent the entry points of a Gate.
 * They store the previous output port that connects to it, allowing for backwards traversal of the circuit.
 * The state of an input port is dependent on the previous output port it connects to.
 */
class Input extends Port {
    private WireComponent wire;
    private Output prevOutput;

    public Input(Gate g) {
        super(g);
        wire = null;
        prevOutput = null;
    }

    // Establish connections between prevOutputs later
    public Input(Input i) {
        super(i);
        wire = new WireComponent(i.getWire());
        prevOutput = null;
    }

    public void setConnection(Output output, WireComponent with) {
        this.prevOutput = output;
        output.setConnectedInput(this);
        wire = with;
        getPortParent().setLabel(new LabelComponent(getPortParent().getLabel(), getPortParent().updatePortNumTag(1)));
    }


    public Output getPrevOutput(){
        return prevOutput;
    }

    public boolean isConnected() {
        return prevOutput != null;
    }

    //Disconnect the wire and unlink from the previous output port
    public void disconnect() {
        if (prevOutput == null) return;
        prevOutput.getConnectedInputs().remove(this);
        prevOutput = null;
        wire.getParent().removeComponent(wire);
        wire = null;
        getPortParent().setLabel(new LabelComponent(getPortParent().getLabel(), getPortParent().updatePortNumTag(-1)));
    }

    public void redrawWire(WireComponent newWire) {
        wire.getParent().replace(wire, newWire, null);
        wire = newWire;
    }

    public WireComponent getWire() {
        return wire;
    }
}


/**
 * Output ports represent points of exit from a gate. They can connect to multiple input ports further down the circuit.
 * The state of an output port is determined by its parent Gate.
 * Output ports can reset the states of all following input ports, depending on its current state.
 */
class Output extends Port {
    //The input of another gate that this output is connected to.
    //Any output ports can connect to multiple input ports
    //protected Input connectedInput;
    protected ArrayList<Input> connectedInputs;

    public Output(Gate g) {
        super(g);
        connectedInputs = new ArrayList<>();
    }

    public Output(Output o) {
        super(o);
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

    //Disconnect all inputs that follow from this port
    public void disconnectAll() {
        for (Input i : connectedInputs) {
            if (i.isConnected()) {
                Output o = i.getPrevOutput();
                o = null;
                WireComponent w = i.getWire();
                w.getParent().removeComponent(w);
                w = null;
                i.getPortParent().setLabel(new LabelComponent(i.getPortParent().getLabel(), i.getPortParent().updatePortNumTag(-1)));
                if (i.getPortParent().gateType != GateType.SUBCIRCUIT) {
                    i.getPortParent().inputs.remove(i);
                }
            }
        }
        connectedInputs.clear();
    }

    //Update all the inputs that follow this port
    public void updateConnectedInput(){
        if(!connectedInputs.isEmpty()){
            for (Input i : connectedInputs) {
                i.setState(this.state);
            }
        }
    }
}
