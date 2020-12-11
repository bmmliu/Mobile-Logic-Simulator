package org.ecs160.a2;

class XnorGate extends Gate{
    private static int id = 0;

    public XnorGate(Slot slotID) {
        super(slotID);
        super.setName("XnorGate" + id++);

        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("xnor_gate.jpg");
        super.onImage = AppMain.theme.getImage("xnor_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.XNOR_GATE;
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    public XnorGate(Gate g) {
        super(g);
    }

    @Override
    public void calculate() {
        Boolean oneZero = false;
        Boolean oneOne = false;
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
        if (oneOne && oneZero) {state = State.ZERO;}
        else {state = State.ONE;}
        setImage();
    }
}