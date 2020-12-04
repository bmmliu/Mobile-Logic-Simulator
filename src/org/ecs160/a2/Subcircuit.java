package org.ecs160.a2;

public class Subcircuit extends Gate{
    private int id = 0; // id should match one of five the save slots
    private TruthTable table;

    // Note: that subcircuit's state is not singular, so it will always be in "not_connected" state.
    //       it should not be a concern as subcircuit will simply modify its outputs' state directly
    public Subcircuit (Slot s, TruthTable t, int circuitId) {
        super(s);
        super.setName("Subcircuit" + circuitId);
        id = circuitId;
        table = t;

        switch(circuitId) {
            case 0:
                super.offImage = AppMain.theme.getImage("circuit_0.jpg");
                super.onImage = AppMain.theme.getImage("circuit_0.jpg"); // TODO: Add onImage
                break;
            case 1:
                super.offImage = AppMain.theme.getImage("circuit_1.jpg");
                super.onImage = AppMain.theme.getImage("circuit_1.jpg"); // TODO: Add onImage
                break;
            case 2:
                super.offImage = AppMain.theme.getImage("circuit_2.jpg");
                super.onImage = AppMain.theme.getImage("circuit_2.jpg"); // TODO: Add onImage
                break;
            case 3:
                super.offImage = AppMain.theme.getImage("circuit_3.jpg");
                super.onImage = AppMain.theme.getImage("circuit_3.jpg"); // TODO: Add onImage
                break;
            case 4:
                super.offImage = AppMain.theme.getImage("circuit_4.jpg");
                super.onImage = AppMain.theme.getImage("circuit_4.jpg"); // TODO: Add onImage
                break;
        }
        super.currentImage = offImage;

        outputs.add(new Output(this));

        gateType = GateType.SUBCIRCUIT;
        inputLimit = -1;            // TODO: inputLimit should be based on # of rows of the truthTable
        minInputs = inputLimit;     // Since we wouldn't know which inputs in subcircuit are necessary, we will make all inputs necessary
        numOutputs = 1;             // TODO: numOutputs should be based on # of cols of the truthTable

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    // The number of ports on the subcircuit should be initizated and fixed
    private void initPorts() {

    }

    @Override
    public void calculate() {
        // By reading the inputs of this subcircuit in order, set the state of each
    }
}
