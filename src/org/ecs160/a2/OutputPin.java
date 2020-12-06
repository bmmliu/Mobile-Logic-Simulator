package org.ecs160.a2;

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
    }

    @Override
    public void calculate() {
        state = inputs.get(0).getState();
        setImage();
    }

}
