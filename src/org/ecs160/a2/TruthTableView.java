package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.layouts.BoxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TruthTableView extends Container {
    UserViewForm simulator;

    // These following 3 member variables need to be set properly in order for Truth Table to be displayed correctly


    //Get these names from the UserViewForm
    public String[] inputPinNames; // Table header names for inputs
    public String[] outputPinNames; // Table header names for outputs

    // Circuit board outputs for each combination of inputs, in the exact order corresponding with
    // what's returned from buildInputTable().
    // Make sure the length of the outputRowData corresponds with the number of rows from output of buildInputTable()
    public String[][] outputRowData;


    private TruthTable truthTable;
    private CircuitBoard circuitBoard;

    TruthTableView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;

        this.circuitBoard = circuitBoard;
    }

    public void refreshView(){
        Table table = buildTruthTable();
        table.setScrollableX(true);
        table.setScrollableY(true);
        //FIXME: I ADD THE NEW TABLE, BUT HOW DO I DELETE THE OLD ONE?
        //FIXED
        removeAll();
        add(table);
    }

    // buildTruthTable builds the truth table to be displayed onto the UI
    private Table buildTruthTable() {
        this.truthTable = circuitBoard.buildTruthTable();
        this.inputPinNames = truthTable.getInputPinNames();
        this.outputPinNames = truthTable.getOutputPinNames();

        ArrayList<String> headerNamesList = new ArrayList<>();
        Collections.addAll(headerNamesList, inputPinNames);
        Collections.addAll(headerNamesList, outputPinNames);
        String[] headerNames = headerNamesList.toArray(new String[0]);

        //Express the input combinations and output states as 2D string arrays
        State[][] inputCombinations = truthTable.getInputCombinations();
        State[][] outputCombinations = truthTable.getOutputCombinations();
        Object[][] truthTableRowData = new Object[inputCombinations.length][inputCombinations.length + outputCombinations.length];
        for(int i = 0; i<inputCombinations.length; i++){
            ArrayList<Integer> input = new ArrayList<>();
            for (State s : inputCombinations[i]) {
                switch (s) {
                    case ZERO:
                        input.add(0);
                        break;
                    case ONE:
                        input.add(1);
                        break;
                    default:
                        System.out.println("Error @TruthTableView in buildTruthTable");
                        break;
                }
            }
            ArrayList<Integer> output = new ArrayList<>();
            for (State s : outputCombinations[i]) {
                switch (s) {
                    case ZERO:
                        input.add(0);
                        break;
                    case ONE:
                        input.add(1);
                        break;
                    default:
                        System.out.println("Error @TruthTableView in buildTruthTable");
                        break;
                }
            }

            /*
            ArrayList<State> inputCombination = new ArrayList<>();
            Collections.addAll(inputCombination, inputCombinations[i]);
            ArrayList<State> outputCombination = new ArrayList<>();
            Collections.addAll(outputCombination, outputCombinations[i]);
            inputCombination.addAll(outputCombination); //Append outputCombination to inputCombination
            truthTableRowData[i] = inputCombination.toArray();

             */
            input.addAll(output);
            truthTableRowData[i] = input.toArray();
        }

        TableModel model = new DefaultTableModel(
            headerNames,
            truthTableRowData
        );

        Table table = new Table(model);
        return table;
    }
}
