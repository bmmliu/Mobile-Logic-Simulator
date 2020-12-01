package org.ecs160.a2;

import com.codename1.ui.Component;

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
    protected int outputLimit; // Max number of outputs. It should be any number 0 to ...
    protected int minInputs;

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
        boolean inputsConnected = true;
        //Only necessary to calculate the state if the gate is fully connected
        for(Input input: inputs){
            if(input.getState() == State.NOT_CONNECTED){
                inputsConnected = false;
                break;
            }
        }
        if(inputsConnected && this.isConnected()){
            calculate();
        }
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
        if(inputLimit == -1){
            return false;
        }
        return inputs.size() + 1 > inputLimit;
    }

    // FIXME: Since for now gate2 will only have one output, as long as they have one output then it is an available output
    //        If this outputLimit = 0, then return false
    private Output getAvailableOutput(){
        if (gateType != GateType.SUBCIRCUIT && gateType != GateType.OUTPUT_PIN) {
            return outputs.get(0);
        }

        /*
        for(Output o: this.outputs){
            if(!o.isConnected()){
                return o;
            }
        }

         */
        return null;
    }

    public boolean connectionPossible(Gate gate2){
        //Ensure that an additional input connection is legal
        if(gate2.passedInputLimit()){
            System.out.println("Testing: gate2 have passed wire accept limit");
            return false;
        }


        // FIXME: common logic gates only have one output gate and is not limited
        //        subcircuit have can have finite number of output gate and each are limited to one connection
        //        outputPin have no output
        //Check if this gate has available outputs
        Output output = getAvailableOutput();
        if(output == null){
            return false;
        }


        return true;
    }

    //Connect this gate's output to one of gate2's inputs. Return true if a successful connection was made.
    public void connect(Gate gate2, WireComponent with){
        //Connect this output to the other gate's inputs
        Input input;
        if (gate2.gateType != GateType.SUBCIRCUIT) {    // If gate connecting to is a subcircuit, we will be expecting a existing input rather than creating a new one
            input = new Input(gate2);
            input.setConnection(getAvailableOutput(), with);
            //input.setConnection(outputs.get(0), with);
            gate2.inputs.add(input);
        }
    }

    //Disconnect this gate's output with one of gate2's inputs.
    public void disconnect(Gate gate2) {
        Output output;
        Input input;

        for (int i = 0; i < gate2.inputs.size(); i++) {
            if (gate2.inputs.get(i).getPrevOutput().getParent() == this) {
                input = gate2.inputs.get(i);
                output = gate2.inputs.get(i).getPrevOutput();
                System.out.println("Two gates are connected. Disconnecting...");
                input.disconnect();
                output.disconnect(input);
                // We don't want to remove input port for subcircuit
                if (gate2.gateType != GateType.SUBCIRCUIT) {
                    gate2.inputs.remove(input); // Remove input from gate2 because now inputs are created everytime new connection was established
                }
                break;
            }
        }
    }

    public State getState(){
        return state;
    }

    public boolean isConnected(){
        if(inputs.size() < minInputs){
            return false;
        }
        for(Input i: inputs){
            if(!i.isConnected()){
                return false;
            }
        }
        //Doesn't matter if output is connected
        return true;
    }

    //Ensure that all connected ports and their wires are disconnected
    public void delete() {
        for (Input i : inputs) {
            i.disconnect();
        }
        for (Output o : outputs) {
            o.disconnectAll();
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
        inputLimit = -1;
        minInputs = 2;
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
        inputLimit = -1;
        minInputs = 2;
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

        outputs.add(new Output(this));

        gateType = GateType.AND_GATE;
        inputLimit = -1;
        outputLimit = 1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        //Only return true if all inputs are true. If any input is false, return false.
        for(Input i: inputs){
            if(i.getState() == State.ZERO){
                state = State.ZERO;
                return;
            }
        }
        state = State.ONE;
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

        //No inputs allowed to an input pin
        inputLimit = 0;
        outputLimit = 1;
        minInputs = 0;

        gateType = GateType.INPUT_PIN;
        //Unlike other gates, inputs start out at false
        state = State.ZERO;
    }

    @Override
    public void calculate() {
    }

    public void toggle(){
        if(state == State.ZERO){
            state = State.ONE;
        }
        else if(state == State.ONE){
            state = State.ZERO;
        }
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

        outputs.clear();

        inputLimit = 1;
        outputLimit = 0;
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



