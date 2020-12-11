package org.ecs160.a2;

class XorGate extends Gate{
    private static int id = 0;

    public XorGate(Slot slotID) {
        super(slotID);
        super.setName("XorGate" + id++);

        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("xor_gate.jpg");
        super.onImage = AppMain.theme.getImage("xor_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.XOR_GATE;
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    public XorGate(Gate g) {
        super(g);
    }

    @Override
    public void calculate() {
        boolean oneZero = false;
        boolean oneOne = false;
        for(Input i: inputs){
            if (i.getState() == State.ZERO) {
                oneZero = true;
            } else if (i.getState() == State.ONE) {
                oneOne = true;
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                System.out.println("Invalid connection detected");
                return;
            }
        }
        if (oneOne && oneZero) {state = State.ONE;}
        else {state = State.ZERO;}
        setImage();
    }
}