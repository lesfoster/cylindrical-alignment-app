/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments are arranged like the staves
of a barrel. The cylinder can spin, and if it spins, it can
do so at two different speeds.  When stopped, properties can
be seen for the aligned values.
Copyright (C) 2005 Leslie L. Foster

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package self.lesfoster.cylindrical_alignment.settings;

/*
 * Created on April 22, 2012, as refactoring from SpinSliderFrame
 */

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import self.lesfoster.cylindrical_alignment.affector.SpeedAffector;

/**
 * Let the user control the spin rate of the cylinder.
 *  
 * @author Leslie L Foster
 */
public class SpinSliderPanel extends JPanel {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 120;

	private static final long serialVersionUID = -1;

	/** Will keep the instance hidden here */
	private static SpinSliderPanel instance;

	/** Keep the affector around. */
	private SpeedAffector affectorInstance;
	private JSlider slider;

	/**
	 * Construct with an affector.  User's choices are messaged to the affector.
     *
	 * @param affector what to call to change the speed of spin.
	 */
	public SpinSliderPanel(SpeedAffector affector) {
		affectorInstance = affector;
		slider = new JSlider(SpeedAffector.FAST_SPEED_DURATION, SpeedAffector.SLOW_SPEED_DURATION, SpeedAffector.FAST_SPEED_DURATION);
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int spinDuration = (int)source.getValue();
			        affectorInstance.setDuration(spinDuration);
			    }
			}
		});
		setLayout(new BorderLayout());

        // Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put( new Integer( SpeedAffector.SLOW_SPEED_DURATION ), new JLabel("Slow") );
		labelTable.put( new Integer( SpeedAffector.FAST_SPEED_DURATION ), new JLabel("Fast") );
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
		stopRotation.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				if (ie.getStateChange() == ItemEvent.DESELECTED) {
					slider.setEnabled(true);
				    affectorInstance.setDuration(slider.getValue());
				}
				else if (ie.getStateChange() == ItemEvent.SELECTED) {
					affectorInstance.setImmobile();
					slider.setEnabled(false);
				}
			}
		});
		
		add(controlsPanel, BorderLayout.CENTER);
	}

    @Override
    public Dimension getPreferredSize() {
        return new Dimension( WIDTH, HEIGHT );
    }

}
