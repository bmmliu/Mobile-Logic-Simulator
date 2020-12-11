package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;


// AddGateListener is a generalized event listener for adding gates. This allows us to have one action listener
// for all of our different gate types
public class AddGateListener implements ActionListener {
    private  CircuitBoard circuitBoard;
    private UserViewForm simulator;

    // Because this will be adding gates to a circuitboard, it needs access to said circuitboard.
    public AddGateListener(CircuitBoard cb, UserViewForm sim) {
        circuitBoard = cb;
        simulator = sim;
    }

    // actionPerformed required for Actionlistener
    public void actionPerformed(ActionEvent evt) {
        Button button = (Button)evt.getSource();
        String buttonText = button.getName();
        evt.consume();
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

    // Abstract way to get a specific kind of gate given the content of its name.
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
            default: // This will never happen, but in case it does you can't go wrong with an AND gate.
                return new AndGate(slot);
        }
    }

}
