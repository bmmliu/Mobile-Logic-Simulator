package org.ecs160.a2;


import java.util.HashMap;
import java.util.ArrayList;

//True is 1, false is 0
public class CircuitBoard {
    ArrayList<Gate> gates;
    HashMap<String, InputPin> inputPinsMap;
    ArrayList<OutputPin> outputPins;

    public CircuitBoard(){
        gates = new ArrayList<Gate>();
        inputPins = new ArrayList<InputPin>();
        outputPins = new ArrayList<OutputPin>();
    }


    public void addGate(Gate gate){
        gates.add(gate);
    }

    public void addInputPin(InputPin inputPin){
        inputPinsMap.put(inputPin.getLabelName(), inputPin);
    }

    public void addOutputPin(OutputPin outputPin){
        outputPins.add(outputPin);
    }

    public void toggleInput(String inputPinID){
        //Find the input pin with the correct ID and toggle it
        inputPinsMap.get(inputPinID).toggle();
    }

    public void runSimulation(){
        if(!checkCircuit()){
            System.out.println("Circuit is invalid");
            return;
        }
        for(OutputPin outputPin: outputPins){
            calculateOutput(outputPin);
        }
    }

    private void calculateOutput(Gate gate){
        //THIS DOESN'T WORK IF WE START FROM THE OUTPUT PINS!!
        ArrayList<Input> inputs = gate.inputs;
        while(inputs.size() != 0){
            for(Input in: inputs){
                //Get in's predecessor
                //Find it's parent gate
                calculateOutput(gate);
            }
        }
        gate.calculate();
    }
}

