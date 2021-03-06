package org.ecs160.a2;

import com.codename1.components.SpanLabel;
import com.codename1.ui.Button;
import com.codename1.ui.Tabs;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.util.Resources;
import com.codename1.ui.Container;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.GridLayout;

import java.util.ArrayList;

/**
 * Visualizes the total propagation delay and maximum operating frequency
 */
public class InfoView extends Container {
    UserViewForm simulator;
    public CircuitBoard circuitBoard;
    public static SpanLabel overAllPDelay;
    public static SpanLabel maxOperaFreq;
    private final static String PDelayString = "Overall Propagation Delay (ns): ";
    private final static String maxFreqString = "Max Operating Frequency (MHz): ";

    InfoView(UserViewForm _simulator_, CircuitBoard circuitBoard) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        simulator = _simulator_;
        this.circuitBoard = circuitBoard;

        overAllPDelay = new SpanLabel("Overall Propagation Delay (ns): ");
        maxOperaFreq = new SpanLabel("Max Operating Frequency (MHz): ");

        addLabels();
    }

    static public void update(ArrayList<Gate> criticalPath) {
        int pDelay = CircuitCalc.GetPropagationDelay(criticalPath);
        overAllPDelay.setText(PDelayString + pDelay);
        maxOperaFreq.setText(maxFreqString + CircuitCalc.GetMaxOperatingFrequency(pDelay));
    }

    public void addLabels() {
        this.add(overAllPDelay);
        this.add(maxOperaFreq);
    }

}