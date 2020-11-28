package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.table.DefaultTableModel;
import com.codename1.ui.table.TableModel;
import com.codename1.ui.table.Table;
import com.codename1.ui.layouts.BoxLayout;

public class TruthTableView  extends Container {
    UserViewForm simulator;

    // These following 3 member variables need to be set properly in order for Truth Table to be displayed correctly

    public String[] inputPinNames = new String[]{"X", "Y", "Z", "W"}; // Table header names for inputs
    public String[] outputPinNames = new String[]{"Output1"}; // Table header names for outputs

    // Circuit board outputs for each combination of inputs, in the exact order corresponding with
    // what's returned from buildInputTable().
    // Make sure the length of the outputRowData corresponds with the number of rows from output of buildInputTable()
    public String[] outputRowData = new String[]{"1", "2", "3", "4", "5"};

    public int totalInputPins = inputPinNames.length;

    TruthTableView(UserViewForm _simulator_) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;

        Table table = buildTruthTable();
        table.setScrollableX(true);
        table.setScrollableY(true);
        add(table);
    }

    // buildTruthTable builds the truth table to be displayed onto the UI
    public Table buildTruthTable() {
        TableModel model = new DefaultTableModel(
            concatStringArr(inputPinNames, outputPinNames),
            appendColumnTo2DArr(buildInputTable(), outputRowData)
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

    // buildInputTable uses the total number of input pins in the circuit board,
    // and permutes all possible combinations for the inputs
    // i.e. If @numInputPins = 2, return result will be
    // {
    //   [0, 0],
    //   [0, 1],
    //   [1, 0],
    //   [1, 1],
    // }
    public Object[][] buildInputTable(){
        int totalRows = (int)Math.pow(2.0, totalInputPins);
        int switchCounter = totalRows / 2;
        int curCount = 0;

        boolean zero = true;
        boolean one = false;

        Object[][] res = new Object[totalRows][totalInputPins];

        for (int i = 0; i < totalInputPins; i++) {
            for (int j = 0; j < totalRows; j++) {
                if (zero)
                    res[j][i] = "0";
                else if (one)
                    res[j][i] = "1";

                curCount++;

                if (curCount >= switchCounter) {
                    if (zero) {
                        zero = false;
                        one = true;
                    } else if (one) {
                        one = false;
                        zero = true;
                    }
                    curCount = 0;
                }
            }
            switchCounter /= 2;
            curCount = 0;
        }

        return res;
    }
}
