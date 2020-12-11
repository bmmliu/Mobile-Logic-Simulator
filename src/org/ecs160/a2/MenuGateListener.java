package org.ecs160.a2;

import com.codename1.components.ToastBar;
import com.codename1.io.Storage;
import com.codename1.ui.Button;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.spinner.Picker;

import javax.swing.*;
import java.util.Stack;
import java.util.function.ToDoubleBiFunction;

public class MenuGateListener implements ActionListener{
    private UserViewForm simulator;
    private Picker psave = new Picker();
    private Picker pload = new Picker();
    private Picker psload = new Picker();
    private String[] pickerList;

    // ActionListeners for each pickers needs to be "callBackAble", so we can remove respective listeners after the button finished
    private ActionListener psloadListener;
    private ActionListener ploadListener;
    private ActionListener psaveListener;


    MenuGateListener(UserViewForm _simulator_) {
        simulator = _simulator_;

        psave.setType(Display.PICKER_TYPE_STRINGS);
        pickerList = new String[]{"Cancel", "circuit0", "circuit1","circuit2", "circuit3","circuit4"};

        psaveListener = createListener("save");
        ploadListener = createListener("load");
        psloadListener = createListener("sload");
    }

    public void actionPerformed(ActionEvent event) {
        Button digitButton = (Button)event.getSource();
        String buttonText = digitButton.getText();

        // TODO: For now, I just made user couldn't save/load circuit while in P_Delay mode
        if (simulator.circuitDisplay.mode == UserMode.PDELAY) {
            return;
        }

        switch (buttonText) {
            case "save":
                psave.setType(Display.PICKER_TYPE_STRINGS);
                psave.setStrings(CircuitStorage.getAvailableCircuits());

                psave.addActionListener(psaveListener);

                simulator.menuDisplay.add(BorderLayout.SOUTH, psave);
                simulator.menuDisplay.revalidate();
                simulator.show();

                psave.pressed();
                psave.released();

                break;
            case "load":
                pload.setType(Display.PICKER_TYPE_STRINGS);
                pload.setStrings(CircuitStorage.getOccupiedCircuits());

                pload.addActionListener(ploadListener);

                simulator.menuDisplay.add(BorderLayout.SOUTH, pload);
                simulator.menuDisplay.revalidate();
                simulator.show();

                pload.pressed();
                pload.released();

                break;
            case "sload":
                psload.setType(Display.PICKER_TYPE_STRINGS);
                psload.setStrings(CircuitStorage.getOccupiedCircuits());

                psload.addActionListener(psloadListener);

                simulator.menuDisplay.add(BorderLayout.SOUTH, psload);
                simulator.menuDisplay.revalidate();
                simulator.show();

                psload.pressed();
                psload.released();

                break;
            default:
                break;
        }
    }

    private void insertSubCircuit(TruthTable t, int circuitId) {
        for (int i = 0; i < CircuitView.slots.size(); i++) {
            Slot s = CircuitView.slots.get(i);
            if (s.isEmpty()) {
                Gate gate = new Subcircuit(s, t, circuitId);
                simulator.circuitBoard.addGate(gate);
                s.setSlot(gate);
                simulator.show();
                break;
            }
        }
    }

    private ActionListener createListener(String listenerType) {
        switch (listenerType) {
            case "save":
                return evt -> {
                    String field = psave.getSelectedString();
                    //TODO: Uncomment this to test saveCircuitView
                    //CircuitStorage.saveCircuitView(field, simulator.circuitDisplay);
                    //This is here only if we can't save a circuit view properly.
                    CircuitStorage.saveSubcircuit(field, simulator.circuitBoard);
                    ToastBar.showMessage("Circuit saved to " + field, FontImage.MATERIAL_INFO);
                    simulator.menuDisplay.removeComponent(psave);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                    psload.removeActionListener(psaveListener);
                };
            case "load":
                return evt -> {
                    String field = pload.getSelectedString();
                    CircuitView newCircuitView = CircuitStorage.loadCircuitView(field);
                    simulator.circuitDisplay = newCircuitView;
                    ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                    simulator.menuDisplay.removeComponent(pload);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                    psload.removeActionListener(ploadListener);
                };
            case "sload":
                return evt -> {
                    String field = psload.getSelectedString();
                    System.out.println(field);
                    TruthTable subcircuitTruthTable = CircuitStorage.loadSubcircuit(field);
                    switch(field){
                        case "circuit0":
                            insertSubCircuit(subcircuitTruthTable, 0);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            CircuitView.mode = UserMode.EDIT;
                            CircuitView.enableDrag(CircuitView.slots);
                            break;
                        case "circuit1":
                            insertSubCircuit(subcircuitTruthTable, 1);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            CircuitView.mode = UserMode.EDIT;
                            CircuitView.enableDrag(CircuitView.slots);
                            break;
                        case "circuit2":
                            insertSubCircuit(subcircuitTruthTable, 2);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            CircuitView.mode = UserMode.EDIT;
                            CircuitView.enableDrag(CircuitView.slots);
                            break;
                        case "circuit3":
                            insertSubCircuit(subcircuitTruthTable, 3);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            CircuitView.mode = UserMode.EDIT;
                            CircuitView.enableDrag(CircuitView.slots);
                            break;
                        case "circuit4":
                            insertSubCircuit(subcircuitTruthTable, 4);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            CircuitView.mode = UserMode.EDIT;
                            CircuitView.enableDrag(CircuitView.slots);
                            break;
                        default:
                            ToastBar.showMessage("Cancel subcircuit loading", FontImage.MATERIAL_INFO);
                            break;
                    }
                    simulator.menuDisplay.removeComponent(psload);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                    psload.removeActionListener(psloadListener);
                };
            default:
                System.out.println("Error @ MenuGateListener in createListener");
                return null;
        }
    }
}
