package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

public class MenuOperationListener implements ActionListener{
    private CircuitBoard circuitBoard;
    private UserViewForm simulator;

    MenuOperationListener(CircuitBoard cb, UserViewForm _simulator_) {
        circuitBoard = cb;
        simulator = _simulator_;
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Button Operation_button = (Button) event.getSource();
        String ButtonText = Operation_button.getName();
        event.consume();

        if (CircuitView.mode == UserMode.PDELAY) {
            return;
        }

        switch (ButtonText) {
            case "▶":
                CircuitView.mode = UserMode.RUNNING;
                circuitBoard.runSimulation();
                simulator.show();
                break;
            case "▌▌":
                CircuitView.mode = UserMode.EDIT;
                break;
            case "Wire":
                CircuitView.mode = UserMode.WIRE;
                CircuitView.disableDrag(CircuitView.slots);
                break;
            case "Edit":
                CircuitView.mode = UserMode.EDIT;
                CircuitView.enableDrag(CircuitView.slots);
                break;
            case "Delete":
                CircuitView.mode = UserMode.DELETE;
                CircuitView.disableDrag(CircuitView.slots);
                break;
            default:
                System.out.println("Error @MenuOperationListener in actionPerform");
                break;
        }
    }
}
