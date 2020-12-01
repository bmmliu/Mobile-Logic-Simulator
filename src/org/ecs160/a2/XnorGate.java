package org.ecs160.a2;

class XnorGate extends Gate{
    private static int id = 0;

    public XnorGate(int slotID) {
        super(slotID);
        super.setName("XnorGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

        gateType = GateType.XNOR_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        // Same as XOR, with the default and conditional state= swapped.
        Boolean oneZero = false;
        Boolean oneOne = false;
        state = State.ONE;
        for(Input i: inputs){
            switch (i.getState()) {
                case ONE: oneOne = true;
                case ZERO: oneZero = true;
            }
        }
        if (oneOne && oneZero) {state = State.ZERO;}
    }
}