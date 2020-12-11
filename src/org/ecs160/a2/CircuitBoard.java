package org.ecs160.a2;


import com.codename1.components.ToastBar;
import com.codename1.ui.FontImage;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

//True is 1, false is 0
public class CircuitBoard implements Externalizable{
    HashMap<String, Gate> gates;
    HashMap<String, InputPin> inputPins;
    HashMap<String, OutputPin> outputPins;

    HashMap<String, Gate> copyGates;
    HashMap<String, InputPin> copyInputPins;
    HashMap<String, OutputPin> copyOutputPins;

    public CircuitBoard(){
        gates = new HashMap<>();
        inputPins = new HashMap<>();
        outputPins = new HashMap<>();

        copyGates = new HashMap<>();
        copyInputPins = new HashMap<>();
        copyOutputPins = new HashMap<>();
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

    /**
     * Check if all gates of circuit are connected
     * @return true if circuit is connected.
     */
    public boolean checkCircuit(){
        for (Gate g: gates.values()){
            if(!g.isConnected()){
                return false;
            }
        }
        return true;
    }

    /**
     * Find each output value by calling calculateOutput() on each output pin.
     */
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

    /**
     * Recursive function for performing a depth-first traversal of the circuit.
     * Calculates the output of each gate calculating those of the previous connected gates.
     * @param gate
     */
    private void calculateOutput(Gate gate){
        //Base case: Terminate when the gate has no inputs
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

    /**
     * Removes a gate from the board
     * @param g
     */
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

    /**
     * Runs a simulation given a particular input combination.
     * Used when calculating truth tables
     * @param inputCombination The values of each input pin, in order
     * @return State array containing the value of each output pin, in order
     */
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

    /**
     * Reset all inputs to zero
     */
    public void resetInputs(){
        for(InputPin input: inputPins.values()){
            input.setInput(State.ZERO);
        }
    }

    /**
     * Build truth table by calculating outputs for all possible input combinations
     * @return the truth table
     */
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
    //
    // i.e. If @numInputPins = 2, return result will be
    // {
    //   [0, 0],
    //   [0, 1],
    //   [1, 0],
    //   [1, 1],
    // }

    /**
     * buildInputTable uses the total number of input pins in the circuit board,
     * and permutes all possible combinations for the inputs
     * i.e. If @numInputPins = 2, return result will be
     * {
     *     [0, 0],
     *     [0, 1],
     *     [1, 0],
     *     [1, 1],
     * }
     *
     * @return 2D array containing each input combination
     */
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



    //The following methods are used when saving a CircuitBoard to external storage.

    @Override
    public int getVersion() {
        return 1;
    }


    @Override
    public void externalize(DataOutputStream dataOutputStream) throws IOException {
//        Util.writeObject(copyGates, dataOutputStream);
//        Util.writeObject(copyInputPins, dataOutputStream);
//        Util.writeObject(copyOutputPins, dataOutputStream);
//        copyGates = new HashMap<>();
//        copyInputPins = new HashMap<>();
//        copyOutputPins = new HashMap<>();
        Util.writeObject(gates, dataOutputStream);
        Util.writeObject(inputPins, dataOutputStream);
        Util.writeObject(outputPins, dataOutputStream);
    }

    static {Util.register("CircuitBoard", CircuitBoard.class);}

    @Override
    public void internalize(int i, DataInputStream dataInputStream) throws IOException {
        System.out.println("Internalizing CircuitBoard...");
        gates = (HashMap<String, Gate>) Util.readObject(dataInputStream);
        inputPins = (HashMap<String, InputPin>) Util.readObject(dataInputStream);
        outputPins = (HashMap<String, OutputPin>) Util.readObject(dataInputStream);
    }

    @Override
    public String getObjectId() {
        return "CircuitBoard";
    }

}