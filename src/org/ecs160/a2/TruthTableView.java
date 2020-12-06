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
        refreshView();
    }

    public void refreshView(){
        Table table = buildTruthTable();
        table.setScrollableX(true);
        table.setScrollableY(true);
        add(table);
    }

    // buildTruthTable builds the truth table to be displayed onto the UI
    private Table buildTruthTable() {
        this.truthTable = circuitBoard.buildTruthTable();
        this.inputPinNames = truthTable.getInputPinNames();
        this.outputPinNames = truthTable.getOutputPinNames();

        //Express the input combinations and output states as 2D string arrays
        State[][] inputCombinations = truthTable.getInputCombinations();
        State[][] outputCombinations = truthTable.getOutputCombinations();
        Object[][] truthTableRowData = new Object[inputCombinations.length][inputCombinations.length + outputCombinations.length];
        for(int i = 0; i<inputCombinations.length; i++){
            ArrayList<State> inputCombination = new ArrayList<>();
            Collections.addAll(inputCombination, inputCombinations[i]);
            ArrayList<State> outputCombination = new ArrayList<>();
            Collections.addAll(outputCombination, outputCombinations[i]);
            inputCombination.addAll(outputCombination); //Append outputCombination to inputCombination
            truthTableRowData[i] = inputCombination.toArray();
        }

        TableModel model = new DefaultTableModel(
            concatStringArr(inputPinNames, outputPinNames),
            truthTableRowData
        );

        Table table = new Table(model);
        return table;
    }

    // appendColumnTo2DArr appends @col column-wise onto @arr1
    // IMPORTANT-- arr1.length (number of rows) should equal col.length
    private Object[][] appendColumnTo2DArr(Object[][] arr1, String[] col) {
        Object[][] res = new Object[arr1.length][arr1[0].length + 1];

        int colIdx = 0;
        for (int i = 0; i < res.length; i++) {
            for (int j = 0; j < res[0].length - 1; j++) {
                res[i][j] = arr1[i][j];
            }
            if (colIdx < col.length)
                res[i][res[0].length - 1] = col[colIdx++];
        }
        return res;
    }

    // concatStringArr takes two String[] and concatenates them into one String[]
    private String[] concatStringArr(String[] arr1, String[] arr2) {
        int idx = 0;
        String[] res = new String[arr1.length + arr2.length];

        for (int i = 0; i < arr1.length; i++) {
            res[i] = arr1[i];
            idx = i;
        }

        idx++;

        for (int i = 0; i < arr2.length; i++) {
            res[idx] = arr2[i];
            idx++;
        }

        return res;
    }
}
