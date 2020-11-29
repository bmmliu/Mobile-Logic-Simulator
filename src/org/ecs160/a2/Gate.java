package org.ecs160.a2;

import com.codename1.ui.Component;

import java.util.ArrayList;

public abstract class Gate extends Component {
    ArrayList<Input> inputs;
    ArrayList<Output> outputs;
    protected boolean state;
    Slot parent;
    LabelComponent label;

    public Gate(Slot s) {
        inputs = new ArrayList<Input>();
        outputs = new ArrayList<Output>();

        state = false;
        label = null;
        parent = s;
    }

    public void update(){
        calculate();
        for(Output output: outputs){
            output.setState(state);
        }
    }

    public abstract void calculate();

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

        label.getParent().removeComponent(label);
        label = null;
    }

    protected LabelComponent setName(){
        return null;
    }

    public String getLabelName() {
        return label.getName();
    }

    public void setLabel(LabelComponent name) {
        this.label = name;
    }

    public LabelComponent getLabel() {
        return label;
    }
}

class P1Gate extends Gate {
    private static int id = 0;

    public P1Gate(Slot s) {
        super(s);
        super.setName("P1Gate");
        label = setName();

        inputs.add(new Input(this));
        inputs.add(new Input(this));

        outputs.add(new Output(this));
    }

    @Override
    public void calculate() {
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
        super.setName("P2Gate");
        label = setName();

        inputs.add(new Input(this));
        inputs.add(new Input(this));

        outputs.add(new Output(this));
    }

    @Override
    public void calculate() {
    }

    @Override
    protected LabelComponent setName() {
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "P2Gate " + Integer.toString(id++));
    }
}


class AndGate extends Gate{
    private static int id = 0;

    public AndGate(Slot s) {
        super(s);
        super.setName("AndGate");
        label = setName();

        inputs.add(new Input(this));
        inputs.add(new Input(this));
        outputs.add(new Output(this));
    }

    public AndGate(Slot s, int numInputs){
        super(s);
        super.setName("AndGate");
        label = setName();

        if(numInputs >= 2){
            for(int i = 0; i<numInputs; i++){
                inputs.add(new Input(this));
            }
            outputs.add(new Output(this));
        }
        else{
            System.out.println("Please enter at least 2 inputs\n");
        }
    }

    @Override
    public void calculate() {
        boolean finalState = true;
        for(Input i: inputs){
            finalState = finalState && i.getState();
        }
    }

    @Override
    protected LabelComponent setName() {
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "AndGate " + Integer.toString(id++));
    }
}

class InputPin extends Gate {
    private static int id = 0;

    public InputPin(Slot s) {
        super(s);
        super.setName("InputPin");
        label = setName();

        inputs.clear();
        outputs.add(new Output(this));
    }

    @Override
    public void calculate() {
    }

    public void toggle(){
        state = !state;
    }

    @Override
    protected LabelComponent setName() {
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "InputPin " + Integer.toString(id++));
    }
}

class OutputPin extends Gate {
    private static int id = 0;

    public OutputPin(Slot s) {
        super(s);
        super.setName("OutputPin");
        label = setName();

        inputs.add(new Input(this));
        outputs.clear();
    }

    @Override
    public void calculate() {
        state = inputs.get(0).getState();
    }

    @Override
    protected LabelComponent setName() {
        int offsetX = parent.getWidth()/2;
        int offsetY = parent.getHeight()/2;

        return new LabelComponent(parent.getAbsoluteX()-offsetX, parent.getAbsoluteY()-offsetY, "OutputPin " + Integer.toString(id++));
    }
}



