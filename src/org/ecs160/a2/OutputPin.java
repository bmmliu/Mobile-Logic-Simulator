package org.ecs160.a2;


// OUTPUT PIN is central to the calculations of the app. Is a gate
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

    @Override
    public void calculate() {
        state = inputs.get(0).getState();
        setImage();
    }

}
