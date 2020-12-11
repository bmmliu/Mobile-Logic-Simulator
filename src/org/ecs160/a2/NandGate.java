package org.ecs160.a2;

/**
 *
 */
class NandGate extends Gate{
    private static int id = 0;

    public NandGate(Slot s) {
        super(s);
        super.setName("NandGate" + id++);

        outputs.add(new Output(this));
        
        super.offImage = AppMain.theme.getImage("nand_gate.jpg");
        super.onImage = AppMain.theme.getImage("nand_gate_c.jpg"); // TODO: Add onImage
        super.currentImage = offImage;

        gateType = GateType.NAND_GATE;
        inputLimit = -1;
        minInputs = 2;

        label = makeLabel(minInputs, numOutputs);
        tag = label.getName();
    }

    public NandGate(Gate g) {
        super(g);
    }

    @Override
    public void calculate() {
        // Opposite of the and gate; just copied and pasted the and gate code but swapped output value of State.ONE and
        // State.ZERO
        for(Input i: inputs){
            if(i.getState() == State.ZERO){
                state = State.ONE;
                setImage();
                return;
            } else if (i.getState() == State.NOT_CONNECTED) {
                state = State.NOT_CONNECTED;
                setImage();
                System.out.println("Invalid connection detected");
                return;
            }
        }
        state = State.ZERO;
        setImage();
    }
}