package org.ecs160.a2;

class NorGate extends Gate{
    private static int id = 0;

    public NorGate(int slotID) {
        super(slotID);
        super.setName("NorGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

        gateType = GateType.NOR_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        // Return true if at least one input is true.
        State outputState = State.ONE;
        for(Input i: inputs){
            if(i.getState() == State.ONE){outputState = State.ZERO; break;}
        }
        state = outputState;
    }
}