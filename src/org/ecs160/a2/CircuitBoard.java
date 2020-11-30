package org.ecs160.a2;


import java.util.HashMap;
import java.util.ArrayList;

//True is 1, false is 0
public class CircuitBoard {
    HashMap<String, Gate> gates;
    HashMap<String, InputPin> inputPinsMap;
    HashMap<String, OutputPin> outputPins;

    public CircuitBoard(){
        gates = new HashMap<>();
        inputPinsMap = new HashMap<>();
        outputPins = new HashMap<>();
    }


    public void addGate(Gate gate){
        gates.put(gate.getLabelName(), gate);
    }

    public void addInputPin(InputPin inputPin){
        gates.put(inputPin.getLabelName(), inputPin);
        inputPinsMap.put(inputPin.getLabelName(), inputPin);
    }

    public void addOutputPin(OutputPin outputPin){
        gates.put(outputPin.getLabelName(), outputPin);
        outputPins.put(outputPin.getLabelName(), outputPin);
    }

    public void toggleInput(String inputPinID){
        //Find the input pin with the correct ID and toggle it
        inputPinsMap.get(inputPinID).toggle();
    }

    public boolean checkCircuit(){
        for (Gate g: gates.values()){
            if(!g.isConnected()){
                return false;
            }
        }
        return true;
    }

    public void runSimulation(){
        if(!checkCircuit()){
            System.out.println("Circuit is invalid");
            return;
        }
        for(OutputPin outputPin: outputPins.values()){
            calculateOutput(outputPin);
            System.out.println(outputPin.getLabelName() + "'s output is " + outputPin.getState());
        }
    }



    private void calculateOutput(Gate gate){
        if(gate.inputs.isEmpty()){
            gate.update();
            return;
        }
        else{
            for(Input in: gate.inputs){
                //Get in's predecessor
                Output prev = in.getPrevOutput();
                //Find its parent gate
                calculateOutput(prev.getParent());
            }
            gate.update();
        }
    }


    public void loadSubcircuit(){}

    public void loadCircuit(){}
}

