package org.ecs160.a2;

import java.util.ArrayList;

public class CircuitCalc {

    /**
     * Given a path of gates, find the total propagation delay by adding each gate's propagation delay.
     *
     * @param path
     * @return
     */
    public static int GetPropagationDelay(ArrayList<Gate> path) {
        int totalPropDelay = 0;
        for (Gate gate : path) {
            totalPropDelay += gate.PDelay;
        }
        return totalPropDelay;
    }

    /**
     * Find the critical path of a given circuit.
     * Perform a depth-first traversal of the circuit, finding the longest input path leading into a gate, and using it to construct the critical path.
     *
     * @param cb
     * @return
     */
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

    /**
     * Calculate the longest path entering this gate
     * @param gate
     * @param pathSoFar
     * @param costSoFar
     * @return
     */
    private static ArrayList<Gate> longestPathFromGate(Gate gate, ArrayList<Gate> pathSoFar, int costSoFar) {

        // Returns the highest cost path out of the gate
        int curCost = costSoFar + gate.PDelay;
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

    /**
     * Find the max operating frequency given the propagation delay.
     * @param propDelay
     * @return
     */
    public static double GetMaxOperatingFrequency(int propDelay) {
        // Want to get the maximum operating frequency in... Megahertz
        double propDelayInSeconds = propDelay / 1_000_000.0;
        double raw_speed = 1.0 / propDelayInSeconds; // Hertz
        return raw_speed / 100_000_000; // Megahertz
    }


}