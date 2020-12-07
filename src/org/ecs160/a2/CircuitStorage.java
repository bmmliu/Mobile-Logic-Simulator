package org.ecs160.a2;

import com.codename1.io.Storage;
import java.util.Arrays;
import java.util.ArrayList;

public class CircuitStorage {
    private static ArrayList<String> circuitNames = new ArrayList<String>(Arrays.asList("circuit0", "circuit1","circuit2", "circuit3","circuit4"));
    private static ArrayList<String> availableCircuitRegisters = new ArrayList<String>(Arrays.asList("circuit0", "circuit1","circuit2", "circuit3","circuit4"));
    private static ArrayList<String> occupiedCircuitRegisters = new ArrayList<String>();




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
        Storage.getInstance().writeObject(registerName, circuitBoard);
        availableCircuitRegisters.remove(registerName);
        occupiedCircuitRegisters.add(registerName);
    }

    public static CircuitBoard loadCircuitBoard(String registerName){
        CircuitBoard circuitBoard = (CircuitBoard)Storage.getInstance().readObject(registerName);
        return circuitBoard;
    }

    public static void clearCircuitRegister(String registerName){
        Storage.getInstance().deleteStorageFile(registerName);
        occupiedCircuitRegisters.remove(registerName);
        availableCircuitRegisters.add(registerName);
    }
}
