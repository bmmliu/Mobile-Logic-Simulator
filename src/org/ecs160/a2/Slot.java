package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;
import com.codename1.ui.plaf.Border;

// TODO: Slot can also added the gate component
public class Slot extends Button{
    private final Slot s = this;

    private int width = Display.getInstance().getDisplayWidth();
    private int id;
    private Gate gate;

    private ActionListener movingAction = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {
            for (int j = 0; j < CircuitView.slots.size(); j++) if (CircuitView.circuitBoardContainer.getComponentIndex(CircuitView.slots.get(j)) != CircuitView.slots.get(j).getId()) CircuitView.slots.get(j).setId(j);
            CircuitView.wire.rearrangeWire(s);
            System.out.print(gate.getName()); System.out.println(" have been dropped");
            moveLabel();
            System.out.print(gate.getName()); System.out.println(" have been dropped");
        }
    };

    // TODO: Slot constructor should take a gate component
    public Slot(String type) {
        setSize(new Dimension(width/10, width/10));
        //getAllStyles().setBorder(Border.createLineBorder(1, 0x00000f));

        if (type.equals("empty")) {
            gate = null;
            setName("empty");
            setText(" ");
            setDropTarget(true);
            setDraggable(false);
            setVisible(false);
            return;
        }
        if (type.equals("P1")) {
            gate = new P1Gate(this);
            setName("P1");
            setText("P1");
            getAllStyles().setFgColor(0xb8dffc);
            setDraggable(true);
            setDropTarget(false);
            return;
        }
        if (type.equals("P2")) {
            gate = new P2Gate(this);
            setName("P2");
            setText("P2");
            getAllStyles().setFgColor(0x000000);
            setDraggable(true);
            setDropTarget(false);
            return;
        }
    }

    public Slot(Slot s) {
        setName(s.getName());
        setText(s.getText());
        getAllStyles().setFgColor(s.getAllStyles().getFgColor());
        setDraggable(s.isDraggable());
        setDropTarget(s.isDropTarget());
    }

    public void setId(int to) {
        id = to;
    }

    public int getId() {
        return id;
    }

    // TODO: also add get gate
    public Slot getSlot() {return this;}

    // TODO: can set Slot to empty Slot by simply calling function without parameter
    // TODO: Clear the information of the gate
    public void setSlot() {
        //Slot s = new Slot("empty");
        setText(" ");
        setDropTarget(true);
        setDraggable(false);
    }

    // TODO: when setting slot, will replace String input to reference to the gate component
    public void setSlot(String type) {
        if (type.equals("empty")) {
            setToEmptyButton();
            return;
        }
        if (type.equals("P1")) {
            setToP1Button();
            return;
        }
        if (type.equals("P2")) {
            setToP2Button();
            return;
        }
    }

    public void setSlot(Slot to) {
        setName(to.getName());
        setText(to.getText());
        getAllStyles().setFgColor(to.getAllStyles().getFgColor());
        setDraggable(to.isDraggable());
        setDropTarget(to.isDropTarget());
    }

    public void setGate(Gate to) {
        gate = to;
    }

    public Gate getGate() {
        return gate;
    }

    // TODO: can add more types of gates. Highly recommend consider using enum
    public boolean isSlotType(String type) {
        if (type.equals("empty")) {
            return getText().equals(" ");
        }
        if (type.equals("P1")) {
            return getText().equals("P1");
        }
        if (type.equals("P2")) {
            return getText().equals("P2");
        }
        return false;
    }

    public void turnOffDrag() {
        setDraggable(false);
    }
    public void turnOnDrag() {
        if (!getName().equals("empty")){
            setDraggable(true);
        }
    }

    // TODO: Add more code for clearing the gate (with wire connect and stuff)
    public void emptySlot() {
        gate.deleteGate();
        setToEmptyButton();
    }

    private void setToEmptyButton() {
        gate = null;
        setName("empty");
        setText(" ");
        setDropTarget(true);
        setDraggable(false);
        setVisible(false);
        disableMove();
    }

    private void makeMoveable() { this.addDragFinishedListener(movingAction); }

    private void disableMove() {
        this.removeDragFinishedListener(movingAction);
    }

    private void moveLabel() {
        int offsetX = this.getWidth()/2;
        int offsetY = this.getHeight()/2;

        NameComponent pre = gate.getLabel();
        NameComponent post = new NameComponent(pre, this.getAbsoluteX()-offsetX, this.getAbsoluteY()-offsetY);
        gate.setLabel(post);
        CircuitView.moveLabel(pre, post);
    }

    private void setToP1Button() {
        gate = new P1Gate(this);
        setName("P1");
        setText("P1");
        getAllStyles().setFgColor(0xb8dffc);
        setDraggable(true);
        setDropTarget(false);
        setVisible(true);
        makeMoveable();
        CircuitView.addLabel(gate.getLabel());
    }

    private void setToP2Button() {
        gate = new P2Gate(this);
        setName("P2");
        setText("P2");
        getAllStyles().setFgColor(0x000000);
        setDraggable(true);
        setDropTarget(false);
        setVisible(true);
        makeMoveable();
        CircuitView.addLabel(gate.getLabel());
    }

    @Override
    public void drop(Component dragged, int x, int y) {
        Button filter = new Button();

        dragged.getParent().replace(this, filter, null);
        dragged.getParent().replace(dragged, this, null);
        filter.getParent().replace(filter, dragged, null);

        dragged.getParent().animateLayoutAndWait(1);
    }
}
