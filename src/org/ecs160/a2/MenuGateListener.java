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

import java.util.Stack;
import java.util.function.ToDoubleBiFunction;

public class MenuGateListener implements ActionListener{
    private UserViewForm simulator;
    private Picker psave = new Picker();
    private Picker pload = new Picker();
    private Picker psload = new Picker();
    String saved_thing;

    MenuGateListener(UserViewForm _simulator_) {
        simulator = _simulator_;
    }

    public void actionPerformed(ActionEvent event) {
        Button digitButton = (Button)event.getSource();
        String buttonText = digitButton.getText();

        switch (buttonText) {
            case "save":
                //Picker psave = new Picker();
                psave.setType(Display.PICKER_TYPE_STRINGS);
                psave.setStrings("circuit0", "circuit1","circuit2", "circuit3","circuit4");

                psave.addActionListener((event1)->{

                    String field = psave.getSelectedString();
                    // TODO: save the current circuit to reg.
                    ToastBar.showMessage("Circuit saved to " + field, FontImage.MATERIAL_INFO);
                    simulator.menuDisplay.removeComponent(psave);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                });

                simulator.menuDisplay.add(BorderLayout.SOUTH, psave);
                simulator.menuDisplay.revalidate();
                simulator.show();
                break;
            case "load":
                //Picker psave = new Picker();
                pload.setType(Display.PICKER_TYPE_STRINGS);
                pload.setStrings("circuit0", "circuit1","circuit2", "circuit3","circuit4");


                pload.addActionListener((event1)->{

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
                    }
                    ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                    simulator.menuDisplay.removeComponent(pload);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                });

                simulator.menuDisplay.add(BorderLayout.SOUTH, pload);
                simulator.menuDisplay.revalidate();
                simulator.show();
                break;
            case "sload":
                //Picker psave = new Picker();
                psload.setType(Display.PICKER_TYPE_STRINGS);
                psload.setStrings("circuit0", "circuit1","circuit2", "circuit3","circuit4");

                psload.addActionListener((event1)->{

                    String field = psload.getSelectedString();
                    // TODO: load the selected circuit as component to field.
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
                    }
                    ToastBar.showMessage(field + " loaded", FontImage.MATERIAL_INFO);
                    simulator.menuDisplay.removeComponent(psload);
                    simulator.menuDisplay.revalidate();
                    simulator.show();
                });

                simulator.menuDisplay.add(BorderLayout.SOUTH, psload);
                simulator.menuDisplay.revalidate();
                simulator.show();
                break;
            default:
        }
    }
}
