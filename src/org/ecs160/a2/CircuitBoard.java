package org.ecs160.a2;


import com.codename1.components.ToastBar;
import com.codename1.ui.FontImage;

import java.util.HashMap;
import java.util.Map;

// CircuitBoard is the primary container for the gates, pins, and their connections to eachother.
//True is 1, false is 0
public class CircuitBoard {
    HashMap<String, Gate> gates;
    HashMap<String, InputPin> inputPins;
    HashMap<String, OutputPin> outputPins;

    // Initialize all values
    public CircuitBoard(){
        gates = new HashMap<>();
        inputPins = new HashMap<>();
        outputPins = new HashMap<>();
    }

    // Copy constructor for externalizing.
    public CircuitBoard(CircuitBoard circuitBoard) {
        gates = new HashMap<>(circuitBoard.gates);
        inputPins = new HashMap<>(circuitBoard.inputPins);
        outputPins = new HashMap<>(circuitBoard.outputPins);
    }

    public void addGate(Gate gate){
        gates.put(gate.getLabelName(), gate);
    }

    public void addInputPin(InputPin inputPin){
        gates.put(inputPin.getLabelName(), inputPin);
        inputPins.put(inputPin.getLabelName(), inputPin);
    }

    public void addOutputPin(OutputPin outputPin){
        gates.put(outputPin.getLabelName(), outputPin);
        outputPins.put(outputPin.getLabelName(), outputPin);
    }

    public void toggleInput(String inputPinID){
        //Find the input pin with the correct ID and toggle it
        inputPins.get(inputPinID).toggle();
        runSimulation();
    }

    // This checks if a circuit is valid or not
    public boolean checkCircuit(){
        for (Gate g: gates.values()){
            //System.out.println(g.getLabelName());
            if(!g.isConnected()){
                return false;
            }
        }
        return true;
    }

    // How we run the actual simulation. Tied to the play/start button
    public void runSimulation(){
        if(!checkCircuit()){
            ToastBar.showMessage("Circuit is invalid", FontImage.MATERIAL_INFO);
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
                calculateOutput(prev.getPortParent());
            }
            gate.update();
        }
    }

    public void removeGate(Gate g) {
        g.getParentSlot().emptySlot();
        String name = g.getLabelName();
        gates.remove(name);
        if (g.gateType == GateType.INPUT_PIN) {
            inputPins.remove(name);
        } else if (g.gateType == GateType.OUTPUT_PIN) {
            outputPins.remove(name);
        }
    }

    public void loadSubcircuit(){}

    public void loadCircuit(){}

    //Run simulation given input values for each input pin
    public State[] runSimulation(State[] inputCombination){
        int i = 0;
        for(Map.Entry<String, InputPin> inputPin: inputPins.entrySet()){
            inputPin.getValue().setInput(inputCombination[i]);
            i++;
        }
        runSimulation();
        State[] outputResults = new State[outputPins.size()];
        int j = 0;
        for(Map.Entry<String, OutputPin> o: outputPins.entrySet()){
            outputResults[j] = o.getValue().getState();
            j++;
        }
        resetInputs();
        return outputResults;
    }

    //Reset all inputs to zero
    public void resetInputs(){
        for(InputPin input: inputPins.values()){
            input.setInput(State.ZERO);
        }
    }

    // Generates a truth table for all possible input combinations
    public TruthTable buildTruthTable(){
        //Get input pin names
        String[] inputPinNames = inputPins.keySet().toArray(new String[0]);
        //Get output pin names
        String[] outputPinNames = outputPins.keySet().toArray(new String[0]);
        //Build input combinations
        State[][] inputCombinations = buildInputCombinations();
        //Get outputs corresponding to each input
        State[] outputResults;
        //Declare hashmap mapping input combinations to output states
        HashMap<State[], State[]> truthTableMap = new HashMap<>();
        //Run simulation with those inputs
        for(State[] combination: inputCombinations) {
            outputResults = runSimulation(combination);
            truthTableMap.put(combination, outputResults);
        }
        //Make truth table
        return new TruthTable(inputPinNames, outputPinNames, truthTableMap);
    }

    // buildInputTable uses the total number of input pins in the circuit board,
    // and permutes all possible combinations for the inputs
    // i.e. If @numInputPins = 2, return result will be
    // {
    //   [0, 0],
    //   [0, 1],
    //   [1, 0],
    //   [1, 1],
    // }
    public State[][] buildInputCombinations() {
        int totalInputPins = inputPins.size();
        int totalRows = (int)Math.pow(2.0, totalInputPins);
        int switchCounter = totalRows / 2;
        int curCount = 0;

        boolean zero = true;
        boolean one = false;

        State[][] res = new State[totalRows][totalInputPins];

        for (int i = 0; i < totalInputPins; i++) {
            for (int j = 0; j < totalRows; j++) {
                if (zero)
                    res[j][i] = State.ZERO;
                else if (one)
                    res[j][i] = State.ONE;

                curCount++;

                if (curCount >= switchCounter) {
                    if (zero) {
                        zero = false;
                        one = true;
                    } else if (one) {
                        one = false;
                        zero = true;
                    }
                    curCount = 0;
                }
            }
            switchCounter /= 2;
            curCount = 0;
        }

        return res;
    }

    public void printTruthTable(){
        //This is for testing
        TruthTable t = buildTruthTable();
        System.out.println("The truth table is: ");
        t.print();
    }

}