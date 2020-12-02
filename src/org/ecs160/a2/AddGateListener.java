package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

public class AddGateListener implements ActionListener {
    private  CircuitBoard circuitBoard;
    private UserViewForm simulator;

    public AddGateListener(CircuitBoard cb, UserViewForm sim) {
        circuitBoard = cb;
        simulator = sim;
    }

    public void actionPerformed(ActionEvent evt) {
        Button button = (Button)evt.getSource();
        String buttonText = button.getName();
        evt.consume();
        CircuitView.mode = UserMode.EDIT;
        CircuitView.enableDrag(CircuitView.slots);
        for (int i = 0; i < CircuitView.slots.size(); i++) {
            Slot s = CircuitView.slots.get(i);
            if (s.isEmpty()) {
                Gate gate = getGateByType(buttonText, s);
                circuitBoard.addGate(gate);
                s.setSlot(gate);
                simulator.show();
                break;
            }
        }
        simulator.show();
    }

    private Gate getGateByType(String buttonText, Slot slot) {
        switch (buttonText) {
            case "andGate":
                return new AndGate(slot);
            case "nandGate":
                return new NandGate(slot);
            case "norGate":
                return new NorGate(slot);
            case "notGate":
                return new NotGate(slot);
            case "orGate":
                return new OrGate(slot);
            case "xnorGate":
                return new XnorGate(slot);
            case "xorGate":
                return new XorGate(slot);
            default: // This is to make the compiler not complain to me
                return new AndGate(slot);
        }
    }

}
