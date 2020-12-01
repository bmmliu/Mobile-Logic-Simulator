package org.ecs160.a2;

class XorGate extends Gate{
    private static int id = 0;

    public XorGate(int slotID) {
        super(slotID);
        super.setName("XorGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

        gateType = GateType.XOR_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        Boolean oneZero = false;
        Boolean oneOne = false;
        state = State.ZERO;
        for(Input i: inputs){
            switch (i.getState()) {
                case ONE: oneOne = true;
                case ZERO: oneZero = true;
            }
        }
        if (oneOne && oneZero) {state = State.ONE;}
    }
}