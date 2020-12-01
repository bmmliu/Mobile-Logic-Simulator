package org.ecs160.a2;

public class OrGate extends Gate {
    private static int id = 0;

    public OrGate(int slotID) {
        super(slotID);
        super.setName("OrGate");
        label = makeLabel(this.getName(), id++);
        outputs.add(new Output(this));

        // Set state?

        gateType = GateType.OR_GATE;
        inputLimit = -1;
        minInputs = 2;
    }

    @Override
    public void calculate() {
        // Return true if at least one input is true.
        State outputState = State.ZERO;
        for(Input i: inputs){
            if(i.getState() == State.ONE){outputState = State.ONE; break;}
        }
        state = outputState;
    }
}
