package org.ecs160.a2;

class NotGate extends Gate{
    private static int id = 0;

    public NotGate(Slot slotID) {
        super(slotID);
        super.setName("NotGate" + id++);

        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("not_gate.jpg");
        super.onImage = AppMain.theme.getImage("not_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.NOT_GATE;
        //Not gates can only have one input
        inputLimit = 1;
        minInputs = 1;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    public NotGate(Gate g) {
        super(g);
    }

    @Override
    public void calculate() {
        state = State.ZERO;
        for(Input i: inputs){
            if(i.getState() == State.ZERO){
                state = State.ONE;
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                System.out.println("Invalid connection detected");
                return;
            }
        }
        setImage();
    }
}