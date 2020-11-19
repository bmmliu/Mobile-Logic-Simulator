package org.ecs160.a2;

import com.codename1.ui.Form;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BorderLayout;

public class UserViewForm extends Form {
    CircuitView circuitDisplay = new CircuitView();
    MenuView menuDisplay = new MenuView();
    TruthTableView truthTableDisplay = new TruthTableView();

    public UserViewForm(String title) {
        setTitle(title);
        setLayout(new BorderLayout());

        initMenuView();
        initTruthTableView();
        initCircuitView();

        show();
    }

    public void initMenuView() {
        add(BorderLayout.CENTER, menuDisplay.initMenuView());
    }

    public void initTruthTableView() {

    }

    public void initCircuitView() {
        Label temp = new Label("circuit placeholder");
        add(BorderLayout.NORTH, temp);
    }

}
