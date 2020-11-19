package org.ecs160.a2;

import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.Container;
import com.codename1.ui.Label;

public class BotView {

    public BotView() {

    }

    public Container initBotView() {
        Container temp = new Container(new GridLayout(1, 1));
        temp.add(new Label("Gates placeholder"));
        return temp;
    }
}
