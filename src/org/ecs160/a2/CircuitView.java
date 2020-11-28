package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;

import java.util.ArrayList;

enum UserMode {
    WIRE,
    DELETE,
    EDIT
}

public class CircuitView extends Container {
    UserViewForm simulator;

    public static UserMode mode;
    public static Wire wire;
    final static Container circuitBoardContainer = new Container(new GridLayout(10, 10));
    public static ArrayList<Slot> slots = new ArrayList<Slot>();

    public Container appLayout = new Container(new BorderLayout());
    Container wireLayout = new Container(new LayeredLayout());

    CircuitView(UserViewForm _simulator_) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;

        initCircuitView();
    }

    private void initCircuitView() {
        mode = UserMode.EDIT;

        wire = new Wire(wireLayout);   // TODO: Remember, needs to add circuitView Form

        // For now, just put layout into form init as layered layout
        this.setLayout(new LayeredLayout());
        add(wireLayout);
        add(appLayout);

        // To initialize the circuitBoard, place empty labels to all of them
        for (int i = 0; i < 100; i++) {
            slots.add(new Slot("empty"));
            Slot s = slots.get(slots.size()-1).getSlot();
            circuitBoardContainer.addComponent(s);
            s.setId(i);
            s.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    evt.consume();

                    // We only let user edit the wire if there is component in it
                    if (mode == UserMode.WIRE && !s.isSlotType("empty")) {
                        //System.out.println("Add wire?");
                        wire.addConnection(s);
                        simulator.show();

                        // Technically we don't need to check if deleting slot is empty but just for consistency
                    } else if (mode == UserMode.DELETE && !s.isSlotType("empty")) {
                        s.emptySlot();
                        simulator.show();
                    }
                }
            });
        }

        appLayout.addComponent(BorderLayout.CENTER, circuitBoardContainer);
    }

    static public void enableDrag(ArrayList<Slot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOnDrag();
        }
    }

    static public void disableDrag(ArrayList<Slot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOffDrag();
        }
    }

    static public Slot findSlot(ArrayList<Slot> slots, int id) {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getId() == id) {
                return slots.get(i);
            }
        }
        return null;
    }
}
