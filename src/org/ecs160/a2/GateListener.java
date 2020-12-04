package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

// TODO: Rename class MenuGateListener
public class GateListener implements ActionListener {
    private CircuitBoard circuitBoard;
    private UserViewForm simulator;

    public GateListener(CircuitBoard cb, UserViewForm sim) {
        circuitBoard = cb;
        simulator = sim;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        Button button = (Button)evt.getSource();
        String buttonText = button.getName();
        evt.consume();
        if (CircuitView.mode == UserMode.PDELAY) {
            return;
        }

        CircuitView.mode = UserMode.EDIT;
        CircuitView.enableDrag(CircuitView.slots);
        for (int i = 0; i < CircuitView.slots.size(); i++) {
            Slot s = CircuitView.slots.get(i);
            if (s.isEmpty()) {
                //System.out.println(buttonText);
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
                System.out.println("Error @GateListener for getGateByType");
                return null;
        }
    }
}
