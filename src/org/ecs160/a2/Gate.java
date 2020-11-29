package org.ecs160.a2;

import java.util.ArrayList;
import java.util.List;

public class Gate {
    ArrayList<Input> inputs;
    ArrayList<Output> outputs;
    Slot parent;

    public Gate(Slot s) {
        inputs = new ArrayList<Input>(); // For now, each gates have the max of two inputs
        inputs.add(new Input(this));
        inputs.add(new Input(this));

        outputs = new ArrayList<Output>(); // For now, each gates have the max of one output
        outputs.add(new Output(this));

        parent = s;
    }

    public void deleteGate() {
        for (Input i : inputs) {
            if (i.isConnected()) {
                i.getConnectedPort().reset(this, i);
            }
            i.reset();
        }
        for (Output o : outputs) {
            for (Input i : o.getConnectedPorts()) {
                i.reset();
            }
            o.reset();
        }
    }
}

class P1Gate extends Gate {
    public P1Gate(Slot s) {
        super(s);
    }
}

class P2Gate extends Gate {
    public P2Gate(Slot s) {
        super(s);
    }
}
