package org.ecs160.a2;

class NotGate extends Gate{
    private static int id = 0;

    public NotGate(int slotID) {
        super(slotID);
        super.setName("NotGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

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