package org.ecs160.a2;

public class OrGate extends Gate {
    private static int id = 0;

    public OrGate(Slot slotID) {
        super(slotID);
        super.setName("OrGate" + id++);

        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("or_gate.jpg");
        super.onImage = AppMain.theme.getImage("or_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.OR_GATE;
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    public OrGate(Gate g) {
        super(g);
    }

    @Override
    public void calculate() {
        // Return true if at least one input is true.
        State outputState = State.ZERO;
        for(Input i: inputs){
            if(i.getState() == State.ONE) {
                outputState = State.ONE;
                break;
            }
            else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                System.out.println("Invalid connection detected");
                return;
            }
        }
        state = outputState;
        setImage();
    }
}
