package org.ecs160.a2;


import com.codename1.ui.Component;
import com.codename1.ui.Display;
import com.codename1.ui.Font;
import com.codename1.ui.Graphics;

// TODO: Change the font size and color
public class LabelComponent extends Component {
    private int x = 0;
    private int y = 0;
    String name;

    public LabelComponent(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public LabelComponent(LabelComponent label) {
        x = label.x;
        y = label.y;
        name = label.name;
    }

    // Rename constructor
    public LabelComponent(LabelComponent label, String name) {
        x = label.x;
        y = label.y;
        this.name = name;
    }

    // Relocate constructor
    public LabelComponent(LabelComponent label, int x, int y) {
        this.x = x;
        this.y = y;
        this.name = label.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(0x000000);

        int fontSize = Display.getInstance().convertToPixels(2);

        Font ttfFont = Font.createTrueTypeFont("OpenSans", "OpenSans-Bold.ttf").
                derive(fontSize, Font.STYLE_PLAIN);

        g.setFont(ttfFont);

        g.drawString(name, x, y);
    }
}
