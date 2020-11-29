package org.ecs160.a2;

import java.util.ArrayList;

public class Gate {
    ArrayList<Input> inputs;
    ArrayList<Output> outputs;
    Slot parent;
    LabelComponent name;

    public Gate(Slot s) {
        inputs = new ArrayList<Input>(); // For now, each gates have the max of two inputs
        inputs.add(new Input(this));
        inputs.add(new Input(this));

        outputs = new ArrayList<Output>(); // For now, each gates have the max of one output
        outputs.add(new Output(this));

        name = null;
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

        CircuitView.removeLabel(name);
        name = null;
    }

    protected LabelComponent setName(){
        return null;
    }

    public String getName() {
        return name.getName();
    }

    public void setLabel(LabelComponent name) {
        this.name = name;
    }

    public LabelComponent getLabel() {
        return name;
    }
}

class P1Gate extends Gate {
    private static int id = 0;

    public P1Gate(Slot s) {
        super(s);
        name = setName();
    }

    @Override
    protected LabelComponent setName() {
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "P1Gate " + Integer.toString(id++));
    }
}

class P2Gate extends Gate {
    private static int id = 0;

    public P2Gate(Slot s) {
        super(s);
        name = setName();
    }

    @Override
    protected LabelComponent setName() {
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "P2Gate " + Integer.toString(id++));
    }
}
