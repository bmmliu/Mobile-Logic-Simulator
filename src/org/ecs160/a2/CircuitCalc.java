package org.ecs160.a2;

import java.util.ArrayList;

// CircuitCalc is a functional class used to find the propagation delay, critical path, and maximum operating frequency
// of a given circuitboard
public class CircuitCalc {

    // Gets the propagation delay for a given path of gates. Assumes they are connected to each other.
    public static int GetPropagationDelay(ArrayList<Gate> path) {
        int totalPropDelay = 0;
        for (Gate gate : path) {
            totalPropDelay += gate.PDelay;
        }
        return totalPropDelay;
    }

    // Get the critical path for the given circuitboard
    public static ArrayList<Gate> GetCriticalPath(CircuitBoard cb) {
        // Need to check for the longest path that leads into each output port.
        ArrayList<Gate> criticalPath = new ArrayList<>();
        int maxPropDelay = 0;
        // We assume there are multiple output pins, so we need to find the longest path out of each pin,
        // and return the one with the longest path
        for (OutputPin output : cb.outputPins.values()) {
            ArrayList<Gate> potentialCriticalPath = longestPathFromGate(output, new ArrayList<Gate>(), 0);
            if (GetPropagationDelay(potentialCriticalPath) >= maxPropDelay) {
                maxPropDelay = GetPropagationDelay(potentialCriticalPath);
                criticalPath = potentialCriticalPath;
            }
        }
        return criticalPath;
    }

    // Our recursive utility function that finds the longest path given a starting gate
    private static ArrayList<Gate> longestPathFromGate(Gate gate, ArrayList<Gate> pathSoFar, int costSoFar) {

        // Returns the highest cost path out of the gate
        int curCost = costSoFar + gate.PDelay;
        // Important to use the "new" keyword or we will end up with every Gate coming from 'gate'
        ArrayList<Gate> curPath = new ArrayList<Gate>();
        curPath.addAll(pathSoFar);
        curPath.add(gate);

        ArrayList<Gate> longestPath = curPath;
        int maxCost = curCost;

        for (Input input : gate.inputs) {
            // get the gate coordinating with this input
            Gate curGate = input.getPrevOutput().getPortParent();
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
        // Want to get the maximum operating frequency in... Megahertz
        double propDelayInSeconds = propDelay / 1_000_000.0;
        double raw_speed = 1.0 / propDelayInSeconds; // Hertz
        return raw_speed / 100_000_000; // Megahertz
    }


}