package org.ecs160.a2;

public class AndGate extends Gate {
    private static int id = 0;

    public AndGate(Slot s) {
        super(s);
        super.setName("AndGate" + id++);

        super.offImage = AppMain.theme.getImage("and_gate.jpg");
        super.onImage = AppMain.theme.getImage("and_gate_c.jpg");
        super.currentImage = offImage;

        outputs.add(new Output(this));

        gateType = GateType.AND_GATE;
        //And gates can have at least 2 inputs
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    public AndGate(Gate g) {
        super(g);
    }

    @Override
    /**
     * Only return true if all inputs are true. If any input is false, return false.
     */
    public void calculate() {

        for (Input i : inputs) {
            if (i.getState() == State.ZERO) {
                state = State.ZERO;
                setImage();
                return;
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                return;
            }
        }
        state = State.ONE;
        setImage();
    }

}
