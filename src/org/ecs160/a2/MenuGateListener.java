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

// TODO: Rename class SaveLoadListener
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
                psave.setStrings(pickerList);

                psave.addActionListener(psaveListener);

                simulator.menuDisplay.add(BorderLayout.SOUTH, psave);
                simulator.menuDisplay.revalidate();
                simulator.show();

                psave.pressed();
                psave.released();

                break;
            case "load":
                pload.setType(Display.PICKER_TYPE_STRINGS);
                pload.setStrings(pickerList);

                pload.addActionListener(ploadListener);

                simulator.menuDisplay.add(BorderLayout.SOUTH, pload);
                simulator.menuDisplay.revalidate();
                simulator.show();

                pload.pressed();
                pload.released();

                break;
            case "sload":
                psload.setType(Display.PICKER_TYPE_STRINGS);
                psload.setStrings(pickerList);

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
                    // TODO: save the current circuit to reg.
                    ToastBar.showMessage("Circuit saved to " + field, FontImage.MATERIAL_INFO);
                    simulator.menuDisplay.removeComponent(psave);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                    psload.removeActionListener(psaveListener);
                };
            case "load":
                return evt -> {
                    String field = pload.getSelectedString();
                    // TODO: load the selected circuit to field.
                    switch(field){
                        case "circuit0":
                            break;
                        case "circuit1":
                            break;
                        case "circuit2":
                            break;
                        case "circuit3":
                            break;
                        case "circuit4":
                            break;
                        default:    // TODO: give user a choice to not choose a gate
                            break;
                    }
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
                    // TODO: load the selected circuit as component to field with selected TruthTable
                    // FIXME: For now, each circuits will put a subcircuit shell regardless
                    switch(field){
                        case "circuit0":
                            insertSubCircuit(new TruthTable(), 0);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            break;
                        case "circuit1":
                            insertSubCircuit(new TruthTable(), 1);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            break;
                        case "circuit2":
                            insertSubCircuit(new TruthTable(), 2);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            break;
                        case "circuit3":
                            insertSubCircuit(new TruthTable(), 3);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                            break;
                        case "circuit4":
                            insertSubCircuit(new TruthTable(), 4);
                            ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
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
