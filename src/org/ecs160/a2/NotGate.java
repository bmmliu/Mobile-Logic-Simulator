package org.ecs160.a2;

class NotGate extends Gate{
    private static int id = 0;

    public NotGate(Slot slotID) {
        super(slotID);
        super.setName("NotGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("not_gate.jpg");
        super.onImage = AppMain.theme.getImage("not_gate.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.NOT_GATE;
        inputLimit = 1;
        minInputs = 1;
    }

    @Override
    public void calculate() {
        state = State.ZERO;
        for(Input i: inputs){
            if(i.getState() == State.ZERO){state = State.ONE;}
        }
    }
}