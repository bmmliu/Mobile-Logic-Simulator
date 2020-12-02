package org.ecs160.a2;

class NandGate extends Gate{
    private static int id = 0;

    public NandGate(Slot s) {
        super(s);
        super.setName("NandGate");
        label = makeLabel(this.getName(), id++);

        outputs.add(new Output(this));
        
        super.offImage = AppMain.theme.getImage("nand_gate.jpg");
        super.onImage = AppMain.theme.getImage("nand_gate.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.NAND_GATE;
        inputLimit = -1;
        minInputs = 2;
        // TODO: Check if we need output limit?
    }

    @Override
    public void calculate() {
        // Opposite of the and gate; just copied and pasted the and gate code but swapped output value of State.ONE and
        // State.ZERO
        for(Input i: inputs){
            if(i.getState() == State.ZERO){
                state = State.ONE;
                return;
            }
        }
        state = State.ZERO;
    }
}