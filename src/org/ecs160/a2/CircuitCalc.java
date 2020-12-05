package org.ecs160.a2;

import java.util.ArrayList;
import java.util.HashMap;

public class CircuitCalc {

    // TODO: Remove this (for testing only)
    public static void TestPropagationDelay(CircuitBoard cb) {
        ArrayList<Gate> path = GetCriticalPath(cb);
        System.out.println("Length of path = " + path.size());
        for (Gate g : path) {
            System.out.println(g.getName() + " <- ");
        }
        System.out.println(GetPropagationDelay(path));
    }

    public static int GetPropagationDelay(ArrayList<Gate> path) {
        int totalPropDelay = 0;
        for (Gate gate : path) {
            totalPropDelay += gate.PDelay;
        }
        return totalPropDelay;
    }

    public static ArrayList<Gate> GetCriticalPath(CircuitBoard cb) {
        // Need to check for the longest path that leads into each output port.
        ArrayList<Gate> criticalPath = new ArrayList<>();
        int maxPropDelay = 0;
        for (OutputPin output : cb.outputPins.values()) {
            ArrayList<Gate> potentialCriticalPath = longestPathFromGate(output, new ArrayList<Gate>(), 0);
            if (GetPropagationDelay(potentialCriticalPath) >= maxPropDelay) {
                maxPropDelay = GetPropagationDelay(potentialCriticalPath);
                criticalPath = potentialCriticalPath;
            }
        }
        return criticalPath;
    }

    private static ArrayList<Gate> longestPathFromGate(Gate gate, ArrayList<Gate> pathSoFar, int costSoFar) {

        // Returns the highest cost path out of the gate
        int curCost = costSoFar + gate.PDelay;
        ArrayList<Gate> curPath = pathSoFar;
        curPath.add(gate);

        ArrayList<Gate> longestPath = curPath;
        int maxCost = curCost;

        System.out.println("Analyzing gate " + gate.getName() + " with inputs:");
        for (Input input : gate.inputs) {
            // get the gate coordinating with this input
            Gate curGate = input.getPrevOutput().getParent();
            System.out.println("-> " + curGate.getName());
            ArrayList<Gate> possibleLongestPath = longestPathFromGate(curGate, curPath, curCost);
            int pathCost = GetPropagationDelay(possibleLongestPath);
            if (pathCost >= maxCost) {
                maxCost = GetPropagationDelay(possibleLongestPath);
                longestPath = possibleLongestPath;
            }
        }
        return longestPath;
    }

    public static double GetMaxOperatingFrequency(int propDelay) {
        // Want to get the maximum operating frequency in... gigahertz?
        double raw_speed = 1.0 / propDelay; // Hertz
        return raw_speed / 1000000000; // Gigahertz
    }


}