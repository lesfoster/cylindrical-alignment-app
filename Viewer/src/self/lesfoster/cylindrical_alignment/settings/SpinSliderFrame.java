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

import javax.swing.JFrame;

import java.awt.BorderLayout;
import self.lesfoster.cylindrical_alignment.affector.SpeedAffector;

/**
 * Special control to popup and let the user control the spin rate of the cylinder.
 *  
 * @author Leslie L Foster
 */
public class SpinSliderFrame extends JFrame {

	private static final long serialVersionUID = -1;

	/** Will keep the instance hidden here */
	private static SpinSliderFrame instance;
	
	/**
	 * Private constructor.  Forces singleton.
	 * 
	 * @param affector what to call to change the speed of spin.
	 */
	private SpinSliderFrame(SpeedAffector affector) {
        SpinSliderPanel panel = new SpinSliderPanel( affector );
		getContentPane().add(panel, BorderLayout.CENTER);

		GuiUtils.upperLeftLocation(this, panel.getPreferredSize().width, panel.getPreferredSize().height);
		GuiUtils.setIcon(this);
	}

	/**
	 * Return only possible instance. 
	 * 
	 * @return the instance.  Create here if needed.
	 * @param affector what to call to change the speed of spin.
	 */
	public static SpinSliderFrame getInstance(SpeedAffector affector) {
		if (affector == null)
			throw new RuntimeException("Need instance of affector");

		if (instance == null) {
			instance = new SpinSliderFrame(affector);
		}
		return instance;
	}

}
