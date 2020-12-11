package org.ecs160.a2;

// InputPin is the primary way the user interacts with the subcircuit when running.
public class InputPin extends Gate {
    private static int id = 0;

    public InputPin(Slot s) {
        super(s);
        super.setName("In" + id);
        label = makeLabel("In ", id++);

        super.offImage = AppMain.theme.getImage("inputpin_0.jpg");
        super.onImage = AppMain.theme.getImage("inputpin_1.jpg");
        super.currentImage = offImage;
        tag = getLabelName();

        inputs.clear();
        outputs.add(new Output(this));

        //No inputs allowed to an input pin
        inputLimit = 0;
        minInputs = 0;

        gateType = GateType.INPUT_PIN;
        PDelay = 0;
        //Unlike other gates, inputs start out at false
        state = State.ZERO;
    }

    // Since pin values are determined elsewhere (setInput) we do not need to implement calculate.
    @Override
    public void calculate() {
    }

    public void toggle() {
        if (state == State.ZERO) {
            state = State.ONE;
            setImage();
        } else if (state == State.ONE) {
            state = State.ZERO;
            setImage();
        }
    }

    public void setInput(State s){
        state = s;
    }
}
