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
 * Spin Slider Frame
 * 
 * Created on Feb 2, 2006
 */

import self.lesfoster.cylindrical_alignment.utils.GuiUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.Hashtable;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;

/**
 * Special control to popup and let the user control the spin rate of the cylinder.
 *  
 * @author Leslie L Foster
 */
    public class DragFactorSliderPanel extends JPanel {

	private static final double DRAG_RATE_CONVERSION_FACTOR = 300.0;

	private static final long serialVersionUID = -1;

	private static final int WIDTH = 300;
	private static final int HEIGHT = 100;

	/** Keep the affector around. */
	private CylinderPositioningEffector affectorInstance;
	private JSlider slider;

	/**
	 * Private constructor.  Forces singleton.
	 *
	 * @param affector what to call to change the speed of spin.
	 */
	public DragFactorSliderPanel(CylinderPositioningEffector affector) {
		affectorInstance = affector;
		int defaultDragSetting = convertFromDragRate(CylinderPositioningEffector.DEFAULT_MOUSE_ROTATE_FACTOR);
		slider = new JSlider(
				CylinderPositioningEffector.SLOW_DRAG, CylinderPositioningEffector.FAST_DRAG, defaultDragSetting);
        slider.setInverted( true );
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
			    JSlider source = (JSlider)e.getSource();
			    if (!source.getValueIsAdjusting()) {
			        int factor = (int)source.getValue();
			        affectorInstance.setMouseRotatorFactor(convertToDragRate(factor));
			    }
			}
		});
		setLayout(new BorderLayout());

        // Create the label table
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(new Integer( CylinderPositioningEffector.SLOW_DRAG ), new JLabel("Slow") );
        labelTable.put(new Integer( CylinderPositioningEffector.FAST_DRAG ), new JLabel("Fast") );
		slider.setPaintTicks(false);
		slider.setPaintLabels(true);

		slider.setLabelTable( labelTable );

		JPanel controlsPanel = new JPanel();
		controlsPanel.setLayout(new BorderLayout());
		slider.setBorder(new TitledBorder(LineBorder.createGrayLineBorder(), "Adjust Cylinder Positioning Drag Rate"));
		controlsPanel.add(slider, BorderLayout.CENTER);

		add(controlsPanel, BorderLayout.CENTER);
	}

    public Dimension getPreferredSize() {
        return new Dimension( WIDTH, HEIGHT );
    }

	private double convertToDragRate(int sliderRate) {
		double value = sliderRate / DRAG_RATE_CONVERSION_FACTOR;
		return value;
	}
	
	private int convertFromDragRate( double dragRate ) {
		int value = (int)(dragRate * DRAG_RATE_CONVERSION_FACTOR);
		return value;
	}

}
