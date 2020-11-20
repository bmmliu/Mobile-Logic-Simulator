package org.ecs160.a2;

import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.layouts.Layout;

public class UserViewForm extends Form {

    public CircuitView circuitDisplay;
    public MenuView menuDisplay;
    public TruthTableView truthTableDisplay;

    public UserViewForm(String title, Layout layoutFormat) {
        super(title, layoutFormat);

        System.out.print("Initializing");

        circuitDisplay = new CircuitView(this);
        menuDisplay = new MenuView(this);
        truthTableDisplay = new TruthTableView(this);
    }

    public void show() {
        this.add(BorderLayout.NORTH, circuitDisplay);
        this.add(BorderLayout.SOUTH, menuDisplay);

        super.show();
    }

}
