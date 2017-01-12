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
 * Created on April 22, 2012, as refactoring from SpinSliderFrame
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.Hashtable;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffector;

/**
 * Let the user control the spin rate of the cylinder.
 *  
 * @author Leslie L Foster
 */
public class SpinSliderPanel extends JPanel {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 120;

	private static final long serialVersionUID = -1;

	/** Keep the effector around. */
	private SpeedEffector effectorInstance;
	private JSlider slider;

	/**
	 * Construct with an effector.  User's choices are messaged to the effector.
     *
	 * @param affector what to call to change the speed of spin.
	 */
	public SpinSliderPanel(SpeedEffector affector) {
		effectorInstance = affector;
		slider = new JSlider(SpeedEffector.FAST_SPEED_DURATION, SpeedEffector.SLOW_SPEED_DURATION, SpeedEffector.INITIAL_SPEED_DURATION);
		slider.addChangeListener((ChangeEvent e) -> {
			JSlider source = (JSlider)e.getSource();
			if (!source.getValueIsAdjusting()) {				
				int spinDuration = source.getValue();
				System.out.println("Setting spin duration to " + spinDuration);
				effectorInstance.setDuration(spinDuration);
			}
		});
		setLayout(new BorderLayout());

        // Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(SpeedEffector.SLOW_SPEED_DURATION, new JLabel("Slow") );
		labelTable.put(SpeedEffector.FAST_SPEED_DURATION, new JLabel("Fast") );
		slider.setPaintTicks(false);
		slider.setPaintLabels(true);

		slider.setLabelTable( labelTable );

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BorderLayout());
		slider.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Adjust Rotation Rate"));
		controlsPanel.add(slider, BorderLayout.CENTER);

		//  Establish a checkbox that will stop the spinning.  If unchecked, the spin
		//  will be back under the control of the slider.
		JCheckBox stopRotation = new JCheckBox("Stop Rotation");
		controlsPanel.add(stopRotation, BorderLayout.NORTH);
		stopRotation.addItemListener((ItemEvent ie) -> {
			if (ie.getStateChange() == ItemEvent.DESELECTED) {
				slider.setEnabled(true);
				effectorInstance.setDuration(slider.getValue());
			}
			else if (ie.getStateChange() == ItemEvent.SELECTED) {
				effectorInstance.setImmobile();
				slider.setEnabled(false);
			}
		});
		
		add(controlsPanel, BorderLayout.CENTER);
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension( WIDTH, HEIGHT );
    }

}
