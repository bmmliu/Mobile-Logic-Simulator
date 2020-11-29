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
    public Tabs t = new Tabs();

    public UserViewForm(String title, Layout layoutFormat) {
        super(title, layoutFormat);

        System.out.print("Initializing");

        circuitDisplay = new CircuitView(this);
        menuDisplay = new MenuView(this);
        pDelayDisplay = new PDelayView(this);
        truthTableDisplay = new TruthTableView(this);

        Style s = UIManager.getInstance().getComponentStyle("Tab");
        FontImage icon1 = FontImage.createMaterial(FontImage.MATERIAL_QUESTION_ANSWER, s);
        Container container1 = BoxLayout.encloseY(new Label("Label1"), new Label("Label2"));
        t.setSwipeActivated(false);
        t.addTab("Circuit", circuitDisplay);
        t.addTab("P_Delay", pDelayDisplay);
        //t.addTab("P_Delay", new SpanLabel("Some text directly in the tab"));
        t.addTab("T_Table", truthTableDisplay);

        t.addSelectionListener(new SelectionListener() {
            @Override
            public void selectionChanged(int oldSelected, int newSelected) {
                if (oldSelected == 0) {     // If moving from Circuit to P_Delay
                    circuitDisplay.swapView();
                    pDelayDisplay.toView();
                } else if (oldSelected == 1) {
                    pDelayDisplay.swapView();
                    circuitDisplay.toView();
                }
            }
        });

        this.add(BorderLayout.CENTER, t);
        this.add(BorderLayout.SOUTH, menuDisplay);
    }
}
