package org.ecs160.a2;

import com.codename1.components.ToastBar;
import com.codename1.io.Externalizable;
import com.codename1.io.Util;
import com.codename1.ui.Component;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.spinner.Picker;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

enum GateType{P_1, P_2, INPUT_PIN, OUTPUT_PIN, AND_GATE, OR_GATE, NAND_GATE, NOR_GATE, NOT_GATE, XOR_GATE, XNOR_GATE, SUBCIRCUIT};
enum State{ZERO, ONE, NOT_CONNECTED, CRITICAL};

public abstract class Gate extends Component implements Externalizable {
    // FIXME: For subcircuit only, I don't how to properly handle this using our structure
    protected Picker inputPicker = new Picker();
    protected Picker outputPicker = new Picker();
    protected static ActionListener pickerListener = evt -> {};

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
    protected int PDelay = 1;

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

        for (Output output : outputs) {
            if (this.gateType != GateType.SUBCIRCUIT) { // Subcircuit do not have unified state, thus this loop is not applicable to subcircuit
                output.setState(state);
            }
            output.updateConnectedInput();              // If this gate is a subcircuit, we assume that the outputs of subcircuit already been updated
        }
    }

    public abstract void calculate();

    //If this Gate's output is connected to any of gate2's inputs, these gates are connected
    public boolean isConnectedTo(Gate gate2){
        // TODO: Remove println after finish debugging
        // Connecting to subCircuit should check the particular gate, which handles during connect
        if (this.gateType == GateType.SUBCIRCUIT && gate2.gateType != GateType.SUBCIRCUIT) {
            System.out.println(outputs.size() + " to " + Subcircuit.outputInterest);
            System.out.println("Checking connection between " + this.outputs.get(Subcircuit.outputInterest).getPortParent().getLabelName() + " and " + gate2.getLabelName());
            System.out.println("With  " + Subcircuit.outputInterest);

            for (Input i : this.outputs.get(Subcircuit.outputInterest).getConnectedInputs()) {
                System.out.println("Existing connections: " + i.getPortParent().getLabelName());
            }

            // if gate2 is not subcircuit, check if outputInterest is connected to gate2, disconnect if it is
            for (Input i : this.outputs.get(Subcircuit.outputInterest).getConnectedInputs()) {
                System.out.println("Checking " + i.getPortParent().getLabelName() + " to " + gate2.getLabelName());
                if (i.getPortParent() == gate2) {
                    System.out.println(i.getPortParent().getLabelName() + " is " + gate2.getLabelName());
                    return true;
                }
            }
            return false;
            /*
            if (this.outputs.get(Subcircuit.outputInterest).getConnectedInputs() == gate2) {
                System.out.println("Output " + Subcircuit.outputInterest + " is connected to gate2");
                return true;
            }

             */
        }

        // If gate1 and 2 are both subcircuit, we handle disconnect in makeConnect
        // If gate1 is not subcircuit, but gate2 is, handle disconnect in makeConnect
        // In other words, as long as gate2 is subCircuit, handle disconnect in makeConnect
        if (gate2.gateType == GateType.SUBCIRCUIT) return false;

        for (Input i: gate2.inputs)
            for(Output o: this.outputs)
                if(i.getPrevOutput() == o) return true;

        return false;
    }

    private boolean passedInputLimit(){
        if(inputLimit == -1) return false;

        if (this.gateType == GateType.SUBCIRCUIT) // If this gate is subcircuit, we will never exist input limit
            return false;

        return inputs.size() + 1 > inputLimit;
    }

    // FIXME: Since for now gate2 will only have one output, as long as they have one output then it is an available output
    //        If this outputLimit = 0, then return false
    private Output getAvailableOutput(){
        // For SubCircuit, output checking depends on which output the user chooses
        if (gateType == GateType.SUBCIRCUIT) {
            return outputs.get(Subcircuit.outputInterest);
        }
        // For OutputPin, there are no outputs
        if (gateType == GateType.OUTPUT_PIN) {
            return null;
        }
        return outputs.get(0);
    }

    public boolean connectionPossible(Gate gate2){
        //Ensure that an additional input connection is legal
        if(gate2.passedInputLimit()){
            System.out.println("No additional input can be added");
            return false;
        }

        //Check if this gate has available outputs
        Output output = getAvailableOutput();
        if(output == null){
            System.out.println("No output have been retrieved");
            return false;
        }

        return true;
    }

    //Connect this gate's output to one of gate2's inputs
    public void connect(Gate gate2, WireComponent with){
        //Connect this output to the other gate's inputs
        Input input;
        Output output;
        if (this.gateType == GateType.SUBCIRCUIT) {             // If this is subcircuit, handle the disconnection here
            output = outputs.get(Subcircuit.outputInterest);    // We should've already got the outputInterest already
        } else {
            output = getAvailableOutput();
        }
        if (gate2.gateType == GateType.SUBCIRCUIT) {    // If gate connecting to is a subcircuit, we will be expecting a existing input rather than creating a new one
            gate2.SCInputConnectTo(output, with);
        } else {
            input = new Input(gate2);
            input.setConnection(output, with);
            gate2.inputs.add(input);
            //ToastBar.showMessage( "Connection Establshed", FontImage.MATERIAL_INFO);
        }
    }

    private void SCInputConnectTo(Output output, WireComponent with) {
        pickerListener = e -> {
            e.consume();

            // TODO: Maybe redraw wire in here based on user choice
            Subcircuit.inputInterest = inputPicker.getSelectedStringIndex() - 1;
            if (Subcircuit.inputInterest == -1) {   // If user pick Cancel
                //ToastBar.showMessage( "Cancelled Connection", FontImage.MATERIAL_INFO);
                with.getParent().removeComponent(with);
            } else {    // If user did not pick Cancel
                Input input = inputs.get(Subcircuit.inputInterest);
                // if gate2's (subCircuit) chosen input is not available and different gate, do not establish connection
                //                                      is not available and same gate, disconnect
                //                                      is available, connect
                if (input.isConnected()) {
                    if (input.getPrevOutput() == output) {
                        System.out.println("Disconnecting from subcircuit input");
                        with.getParent().removeComponent(with);
                        input.disconnect();
                    } else {
                        with.getParent().removeComponent(with); // NOTE: This is to integrate the messes code from no-subCircuit connection
                        System.out.println("SubCiruit input is not available");
                    }
                } else {
                    input.setConnection(output, with);    // Get AvailableOutput is special for subCircuit
                    //ToastBar.showMessage( "Connection Establshed", FontImage.MATERIAL_INFO);
                }
            }

            CircuitView.simulator.menuDisplay.removeComponent(inputPicker);
            CircuitView.simulator.menuDisplay.revalidate();

            inputPicker.removeActionListener(pickerListener);
        };

        inputPicker.addActionListener(pickerListener);
        CircuitView.simulator.menuDisplay.add(BorderLayout.SOUTH, inputPicker);
        CircuitView.simulator.menuDisplay.revalidate();
        inputPicker.pressed();
        inputPicker.released();
    }

    // This function is for subcircuit only
    public void promptSCOuputPort() {
        pickerListener = e -> {
            e.consume();

            Subcircuit.outputInterest = outputPicker.getSelectedStringIndex() - 1;
            //System.out.println("Output " + Subcircuit.outputInterest);

            CircuitView.simulator.menuDisplay.removeComponent(outputPicker);
            CircuitView.simulator.menuDisplay.revalidate();

            outputPicker.removeActionListener(pickerListener);
        };

        outputPicker.addActionListener(pickerListener);
        CircuitView.simulator.menuDisplay.add(BorderLayout.SOUTH, outputPicker);
        CircuitView.simulator.menuDisplay.revalidate();
        outputPicker.pressed();
        outputPicker.released();
    }

    /*
    // TODO: Let 0 be input, 1 be output
    public int getOffset(int portType) {
        if (gateType == GateType.SUBCIRCUIT) {
            if (portType == 0) {
                System.out.println(parent.getHeight()/inputLimit * Subcircuit.inputInterest);
                return parent.getHeight()/inputLimit * Subcircuit.inputInterest;
            } else {
                return parent.getHeight()/numOutputs * Subcircuit.outputInterest;
            }
        } else {
            return parent.getHeight() / 2;
        }
    }

     */

    //Disconnect this gate's output with one of gate2's inputs.
    public void disconnect(Gate gate2) {
        Output output;
        Input input;

        if (this.gateType == GateType.SUBCIRCUIT) {
            input = null;
            output = outputs.get(Subcircuit.outputInterest);
            for (Input i : output.getConnectedInputs()) {
                if (i.getPortParent() == gate2) {
                    input = i;
                }
            }

            input.disconnect();
            output.disconnect(input);

            // We don't want to remove input port for subcircuit
            if (gate2.gateType != GateType.SUBCIRCUIT) {
                gate2.inputs.remove(input); // Remove input from gate2 because now inputs are created everytime new connection was established
            }
            return;
        }

        /*
        if (gate2.gateType == GateType.SUBCIRCUIT) {

        }

         */
        for (Input i : gate2.inputs) {
            if (i.getPrevOutput().getPortParent() == this) {
                input = i;
                output = i.getPrevOutput();

                input.disconnect();
                output.disconnect(input);

                // We don't want to remove input port for subcircuit
                if (gate2.gateType != GateType.SUBCIRCUIT) {
                    gate2.inputs.remove(input); // Remove input from gate2 because now inputs are created everytime new connection was established
                }
                return;
            }
        }

        /*
        for (int i = 0; i < gate2.inputs.size(); i++) {
            if (gate2.inputs.get(i).getPrevOutput().getPortParent() == this) {
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

         */
    }

    protected String updatePortNumTag(int crementCount) {
        if (this.gateType == GateType.INPUT_PIN) {
            System.out.println("Function should not be called: @Gate.java for updateGateNumTag");
        } else if (this.gateType == GateType.OUTPUT_PIN || this.gateType == GateType.SUBCIRCUIT) {
            return tag;
        }

        String portNumTag;
        if (inputs.size() + crementCount > minInputs) {
            portNumTag = Integer.toString(inputs.size() + crementCount) + "     " + Integer.toString(numOutputs);
        } else {
            portNumTag = Integer.toString(minInputs) + "     " + Integer.toString(numOutputs);
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
        int offsetX = parent.getWidth()/3 + 6;
        int offsetY = parent.getHeight()/2 - 5;
        String name = gateName + Integer.toString(uid);

        return new LabelComponent(parent.getAbsoluteX()+offsetX, parent.getAbsoluteY()-offsetY, name);
    }

    protected LabelComponent makeLabel(int numInput, int numOutput) {
        int offsetX = parent.getWidth()/3;
        int offsetY = parent.getHeight()/2 - 5;
        String name = Integer.toString(numInput) + "     " + Integer.toString(numOutput);

        return new LabelComponent(parent.getAbsoluteX()+offsetX, parent.getAbsoluteY()-offsetY, name);
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
                setLabel(new LabelComponent(this.label, "   "+Integer.toString(PDelay)));
            } else {
                setLabel(new LabelComponent(this.label, tag));
            }
        }
    }

    public void setPDelay(int newDelay) {
        System.out.println(newDelay);
        PDelay = newDelay;
        setLabel(new LabelComponent(this.label, "    "+Integer.toString(newDelay)));
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
            case CRITICAL:
                redrawWire();
                if (gateType != GateType.INPUT_PIN || gateType != GateType.OUTPUT_PIN) {
                    currentImage = onImage;
                    parent.update();
                }
                break;
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
            case CRITICAL:
                color = Wire.PURPLE;
                break;
            default:
                color = Wire.RED;
                break;
        }

        if (this.gateType == GateType.SUBCIRCUIT && this.state != State.CRITICAL) {
            for (Output o : outputs) {
                for (Input i : o.getConnectedInputs()) {
                    switch (o.getState()) {
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
                    i.redrawWire(new WireComponent(i.getWire(), color));
                }
            }
        }

        for (Output o : outputs)
            for (Input i : o.getConnectedInputs()) {
                if (this.state == State.CRITICAL) {
                    if (i.getPortParent().state == State.CRITICAL) {
                        i.redrawWire(new WireComponent(i.getWire(), color));
                    }
                }
                else i.redrawWire(new WireComponent(i.getWire(), color));
            }
    }
