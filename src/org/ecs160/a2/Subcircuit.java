package org.ecs160.a2;

// Create shell subcircuit with 3 inputs and 4 outputs for now
// Each inputs and outputs should be able to establish connections with other ports
//  Depends on the order of wiring, prompt user to choose which connections to use
//  Check if the user's choice of output is connected
//      If output is connected and is connected to target input, disconnect
//      If output is not connected, establish connection
// When established connection

import com.codename1.ui.Display;

public class Subcircuit extends Gate{
    private static int id = 0; // id should match one of five the save slots
    private TruthTable truthTable;
    private String[] inputLabel;
    private String[] outputLabel;

    public static int inputInterest = 0;
    public static int outputInterest = 0;

    // Note: that subcircuit's state is not singular, so it will always be in "not_connected" state.
    //       it should not be a concern as subcircuit will simply modify its outputs' state directly
    public Subcircuit (Slot s, TruthTable t, int circuitId) {
        super(s);
        super.setName("Subcircuit" + circuitId + "_" + id++);
        truthTable = t;

        switch(circuitId) {
            case 0:
                super.offImage = AppMain.theme.getImage("circuit_0.jpg");
                super.onImage = AppMain.theme.getImage("circuit_0_c.jpg"); // TODO: Add onImage
                break;
            case 1:
                super.offImage = AppMain.theme.getImage("circuit_1.jpg");
                super.onImage = AppMain.theme.getImage("circuit_1_c.jpg"); // TODO: Add onImage
                break;
            case 2:
                super.offImage = AppMain.theme.getImage("circuit_2.jpg");
                super.onImage = AppMain.theme.getImage("circuit_2_c.jpg"); // TODO: Add onImage
                break;
            case 3:
                super.offImage = AppMain.theme.getImage("circuit_3.jpg");
                super.onImage = AppMain.theme.getImage("circuit_3_c.jpg"); // TODO: Add onImage
                break;
            case 4:
                super.offImage = AppMain.theme.getImage("circuit_4.jpg");
                super.onImage = AppMain.theme.getImage("circuit_4_c.jpg"); // TODO: Add onImage
                break;
        }
        super.currentImage = offImage;

        initPorts();

        gateType = GateType.SUBCIRCUIT;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();

        inputPicker.setType(Display.PICKER_TYPE_STRINGS);
        inputPicker.setStrings(inputLabel);

        outputPicker.setType(Display.PICKER_TYPE_STRINGS);
        outputPicker.setStrings(outputLabel);
    }

    // The number of ports on the subcircuit should be initizated and fixed
    private void initPorts() {
        // FIXME: Create shell subcircuit with 3 inputs and 4 outputs for now
        inputLimit = truthTable.getInputPinNames().length;            // TODO: inputLimit should be based on # of input pins
        minInputs = inputLimit;     // Since we wouldn't know which inputs in subcircuit are necessary, we will make all inputs necessary
        numOutputs = truthTable.getOutputPinNames().length;             // TODO: numOutputs should be based on # of output pins

        for (int i = 0; i < inputLimit; i++) {
            inputs.add(new Input(this));
        }
        for (int o = 0; o < numOutputs; o++) {
            outputs.add(new Output(this));
        }

        // +1 to house Cancel option
        inputLabel = new String[inputLimit+1];
        outputLabel = new String[numOutputs+1];

        inputLabel[0] = "Cancel";
        for (int i = 1; i < inputLimit+1; i++) {
            inputLabel[i] = "Input " + i;
        }
        outputLabel[0] = "Cancel";
        for (int o = 1; o < numOutputs+1; o++) {
            outputLabel[o] = "Ouput " + o;
        }
    }

    @Override
    public void calculate() {
        // By reading the inputs of this subcircuit in order, set the state of each
        //If subcircuit is not connected, set all output pins to NOT_CONNECTED
        if(!isConnected()){
            for(Output o: outputs){
                o.setState(State.NOT_CONNECTED);
            }
            return;
        }
        //Look up the outputs in the truth table corresponding to these inputs, set the outputs in order
        State[] inputCombination = new State[minInputs];
        for (int i = 0; i < inputCombination.length; i++) {
            inputCombination[i] = inputs.get(i).getState();
        }

        State[] outputCombination = truthTable.findOutputs(inputCombination);
        for(int i = 0; i<outputCombination.length; i++){
            this.outputs.get(i).setState(outputCombination[i]);
        }
    }
}
