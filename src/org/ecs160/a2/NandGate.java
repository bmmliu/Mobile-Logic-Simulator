package org.ecs160.a2;

class NandGate extends Gate{
    private static int id = 0;

    public NandGate(Slot s) {
        super(s);
        super.setName("NandGate");
        label = makeLabel(this.getName(), id++);

        outputs.add(new Output(this));

        gateType = GateType.NAND_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        // Just NOT'ing the and gate
        for(Input i: inputs){
            if(i.getState() == State.ZERO){
                state = State.ONE;
                return;
            }
        }
        state = State.ZERO;
    }
}