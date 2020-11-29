package org.ecs160.a2;


import com.codename1.ui.Component;
import com.codename1.ui.Graphics;

// TODO: Change the font size and color
public class NameComponent extends Component {
    private int x = 0;
    private int y = 0;
    String name;

    public NameComponent(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    // Rename constructor
    public NameComponent(NameComponent label, String name) {
        x = label.x;
        y = label.y;
        this.name = name;
    }

    // Relocate constructor
    public NameComponent(NameComponent label, int x, int y) {
        this.x = x;
        this.y = y;
        this.name = label.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public void paint(Graphics g) {
        g.drawString(name, x, y);
    }
}
