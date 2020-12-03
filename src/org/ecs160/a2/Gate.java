package org.ecs160.a2;

import com.codename1.ui.Component;
import com.codename1.ui.Image;

import java.util.ArrayList;


enum GateType{P_1, P_2, INPUT_PIN, OUTPUT_PIN, AND_GATE, OR_GATE, NAND_GATE, NOR_GATE, NOT_GATE, XOR_GATE, XNOR_GATE, SUBCIRCUIT};
enum State{ZERO, ONE, NOT_CONNECTED};

public abstract class Gate extends Component {
    ArrayList<Input> inputs = new ArrayList<Input>();
    ArrayList<Output> outputs = new ArrayList<Output>();
    protected State state = State.NOT_CONNECTED;
    int slotID = 0;
    Slot parent;
    LabelComponent label = null;
    protected String tag = null;
    protected GateType gateType = null;
    protected Image offImage = null;
    protected Image onImage = null;
    Image currentImage = null;
    protected int PDelay = 0;

    protected int inputLimit; //Max number of inputs. If inputLimit is -1, any number of inputs is possible
    protected int numOutputs = 1; // Max number of outputs. It should be any number 0 to ...
    protected int minInputs;

    public Gate(Slot s) {
        parent = s;
        inputLimit = -1;
    }

    public Image getOffImage() {
        return offImage;
    }

    public Image getOnImage() {
        return onImage;
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
        if (this.gateType != GateType.SUBCIRCUIT) {     // Subcircuit do not have unified state, thus this loop is not applicable to subcircuit
            for (Output output : outputs) {             // However, similar slip of code here might be necessary for Subcircuit's calculate function
                output.setState(state);
                output.updateConnectedInput();
            }
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
        if (!inputs.isEmpty()) {
            Input i = inputs.get(0);
            Output o = i.getPrevOutput();
            Gate g = o.getParent();
            GateType gt = g.gateType;
            System.out.println(gt);
        }
        return inputs.size() + 1 > inputLimit;
    }

    // FIXME: Since for now gate2 will only have one output, as long as they have one output then it is an available output
    //        If this outputLimit = 0, then return false
    private Output getAvailableOutput(){
        if (gateType != GateType.SUBCIRCUIT && gateType != GateType.OUTPUT_PIN) {
            return outputs.get(0);
        }
        return null;
    }

    public boolean connectionPossible(Gate gate2){
        //Ensure that an additional input connection is legal
        if(gate2.passedInputLimit()){
            System.out.println("No additional input can be added");
            return false;
        }

        // FIXME: common logic gates only have one output gate and is not limited
        //        subcircuit have can have finite number of output gate and each are limited to one connection
        //        outputPin have no output
        //Check if this gate has available outputs
        Output output = getAvailableOutput();
        if(output == null){
            System.out.println("No output have been retrieved");
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
                //System.out.println("Two gates are connected. Disconnecting...");
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

    protected String updatePortNumTag(int crementCount) {
        if (this.gateType == GateType.INPUT_PIN) {
            System.out.println("Function should not be called: @Gate.java for updateGateNumTag");
        } else if (this.gateType == GateType.OUTPUT_PIN) {
            return tag;
        }

        String portNumTag;
        if (inputs.size() + crementCount > minInputs) {
            portNumTag = "       " + Integer.toString(inputs.size() + crementCount) + "    " + Integer.toString(numOutputs);
        } else {
            portNumTag = "       " + Integer.toString(minInputs) + "    " + Integer.toString(numOutputs);
        }

        tag = portNumTag;
        return portNumTag;
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

    protected LabelComponent makeLabel(String gateName, int uid) {
        int offsetX = parent.getWidth()/6;
        int offsetY = parent.getHeight()/2;
        String name = gateName + Integer.toString(uid);

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, name);
    }

    protected LabelComponent makeLabel(int numInput, int numOutput) {
        int offsetX = parent.getWidth()/6;
        int offsetY = parent.getHeight()/2;
        String name = "       " + Integer.toString(numInput) + "    " + Integer.toString(numOutput);

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, name);
    }

    public String getLabelName() {
        return this.getName();
    }

    public void setLabel(LabelComponent label) {
        this.label.getParent().replace(this.label, label, null);
        this.label = label;
    }

    public LabelComponent getLabel() {
        return label;
    }

    public void swapLabel(UserMode m) {
        if (gateType != GateType.INPUT_PIN && gateType != GateType.OUTPUT_PIN) {
            if (m == UserMode.PDELAY) {
                setLabel(new LabelComponent(this.label, "        "+Integer.toString(PDelay)));
            } else {
                setLabel(new LabelComponent(this.label, tag));
            }
        }
    }

    public void setPDelay(int newDelay) {
        System.out.println(newDelay);
        PDelay = newDelay;
        setLabel(new LabelComponent(this.label, "        "+Integer.toString(newDelay)));
    }


    public GateType getGateType(){
        return this.gateType;
    }

    protected void setImage() {
        switch (state) {
            case ZERO:
                redrawWire();
                currentImage = offImage;
                parent.update();
                return;
            case ONE:
                redrawWire();
                if (gateType == GateType.INPUT_PIN || gateType == GateType.OUTPUT_PIN) {
                    currentImage = onImage;
                    parent.update();
                }
                return;
            default:
                System.out.println("Error at Gate.Java's setImage");
                return;
        }
        /*
        if (state == State.ZERO) {
            redrawWire();
            currentImage = offImage;
            parent.update();
        } else if (state == State.ONE) {
            redrawWire();
            currentImage = onImage;
            parent.update();
        } else if (state == State.NOT_CONNECTED) {
            System.out.println("Error at Gate.Java's setImage");
        }

         */
    }

    private void redrawWire() {
        int color;

        switch(state) {
            case ZERO:
                color = Wire.DARK_GREEN;
                break;
            case ONE:
                color = Wire.GREEN;
                break;
            default:
                color = Wire.RED;
                break;
        }

        for (Output o : outputs) {
            for (Input i : o.getConnectedInputs()) {
                //System.out.println(i.getParent().getLabelName());
                i.redrawWire(new WireComponent(i.getWire(), color));
            }
        }
    }
}
