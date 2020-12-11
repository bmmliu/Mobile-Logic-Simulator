package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.geom.Dimension;

import java.util.HashMap;

public class Slot extends Button{
    private int width = Display.getInstance().getDisplayWidth();
    private int id;
    private int gridPostion;
    private Gate gate;

    private ActionListener movingAction = evt -> {
        CircuitView.wire.rearrangeWire(this);
        moveLabel();
    };

    //Constructor for an empty slot
    public Slot(){
        this.gate = null;
        setName("empty");
        setText(" ");
        setIcon(AppMain.theme.getImage("empty_slot.jpg"));
        setDropTarget(true);
        setDraggable(false);
        setVisible(false);
        return;
    }

    public Slot(Gate g) {
        setSize(new Dimension(width/10, width/10));
        //getAllStyles().setBorder(Border.createLineBorder(1, 0x00000f));

        this.gate = g;
        setName(g.getLabelName());
        setText(g.getLabelName());
        getAllStyles().setFgColor(0xb8dffc);
        setDraggable(true);
        setDropTarget(false);
        return;
    }

    public Slot(Slot s) {
        setName(s.getName());
        setText(s.getText());
        setIcon(s.getIcon());
        setDraggable(s.isDraggable());
        setDropTarget(s.isDropTarget());
        setVisible(s.isVisible());

        CircuitBoard c = CircuitView.simulator.circuitDisplay.copyCircuitBoard;
        switch (s.gate.gateType) {
            case INPUT_PIN:
                InputPin tempInGate = new InputPin(s.gate);
                c.copyInputPins.put(tempInGate.getLabelName(), tempInGate);
                gate = tempInGate;
                break;
            case OUTPUT_PIN:
                OutputPin tempOutGate = new OutputPin(s.gate);
                c.copyOutputPins.put(tempOutGate.getLabelName(), tempOutGate);
                gate = tempOutGate;
                break;
            case AND_GATE:
                gate = new AndGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            case OR_GATE:
                gate = new OrGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            case NAND_GATE:
                gate = new NandGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            case NOR_GATE:
                gate = new NorGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            case NOT_GATE:
                gate = new NotGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            case XNOR_GATE:
                gate = new XnorGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            case XOR_GATE:
                gate = new XorGate(s.gate);
                c.copyGates.put(gate.getLabelName(), gate);
                break;
            default:
                gate = new Gate(s.gate) {
                    @Override
                    public void calculate() {}
                };
                break;
        };
    }

    public void setGridPostion(int to) {
        gridPostion = to;
    }
    public int getGridPostion() {
        return gridPostion;
    }

    public void setId(int to) {
        id = to;
        if (gate != null) {
            gate.slotID = to;
        }
    }

    public int getId() {
        return id;
    }

    public Slot getSlot() {return this;}

    public void setSlot() {
        setToEmptyButton();
    }

    public void setSlot(Gate g) {
        this.gate = g;
        setName(gate.getLabelName());
        setText(gate.getLabelName());
        setIcon(gate.currentImage);
        getAllStyles().setFgColor(0x000000);
        setDraggable(true);
        setDropTarget(false);
        setVisible(true);
        makeMoveable();
        CircuitView.addLabel(gate.getLabel());
    }

    public void setSlot(Slot to) {
        setName(to.getName());
        setText(to.getText());
        setIcon(to.getIcon());
        getAllStyles().setFgColor(to.getAllStyles().getFgColor());
        setDraggable(to.isDraggable());
        setDropTarget(to.isDropTarget());
        setVisible(to.isVisible());
    }

    public Gate getGate() {
        return gate;
    }

    // TODO: can add more types of gates. Highly recommend consider using enum
    public boolean isSlotType(GateType gateType) {
        if(gate.gateType == gateType){
            return true;
        }
        return false;
    }

    public void update() {
        setIcon(gate.currentImage);
    }

    public boolean isEmpty(){
        return gate == null;
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
        gate.delete();
        setToEmptyButton();
    }

    private void setToEmptyButton() {
        gate = null;
        setName("empty");
        setText(" ");
        setIcon(AppMain.theme.getImage("empty_slot.jpg"));
        setDropTarget(true);
        setDraggable(false);
        setVisible(false);
        disableMove();
    }

    private void makeMoveable() { this.addDragFinishedListener(movingAction); }

    private void disableMove() { this.removeDragFinishedListener(movingAction); }

    private void moveLabel() {
        int offsetX;
        int offsetY = this.getHeight()/2 - 5;
        if (gate.gateType == GateType.INPUT_PIN || gate.gateType == GateType.OUTPUT_PIN) {
            offsetX = this.getWidth()/3 + 6;
        } else {
            offsetX = this.getWidth()/3;
        }

        LabelComponent pre = gate.getLabel();
        LabelComponent post = new LabelComponent(pre, this.getAbsoluteX()+offsetX, this.getAbsoluteY()-offsetY);
        gate.setLabel(post);
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
