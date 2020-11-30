package org.ecs160.a2;

import com.codename1.ui.Component;
import org.graalvm.compiler.nodeinfo.InputType;

import java.util.ArrayList;


enum GateType{P_1, P_2, INPUT_PIN, OUTPUT_PIN, AND_GATE, SUBCIRCUIT};
enum State{ZERO, ONE, NOT_CONNECTED};

public abstract class Gate extends Component {
    ArrayList<Input> inputs;
    ArrayList<Output> outputs;
    protected State state;
    int slotID;
    LabelComponent label;
    protected GateType gateType;
    protected int inputLimit; //Max number of inputs. If inputLimit is -1, any number of inputs is possible

    public Gate(int slotID) {
        inputs = new ArrayList<Input>();
        outputs = new ArrayList<Output>();

        state = State.NOT_CONNECTED;
        label = null;
        this.slotID = slotID;
        inputLimit = -1;
    }



    //Calculates the state based on the inputs and sets all output ports accordingly
    //Also updates the inputs connected to this gate's outputs
    public void update(){
        calculate();
        for(Output output: outputs){
            output.setState(state);
            output.updateConnectedInput();
        }
    }

    public abstract void calculate();

    //If this Gate's output is connected to any of gate2's inputs, these gates are connected
    public boolean isConnectedTo(Gate gate2){
        for (Input i: gate2.inputs) {
            for(Output o: this.outputs){
                if(i.getPrevOutput() == o){
                    return true;
                }
            }
        }
        return false;
    }

    private boolean passedInputLimit(){
        return inputs.size() + 1 > inputLimit;
    }

    private Output getAvailableOutput(){
        for(Output o: this.outputs){
            if(!o.isConnected()){
                return o;
            }
        }
        return null;
    }

    //Connect this gate's output to one of gate2's inputs. Return true if a successful connection was made.
    public boolean connect(Gate gate2, WireComponent with){
        //Ensure that an additional input connection is legal
        if(gate2.passedInputLimit()){
            return false;
        }

        //Check if this gate has available outputs
        Output output = getAvailableOutput();
        if(output == null){
            return false;
        }

        //Connect this output to the other gate's inputs
        Input input = new Input(gate2);
        input.setConnection(output, with);
        gate2.inputs.add(input);
    }

    public boolean getState(){
        return state;
    }

    public boolean isConnected(){
        for(Input in: inputs){
            if(!in.isConnected()){
                return false;
            }
        }
//        for(Output out: outputs){
//            if(!out.isConnected()){
//                return false;
//            }
//        }
        return true;
    }

    public void deleteGate() {
        for (Input i : inputs) {
            if (i.isConnected()) {
                i.getNextOutput().reset(this, i);
            }
            i.reset();
        }
        for (Output o : outputs) {
            for (Input i : o.getConnectedInputs()) {
                i.reset();
            }
            o.reset();
        }

        label.getParent().removeComponent(label);
        label = null;
    }

    protected abstract LabelComponent makeLabel();

    public String getLabelName() {
        return label.getName();
    }

    public void setLabel(LabelComponent label) {
        this.label = label;
    }

    public LabelComponent getLabel() {
        return label;
    }

    public GateType getGateType(){
        return this.gateType;
    }
}

class P1Gate extends Gate {
    private static int id = 0;

    public P1Gate(int slotID) {
        super(slotID);
        label = makeLabel();

        inputs.add(new Input(this));
        inputs.add(new Input(this));

        outputs.add(new Output(this));

        gateType = GateType.P_1;
    }

    @Override
    public void calculate() {
    }

    @Override
    protected LabelComponent makeLabel() {
        Slot parent = CircuitView.slots.get(slotID);
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "P1Gate " + Integer.toString(id++));
    }
}

class P2Gate extends Gate {
    private static int id = 0;

    public P2Gate(int slotID) {
        super(slotID);
        super.setName("P2Gate");
        label = makeLabel();

        inputs.add(new Input(this));
        inputs.add(new Input(this));

        outputs.add(new Output(this));

        gateType = GateType.P_2;
    }

    @Override
    public void calculate() {
    }

    @Override
    protected LabelComponent makeLabel() {
        Slot parent = CircuitView.slots.get(slotID);
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "P2Gate " + Integer.toString(id++));
    }
}


class AndGate extends Gate{
    private static int id = 0;

    public AndGate(int slotID) {
        super(slotID);
        super.setName("AndGate");
        label = makeLabel();

        inputs.add(new Input(this));
        inputs.add(new Input(this));
        outputs.add(new Output(this));

        gateType = GateType.AND_GATE;
        inputLimit = -1;
    }

    public AndGate(int slotID, int numInputs){
        super(slotID);
        super.setName("AndGate");
        label = makeLabel();

        if(numInputs >= 2){
            for(int i = 0; i<numInputs; i++){
                inputs.add(new Input(this));
            }
            outputs.add(new Output(this));
        }
        else{
            System.out.println("Please enter at least 2 inputs\n");
        }
        inputLimit = -1;
    }

    @Override
    public void calculate() {
        boolean finalState = true;
        for(Input i: inputs){
            finalState = finalState && i.getState();
        }
        //Get all the input values
        //Look up the entry in the truth table with the result for this combination
    }

    @Override
    protected LabelComponent makeLabel() {
        Slot parent = CircuitView.slots.get(slotID);
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "AndGate " + Integer.toString(id++));
    }
}

class InputPin extends Gate {
    private static int id = 0;

    public InputPin(int slotID) {
        super(slotID);
        super.setName("InputPin");
        label = makeLabel();

        inputs.clear();
        outputs.add(new Output(this));
        inputLimit = 0;

        gateType = GateType.INPUT_PIN;
    }

    @Override
    public void calculate() {
    }

    public void toggle(){
        state = !state;
    }

    @Override
    protected LabelComponent makeLabel() {
        Slot parent = CircuitView.slots.get(slotID);
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "InputPin " + Integer.toString(id++));
    }
}

class OutputPin extends Gate {
    private static int id = 0;

    public OutputPin(int slotID) {
        super(slotID);
        super.setName("OutputPin");
        label = makeLabel();

        inputs.add(new Input(this));
        outputs.clear();

        inputLimit = 1;
        gateType = GateType.OUTPUT_PIN;
    }

    @Override
    public void calculate() {
        state = inputs.get(0).getState();
    }

    @Override
    protected LabelComponent makeLabel() {
        Slot parent = CircuitView.slots.get(slotID);
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "OutputPin " + Integer.toString(id++));
    }
}



