package org.ecs160.a2;

// Nor gate is the opposite of an or gate. True unless at least one input is true.
class NorGate extends Gate{
    private static int id = 0;

    public NorGate(Slot slotID) {
        super(slotID);
        super.setName("NorGate" + id++);

        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("nor_gate.jpg");
        super.onImage = AppMain.theme.getImage("nor_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.NOR_GATE;
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    @Override
    public void calculate() {
        // Be ZERO if at least one input is ONE.
        state = State.ONE;
        setImage();
        for(Input i: inputs){
            if(i.getState() == State.ONE){
                state = State.ZERO;
                setImage();
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                System.out.println("Invalid connection detected");
                return;
            }
        }
    }
}