// Externalizable stuff

    @Override
    public int getVersion() {return 1;}

    @Override
    public void externalize(DataOutputStream out) throws IOException {

        // Util.writeObject(inputPicker, out);
        // Util.writeObject(outputPicker, out);
        // Util.writeObject(pickerListener, out);

        Util.writeObject(inputs, out);
        Util.writeObject(outputs, out);
        // Util.writeObject(slotID, out);
        out.writeInt(slotID);
        Util.writeObject(parent, out);
        Util.writeObject(label, out);
        // Util.writeObject(tag, out);
        Util.writeUTF(tag, out);
        Util.writeObject(gateType, out);

        Util.writeObject(offImage, out);
        Util.writeObject(onImage, out);
        Util.writeObject(currentImage, out);
        //Util.writeObject(PDelay, out);
        out.writeInt(PDelay);
        out.writeInt(inputLimit);
        out.writeInt(numOutputs);
        out.writeInt(minInputs);

    }

    // ALL CLASSES need to register themselves to Util
    static { Util.register("Gate", Gate.class); }

    @Override
    public void internalize(int version, DataInputStream in) throws IOException {
        Util.register("Gate", Gate.class);

        // inputPicker = (Picker) Util.readObject(in);
        // outputPicker = (Picker) Util.readObject(in);
        // pickerListener = (ActionListener) Util.readObject(in);

        inputPicker = new Picker();
        outputPicker = new Picker();
        pickerListener = evt -> {};

        inputs = (ArrayList<Input>) Util.readObject(in);
        outputs = (ArrayList<Output>) Util.readObject(in);
        slotID = in.readInt();
        parent = (Slot) Util.readObject(in);
        label = (LabelComponent) Util.readObject(in);
        tag = Util.readUTF(in);
        gateType = (GateType) Util.readObject(in);

        offImage = (Image) Util.readObject(in);
        onImage = (Image) Util.readObject(in);
        currentImage  = (Image) Util.readObject(in);

        PDelay = in.readInt();
        inputLimit = in.readInt();
        numOutputs = in.readInt();
        minInputs = in.readInt();

    }

    @Override
    public String getObjectId() {
        return "Gate";
    }


}
