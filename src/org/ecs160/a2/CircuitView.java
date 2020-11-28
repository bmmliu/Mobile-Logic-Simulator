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

    public static UserMode mode; // TODO: mode could be a global variable
    public Container appLayout = new Container(new BorderLayout());

    CircuitView(UserViewForm _simulator_) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;

        initCircuitView(simulator);
        add(appLayout);
    }

    private void initCircuitView(UserViewForm simulator) {
        mode = UserMode.EDIT;

        // For now, just put layout into form init as layeredlayout
        this.setLayout(new LayeredLayout());

        // Create the circuitBoard and ButtonList components
        final Container circuitBoardContainer = new Container(new GridLayout(10, 10));
        final Container buttonListContainer = new Container(new GridLayout(1, 5));

        // Create 3 different types of components: empty, placeHolder1, and placeHolder2
        // Button pH1 and pH2 will be draggable
        ArrayList<Slot> slots = new ArrayList<Slot>();

        // To initalize the circuitBoard, place empty labels to all of them
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
                        System.out.println("Add wire?");
                        // Technically we don't need to check if deleting slot is empty but just for consistency
                    } else if (mode == UserMode.DELETE && !s.isSlotType("empty")) {
                        s.emptySlot();
                    }
                }
            });
        }

        // To initalize the buttonList, place 5 different buttons into them
        // The first button is for adding wire, which we will not have for now
        // This will switch all components to nondraggable
        Button wireEdit = new Button("Wire");

        // The second and third buttons are for PlaceHold objects,
        // and will create PlaceHolder object when clicked
        Button pH1Select = new Button("P1");
        Button pH2Select = new Button("P2");

        // The forth button let user delete unwanted components
        // This will switch all components to nondraggable
        Button trashCan = new Button ("Del");

        // The final button let user to move around the component
        // This will switch all appropriate components to draggable
        // Upon user clicking any buttons to add new gates, it wlll switch back to edit mode
        Button circuitEdit = new Button("Move");

        buttonListContainer.addComponent(wireEdit);
        buttonListContainer.addComponent(pH1Select);
        buttonListContainer.addComponent(pH2Select);
        buttonListContainer.addComponent(trashCan);
        buttonListContainer.addComponent(circuitEdit);

        appLayout.addComponent(BorderLayout.CENTER, circuitBoardContainer);
        appLayout.addComponent(BorderLayout.SOUTH, buttonListContainer);

        // When clicked "Wire" button, user can add wire to connect each gates
        // TODO: For now, "Wire" button make all components nondraggable to make them clickable
        wireEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                mode = UserMode.WIRE;
                disableDrag(slots);
            }
        });

        // When clicked P1/P2, object P1/P2 should be spawned at the first available slot
        pH1Select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                mode = UserMode.EDIT;
                enableDrag(slots);
                for (int i = 0; i < slots.size(); i++) {
                    Slot s = slots.get(i);
                    if (s.isSlotType("empty")) {
                        s.setSlot("P2");

                        s.addDropListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {  // Correcting idenifiers
                                for (int j = 0; j < slots.size(); j++) if (circuitBoardContainer.getComponentIndex(slots.get(j)) != slots.get(j).getId()) slots.get(j).setId(j);
                            }
                        });
                        break;
                    }
                }
                simulator.show();
            }
        });

        pH2Select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                mode = UserMode.EDIT;
                enableDrag(slots);
                for (int i = 0; i < slots.size(); i++) {
                    Slot s = slots.get(i);
                    if (s.isSlotType("empty")) {
                        s.setSlot("P1");
                        s.addDropListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent evt) {  // Correcting idenifiers
                                for (int j = 0; j < slots.size(); j++) if (circuitBoardContainer.getComponentIndex(slots.get(j)) != slots.get(j).getId()) slots.get(j).setId(j);
                            }
                        });
                        break;
                    }
                }
                simulator.show();
            }
        });

        // Trash can will enable user to delete object on click
        trashCan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                mode = UserMode.DELETE;
                disableDrag(slots);
            }
        });

        // Move will enable user to move object around
        circuitEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                evt.consume();
                mode = UserMode.EDIT;
                enableDrag(slots);
            }
        });
    }

    public void enableDrag(ArrayList<Slot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOnDrag();
        }
    }

    public void disableDrag(ArrayList<Slot> slots) {
        for (int i = 0; i < slots.size(); i++) {
            slots.get(i).turnOffDrag();
        }
    }

    public Slot findSlot(ArrayList<Slot> slots, int id) {
        for (int i = 0; i < slots.size(); i++) {
            if (slots.get(i).getId() == id) {
                return slots.get(i);
            }
        }
        return null;
    }
}
