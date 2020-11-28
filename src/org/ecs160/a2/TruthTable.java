package org.ecs160.a0;

import java.util.HashMap;

// Everytime user run simulation, create a new truthTable logic
// When creating logic, truthTable requires to check all connections
// If any connections are missing and caused output to be invalid, we can stop the simulation
//      Better yet, we can put user in simulation mode, not visualizing the running circuit but instead mark problem components in red
//      for now, just set all ok output pins to 0 and problems to -1
// If all connections are valid, we should return the list of output Pins with Integer as booleans


public class TruthTable {
    // We probably needs all the Hashtables to get the connections and parent Components for each ports
    HashMap<Integer, InputPin> inputPinsMap;
    HashMap<Integer, OutputPin> outputPinsMap;
    HashMap<Integer, Component> componentsMap;
    HashMap<Integer, Port> portsMap;

    // outPins[0] should be from input Pins "000...00"
    // outPins[1] should be "000...01"
    // outPins[2] should be "000...10"
    // And so on
    private Integer[][] outputPins;
    private boolean valid;

    public TruthTable(HashMap<Integer, InputPin> i, HashMap<Integer, OutputPin> o, HashMap<Integer, Component> c, HashMap<Integer, Port> p){
        inputPinsMap = i;
        outputPinsMap = o;
        componentsMap = c;
        portsMap = p;

        int inputSize = (int)Math.pow(2, i.size());
        int outputSize = o.size();
        outputPins = new Integer[inputSize][outputSize];

        valid = true;
        buildTruthTableLogic();
    }

    public Integer[][] getTable() {
        return outputPins;
    }

    public boolean isValid() {
        return valid;
    }

    // Build the outputPins mapping
    // If return false, not all outputPins are valid
    private void buildTruthTableLogic() {
        // For each combination of inputPins
        //      For each element of the column of outputPins
        //          Call recursive function to get the boolean value of outputPin
        //          Here, I will treat outputPin storing an integer, with 0=false, 1=true, and -1=NA

        int inputLength = inputPinsMap.size();
        int outputLength = outputPinsMap.size();
        for (int i = 0; i < Math.pow(2, inputLength); i++) {
            String input = IntToBinary(i, inputLength);
            for (int j = 0; j < inputLength; j++) {
                inputPinsMap.get(j).setState(Character.getNumericValue(input.charAt(j)));
            }

            for (int o = 0; o < outputLength; o++) {
                Integer bool = getBool(outputPinsMap.get(o).getInputPin);
                if (bool == -1) valid = false;
                outputPins[i][o] = bool;
            }
        }
    }

    // Recursively transverse circuit path from outputPin to inputPin
    private Integer getBool(Port output) {
        // If we reach the InputPin, return that value
        if (Type(output.connectedTo) == InputPin) {
            return output.connectedTo.getState;
        }

        // Otherwise, we arrived at a gate, which probably needs an array of inputPorts to calculate and return the output
        // For each inputPorts, call this function recursively to obtain their states
        // If any of the inputPorts return a -1 (assume they result from missing input value in gate when calculating output)
        //      We will simply save them and might want to set their parent Component to red when visualizing
        Port[] inputPorts = output.getParent().getInputPorts();
        for (Port p : inputPorts) {
            p.setState(getBool(p));
        }
        output.getParent().calculate();
        return output.getState();
    }

    // Reference from https://mkyong.com/java/java-convert-an-integer-to-a-binary-string/ by mkyong
    private String IntToBinary(int number, int size) {
        StringBuilder result = new StringBuilder();
        for (int i = size; i >= 0; i--) {
            int mask = 1 << i;
            result.append((number & mask) != 0 ? "1" : "0");
        }

        return result.toString();
    }

}
