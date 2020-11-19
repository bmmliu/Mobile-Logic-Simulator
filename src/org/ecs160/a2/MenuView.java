package org.ecs160.a2;

import com.codename1.ui.Container;
import com.codename1.ui.layouts.GridLayout;

public class MenuView {
    Container menu = new Container(new GridLayout(2, 1)); // Contains TopView and BotView
    TopView topDisplay = new TopView();
    BotView botDisplay = new BotView();

    public MenuView() {

    }

    public Container initMenuView() {
        menu.add(topDisplay.initTopView());
        menu.add(botDisplay.initBotView());
        return menu;
    }
}
