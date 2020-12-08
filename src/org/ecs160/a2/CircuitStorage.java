package org.ecs160.a2;

import com.codename1.io.Storage;
import com.codename1.io.Util;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.HashMap;

public class CircuitStorage {
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

    public static void saveCircuitBoard(String registerName, CircuitBoard circuitBoard){
        circuitBoardMap.put(registerName, circuitBoard);
        //Storage.getInstance().writeObject(registerName, arr);
        availableCircuitRegisters.remove(registerName);
        occupiedCircuitRegisters.add(registerName);
    }

    public static CircuitBoard loadCircuitBoard(String registerName){
        //CircuitBoard circuitBoard = (CircuitBoard)Storage.getInstance().readObject(registerName);
        CircuitBoard circuitBoard = circuitBoardMap.get(registerName);
        return circuitBoard;
    }

    public static void clearCircuitRegister(String registerName){
        //Storage.getInstance().deleteStorageFile(registerName);
        circuitBoardMap.remove(registerName);
        occupiedCircuitRegisters.remove(registerName);
        availableCircuitRegisters.add(registerName);
    }

    public static TruthTable loadSubcircuit(String registerName){
        CircuitBoard circuitBoard = loadCircuitBoard(registerName);
        circuitBoard.printTruthTable();
        return circuitBoard.buildTruthTable();
    }


    //Circuit Storage Class
    //HashMap of slot indexes to Gate types {1, AND}
    //Pairs representing wire endpoints(Slot 1 -> Slot 2)
}
