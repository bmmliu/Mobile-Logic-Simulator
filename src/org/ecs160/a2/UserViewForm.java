package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.*;
import com.codename1.ui.events.SelectionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.plaf.UIManager;

public class UserViewForm extends Form {

    public CircuitView circuitDisplay;
    public MenuView menuDisplay;
    public PDelayView pDelayDisplay;
    public TruthTableView truthTableDisplay;
    public int PreviousView;
    public Tabs t = new Tabs();
    public CircuitBoard circuitBoard;

    public UserViewForm(String title, Layout layoutFormat) {
        super(title, layoutFormat);

        System.out.print("Initializing");

        circuitBoard = new CircuitBoard();

        circuitDisplay = new CircuitView(this, circuitBoard);
        menuDisplay = new MenuView(this, circuitBoard);
        pDelayDisplay = new PDelayView(this);
        truthTableDisplay = new TruthTableView(this);

        PreviousView = 0;

        Style s = UIManager.getInstance().getComponentStyle("Tab");
        FontImage icon1 = FontImage.createMaterial(FontImage.MATERIAL_QUESTION_ANSWER, s);
        Container container1 = BoxLayout.encloseY(new Label("Label1"), new Label("Label2"));
        t.setSwipeActivated(false);
        t.addTab("Circuit", circuitDisplay);
        t.addTab("P_Delay", pDelayDisplay);
        //t.addTab("P_Delay", new SpanLabel("Some text directly in the tab"));
        t.addTab("T_Table", truthTableDisplay);

        // If PreviousView is 0, then when to 2, and go back 1, need to retrieve info based on which previous view
        t.addSelectionListener(new SelectionListener() {
            @Override
            public void selectionChanged(int oldSelected, int newSelected) {
                if (PreviousView == 0 && newSelected == 1) {        // If switching from circuit to PDelay
                    circuitDisplay.swapView();
                    pDelayDisplay.toView();
                    PreviousView = 1;
                } else if (PreviousView == 1 && newSelected == 0) {  // If switching from PDelay to circuit
                    pDelayDisplay.swapView();
                    circuitDisplay.toView();
                    PreviousView = 0;
                }
            }
        });

        this.add(BorderLayout.CENTER, t);
        this.add(BorderLayout.SOUTH, menuDisplay);
    }
}
