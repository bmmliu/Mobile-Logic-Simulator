package org.ecs160.a2;

import com.codename1.ui.Button;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;

public class MenuOperationListener implements ActionListener{
    private UserViewForm simulator;

    MenuOperationListener(UserViewForm _simulator_) {
        simulator = _simulator_;
    }

    public void actionPerformed(ActionEvent event) {
        Button Operation_button = (Button) event.getSource();
        String ButtonText = Operation_button.getText();

        switch (ButtonText) {
            case "▶":
                Operation_button.setText("works");
                break; // TODO
            case "▌▌":
                Operation_button.setText("works1");
                break; // TODO
        }
    }
}
