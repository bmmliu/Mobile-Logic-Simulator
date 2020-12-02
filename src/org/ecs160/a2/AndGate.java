package org.ecs160.a2;

public class AndGate extends Gate {
    private static int id = 0;

    public AndGate(Slot s) {
        super(s);
        super.setName("AndGate");
        label = makeLabel(this.getName(), id++);

        super.offImage = AppMain.theme.getImage("and_gate.jpg");
        super.onImage = AppMain.theme.getImage("nand_gate.jpg"); // TODO: Add onImage
        super.currentImage = offImage;
        name = getLabelName();

        outputs.add(new Output(this));

        gateType = GateType.AND_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        //Only return true if all inputs are true. If any input is false, return false.
        for (Input i : inputs) {
            if (i.getState() == State.ZERO) {
                state = State.ZERO;
                setImage();
                return;
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                System.out.println("Invalid connection detected");
                return;
            }
        }
        state = State.ONE;
        setImage();
    }

}
