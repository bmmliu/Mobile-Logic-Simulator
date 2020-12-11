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
public class TruthTable{
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


}