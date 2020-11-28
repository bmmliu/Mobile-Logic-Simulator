package org.ecs160.a2;

import com.codename1.ui.Component;
import com.codename1.ui.Graphics;

public class wireComponent extends Component {
    private int x1 = 0;
    private int y1 = 0;
    private int x2 = 0;
    private int y2 = 0;
    private int color = 0;

    public wireComponent(int x1, int y1, int x2, int y2, int color) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.color = color;
    }

    public wireComponent(wireComponent wire, int color) {
        x1 = wire.x1;
        x2 = wire.x2;
        y1 = wire.y1;
        y2 = wire.y2;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(color);
        g.drawLine(x1, y1, x2, y2);
    }

}

