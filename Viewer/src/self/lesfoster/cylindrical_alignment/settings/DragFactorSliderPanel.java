/*
 CDDL HEADER START

 The contents of this file are subject to the terms of the
 Common Development and Distribution License (the "License").
 You may not use this file except in compliance with the License.

 You can obtain a copy of the license at
   https://opensource.org/licenses/CDDL-1.0.
 See the License for the specific language governing permissions
 and limitations under the License.

 When distributing Covered Code, include this CDDL HEADER in each
 file and include the License file at
    https://opensource.org/licenses/CDDL-1.0.
 If applicable, add the following below this CDDL HEADER, with the
 fields enclosed by brackets "[]" replaced with your own identifying
 information: Portions Copyright [yyyy] [name of copyright owner]

 CDDL HEADER END
 */
package self.lesfoster.cylindrical_alignment.settings;

/*
 * Spin Slider Frame
 * 
 * Created on Feb 2, 2006
 */
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.util.Hashtable;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;

/**
 * Special control to popup and let the user control the spin rate of the
 * cylinder.
 *
 * @author Leslie L Foster
 */
public class DragFactorSliderPanel extends JPanel {

    private static final double DRAG_RATE_CONVERSION_FACTOR = 300.0;

    private static final long serialVersionUID = -1;

    private static final int DFSP_WIDTH = 300;
    private static final int DFSP_HEIGHT = 100;

    /**
     * Keep the affector around.
     */
    private CylinderPositioningEffector affectorInstance;
    private final JSlider slider;

    /**
     * Private constructor. Forces singleton.
     *
     * @param affector what to call to change the speed of spin.
     */
    public DragFactorSliderPanel(CylinderPositioningEffector affector) {
        affectorInstance = affector;
        int defaultDragSetting = convertFromDragRate(CylinderPositioningEffector.DEFAULT_MOUSE_ROTATE_FACTOR);
        slider = new JSlider(
                CylinderPositioningEffector.SLOW_DRAG, CylinderPositioningEffector.FAST_DRAG, defaultDragSetting);
        slider.setInverted(true);
        slider.addChangeListener((ChangeEvent e) -> {
            JSlider source = (JSlider) e.getSource();
            if (!source.getValueIsAdjusting()) {
                int factor = (int) source.getValue();
                affectorInstance.setMouseRotatorFactor(convertToDragRate(factor));
            }
        });
        setLayout(new BorderLayout());

        // Create the label table: type requred for called method.
        Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
        labelTable.put(CylinderPositioningEffector.SLOW_DRAG, new JLabel("Slow"));
        labelTable.put(CylinderPositioningEffector.FAST_DRAG, new JLabel("Fast"));
        slider.setPaintTicks(false);
        slider.setPaintLabels(true);

        slider.setLabelTable(labelTable);

        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BorderLayout());
        slider.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Adjust Cylinder Positioning Drag Rate"));
        controlsPanel.add(slider, BorderLayout.CENTER);

        add(controlsPanel, BorderLayout.CENTER);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(DFSP_WIDTH, DFSP_HEIGHT);
    }

    private double convertToDragRate(int sliderRate) {
        double value = sliderRate / DRAG_RATE_CONVERSION_FACTOR;
        return value;
    }

    private int convertFromDragRate(double dragRate) {
        int value = (int) (dragRate * DRAG_RATE_CONVERSION_FACTOR);
        return value;
    }

}
