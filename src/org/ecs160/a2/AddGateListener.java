package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

/**
 * Listener for adding Gates
 */
public class AddGateListener implements ActionListener {
    private  CircuitBoard circuitBoard;
    private UserViewForm simulator;

    /**
     * Create a new And Gate Listener
     * @param cb CircuitBoard to place Gate in
     * @param sim The parent UserViewForm
     */
    public AddGateListener(CircuitBoard cb, UserViewForm sim) {
        circuitBoard = cb;
        simulator = sim;
    }

    /**
     * Depending on the gate button pressed, add a new gate to the CircuitBoard
     * @param evt
     */
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

    /**
     * Return a new gate based on the button pressed
     * @param buttonText Type of gate
     * @param slot Slot to place gate in
     * @return The new Gate
     */
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
            default:
                return new AndGate(slot);
        }
    }

}
