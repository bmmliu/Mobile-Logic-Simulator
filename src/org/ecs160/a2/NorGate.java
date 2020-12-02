package org.ecs160.a2;

class NorGate extends Gate{
    private static int id = 0;

    public NorGate(Slot slotID) {
        super(slotID);
        super.setName("NorGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

        super.offImage = AppMain.theme.getImage("nor_gate.jpg");
        super.onImage = AppMain.theme.getImage("nor_gate.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.NOR_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        // Be ZERO if at least one input is ONE.
        state = State.ONE;
        for(Input i: inputs){
            if(i.getState() == State.ONE){state = State.ZERO;}
        }
    }
}