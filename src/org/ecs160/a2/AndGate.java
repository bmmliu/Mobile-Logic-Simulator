package org.ecs160.a2;

// AND gate. True if and only if all inputs are true.
public class AndGate extends Gate {
    private static int id = 0;

    public AndGate(Slot s) {
        super(s);
        super.setName("AndGate" + id++);

        super.offImage = AppMain.theme.getImage("and_gate.jpg");
        super.onImage = AppMain.theme.getImage("and_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        outputs.add(new Output(this));

        gateType = GateType.AND_GATE;
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    @Override
    public void calculate() {
        System.out.println("Calculating AndGate");
        //Only return true if all inputs are true. If any input is false, return false.
        for (Input i : inputs) {
            if (i.getState() == State.ZERO) {
                state = State.ZERO;
                setImage();
                return;
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                System.out.println("Invalid connection detected");
                return;
            }
        }
        state = State.ONE;
        setImage();
    }

}
