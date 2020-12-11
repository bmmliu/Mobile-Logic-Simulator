package org.ecs160.a2;


import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;

import com.codename1.io.Externalizable;
import com.codename1.io.Util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


/**
 * Represents the TruthTable of a circuit.
 * The table is represented by a hashmap mapping input combinations to output combinations.
 */
public class TruthTable implements Externalizable{
    //List of input pin names
    private String[] inputPinNames;
    //List of output pin names
    private String[] outputPinNames;
    //Map of input combinations to output values
    private HashMap<State[], State[]> truthTable;

    public TruthTable(String[] inputPinNames, String[] outputPinNames, HashMap<State[], State[]> truthTable){
        this.inputPinNames = inputPinNames;
        this.outputPinNames = outputPinNames;
        this.truthTable = truthTable;
    }

    public State[] findOutputs(State[] inputs){
        for(Map.Entry<State[], State[]> m: truthTable.entrySet()){
            if (isEqual(m.getKey(), inputs)) {
                System.out.println("Matched!!!");
                return truthTable.get(m.getKey());
            }
        }

        return null;
    }

    private boolean isEqual(State[] state1, State[] state2) {
        for (int i = 0; i < state1.length; i++) {
            if (state1[i] != state2[i]) {
                return false;
            }
        }
        return true;
    }

    public String[] getInputPinNames(){
        return inputPinNames;
    }

    public String[] getOutputPinNames(){
        return outputPinNames;
    }

    public State[][] getInputCombinations(){
        State[][] inputCombinations = new State[truthTable.size()][inputPinNames.length];
        int i = 0;
        for(State[] inputCombination: truthTable.keySet()){
            inputCombinations[i] = inputCombination;
            i++;
        }
        return inputCombinations;
    }

    public State[][] getOutputCombinations(){
        State[][] outputCombinations = new State[truthTable.size()][outputPinNames.length];
        int i = 0;
        for(State[] outputCombination: truthTable.values()){
            outputCombinations[i] = outputCombination;
            i++;
        }
        return outputCombinations;
    }

    /**
     * Create a HashMap<int[], int[]> from the class' truth table. Used to create an object suitable for storage.
     * @return
     */

    private HashMap<int[], int[]> getStorageTable(){
        HashMap<int[], int[]> storageTable = new HashMap<>();
        for(Map.Entry<State[], State[]> m: truthTable.entrySet()){
            int[] inputs = new int[m.getKey().length];
            for(int i = 0; i<inputs.length; i++){
                inputs[i] = m.getKey()[i].ordinal();
            }
            int[] outputs = new int[m.getValue().length];
            for(int i = 0; i<outputs.length; i++){
                outputs[i] = m.getValue()[i].ordinal();
            }
            storageTable.put(inputs, outputs);
        }
        return storageTable;
    }

    /**
     * Create a HashMap<State[], State[]> given a HashMap<int[], int[]>. Used when bringing truth tables from storage
     *
     * @param storageTable
     * @return
     */
    private HashMap<State[], State[]> restoreTruthTable(HashMap<int[],int[]> storageTable){
        HashMap<State[], State[]> tTable = new HashMap<>();
         for(Map.Entry<int[], int[]> m: storageTable.entrySet()){
            State[] inputs = new State[m.getKey().length];
            for(int i = 0; i<inputs.length; i++){
                inputs[i] = State.values()[m.getKey()[i]];
            }
            State[] outputs = new State[m.getValue().length];
            for(int i = 0; i<outputs.length; i++){
                outputs[i] = State.values()[m.getValue()[i]];
            }
            tTable.put(inputs, outputs);
        }
        return tTable;
    }



    @Override
    public int getVersion() {
        return 1;
    }

    @Override
    public void externalize(DataOutputStream dataOutputStream) throws IOException {
        Util.writeObject(inputPinNames, dataOutputStream);
        Util.writeObject(outputPinNames, dataOutputStream);
        HashMap<int[], int[]> truthTableStorage = getStorageTable();
        Util.writeObject(truthTableStorage, dataOutputStream);

    }

    static {Util.register("TruthTable", Slot.class);}

    @Override
    public void internalize(int i, DataInputStream dataInputStream) throws IOException {
        inputPinNames = (String[])Util.readObject(dataInputStream);
        outputPinNames = (String[])Util.readObject(dataInputStream);
        HashMap<int[], int[]> storedTruthTable = (HashMap<int[], int[]>)Util.readObject(dataInputStream);
        truthTable = restoreTruthTable(storedTruthTable);
    }

    @Override
    public String getObjectId() {
        return "TruthTable";
    }


}