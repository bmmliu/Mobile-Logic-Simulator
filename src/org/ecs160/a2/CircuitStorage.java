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
    private static ArrayList<String> circuitNames = new ArrayList<String>(Arrays.asList("circuit0", "circuit1","circuit2", "circuit3","circuit4"));
    private static ArrayList<String> availableCircuitRegisters = new ArrayList<String>(Arrays.asList("circuit0", "circuit1","circuit2", "circuit3","circuit4"));
    private static ArrayList<String> occupiedCircuitRegisters = new ArrayList<String>();

    private static HashMap<String, CircuitBoard> circuitBoardMap = new HashMap<>();


    public static String[] getCircuitNames(){
        return circuitNames.toArray(new String[0]);
    }

    public static String[] getAvailableCircuits(){
        return availableCircuitRegisters.toArray(new String[0]);
    }

    public static String[] getOccupiedCircuits(){
        return occupiedCircuitRegisters.toArray(new String[0]);
    }



    public static void saveCircuitView(String registerName, CircuitView circuitView){
        Storage.getInstance().writeObject(registerName, circuitView);
        availableCircuitRegisters.remove(registerName);
        occupiedCircuitRegisters.add(registerName);
    }


    public static CircuitView loadCircuitView(String registerName){
        CircuitView circuitView = (CircuitView)Storage.getInstance().readObject(registerName);
        return circuitView;
    }

    public static void saveSubcircuit(String registerName, CircuitBoard circuitBoard){
        circuitBoardMap.put(registerName, circuitBoard);
        availableCircuitRegisters.remove(registerName);
        occupiedCircuitRegisters.add(registerName);
    }

    public static TruthTable loadSubcircuit(String registerName){
        TruthTable truthTable = circuitBoardMap.get(registerName).buildTruthTable();
        return truthTable;
    }
}
