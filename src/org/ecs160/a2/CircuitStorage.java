package org.ecs160.a2;

import com.codename1.io.Storage;
import com.codename1.io.Util;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class designed for saving and loading circuits
 */
public class CircuitStorage {
    //These fields track the available circuits for storage and those that are already occupied.
    //They are used to update the Picker used to select the circuit register for storage.
    private ArrayList<String> circuitNames;
    private ArrayList<String> availableCircuitRegisters;
    private ArrayList<String> occupiedCircuitRegisters;

    public CircuitStorage(){
        circuitNames = new ArrayList<String>(Arrays.asList("circuit0", "circuit1","circuit2", "circuit3","circuit4"));

        //Attempt to load saved register names from memory
        availableCircuitRegisters = (ArrayList<String>) Storage.getInstance().readObject("AvailableRegisters");
        occupiedCircuitRegisters = (ArrayList<String >)Storage.getInstance().readObject("OccupiedRegisters");
        if(availableCircuitRegisters == null){
            availableCircuitRegisters = new ArrayList<String>(Arrays.asList("circuit0", "circuit1","circuit2", "circuit3","circuit4"));
        }
        if(occupiedCircuitRegisters == null){
            occupiedCircuitRegisters = new ArrayList<String>();
        }
    }

    public String[] getCircuitNames(){
        return circuitNames.toArray(new String[0]);
    }

    public String[] getAvailableCircuits(){
        return availableCircuitRegisters.toArray(new String[0]);
    }

    public String[] getOccupiedCircuits(){
        return occupiedCircuitRegisters.toArray(new String[0]);
    }

    public static CircuitView loadCircuitView(String registerName){
        CircuitView circuitView = (CircuitView)Storage.getInstance().readObject(registerName);
        return circuitView;
    }

    public void saveSubcircuit(String registerName, TruthTable truthTable){
        Storage.getInstance().writeObject(registerName, truthTable);
        availableCircuitRegisters.remove(registerName);
        occupiedCircuitRegisters.add(registerName);
        Storage.getInstance().writeObject("OccupiedRegisters", occupiedCircuitRegisters);
        Storage.getInstance().writeObject("AvailableRegisters", availableCircuitRegisters);
    }

    public TruthTable loadSubcircuit(String registerName){
        TruthTable truthTable = (TruthTable)Storage.getInstance().readObject(registerName);
        return truthTable;
    }
}
