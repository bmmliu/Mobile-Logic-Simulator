package org.ecs160.a2;

/**
 * Output pin of a circuit. Output pins have no output ports, and only contain a single input port.
 * Their image changes depending on their state.
 */
public class OutputPin extends Gate {
    private static int id = 0;

    public OutputPin(Slot s) {
        super(s);
        super.setName("Out" + id);
        label = makeLabel("Out", id++);

        super.offImage = AppMain.theme.getImage("outputpin_0.jpg");
        super.onImage = AppMain.theme.getImage("outputpin_1.jpg");
        super.currentImage = offImage;
        tag = getLabelName();

        outputs.clear();

        inputLimit = 1;
        minInputs = 1;
        numOutputs = 0;
        gateType = GateType.OUTPUT_PIN;
        PDelay = 0;
    }

    public OutputPin(Gate g) {
        super(g);
    }

    @Override
    public void calculate() {
        state = inputs.get(0).getState();
        setImage();
    }

}
