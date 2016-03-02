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

import javax.swing.*;
import java.awt.*;
import org.openide.util.Lookup;
import org.openide.util.Utilities;
import self.lesfoster.cylindrical_alignment.affector.Affected;
import self.lesfoster.cylindrical_alignment.affector.Affector;
import self.lesfoster.cylindrical_alignment.affector.CylinderPositioningAffector;
import self.lesfoster.cylindrical_alignment.affector.SettingsAffector;
import self.lesfoster.cylindrical_alignment.affector.SpeedAffector;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.CylinderContainer;

/**
 * Created by IntelliJ IDEA.
 * User: Leslie L Foster
 * Date: 4/22/12
 * Time: 8:41 PM
 *
 * A settings panel for the viewer.
 */
public class UnifiedSettingsPanel extends JPanel {
    private int WIDTH = 300;
    private int HEIGHT = 325;

	public UnifiedSettingsPanel(
	) {
		initWhenReady();
	}

    public UnifiedSettingsPanel(
            SpeedAffector speedAffector,
            SettingsAffector settingsAffector,
            CylinderPositioningAffector cylinderPositioningAffector ) {
        //super( "Adjust Settings" );
        initGui( speedAffector, settingsAffector, cylinderPositioningAffector );
    }

	private void initWhenReady() {
		Runnable runnable = () -> {
			CylinderContainer affected = null;
			while (true) {
				try {
					affected = Utilities.actionsGlobalContext().lookup(CylinderContainer.class);
					if (affected == null) {
						//System.out.println("No lookup.");
						Thread.sleep(300);
						continue;
					}
					break;
				} catch (InterruptedException ie) {
					// Eat this one.

				}
			}

			//System.out.println("Got the lookup");

			Affector[] affectors = affected.getAffectors();
			SpeedAffector speedAffector = null;
			for (Affector affector : affectors) {
				if (affector instanceof SpeedAffector) {
					speedAffector = (SpeedAffector) affector;
				}
			}
			initGui(speedAffector, null, null);
		};
		new Thread(runnable).start(); //Out of AWT thread.
	}

    /** Build out the GUI with all the affectors known here. */
    private void initGui(
            SpeedAffector speedAffector,
            SettingsAffector settingsAffector,
            CylinderPositioningAffector cylinderPositioningAffector ) {

        SpinSliderPanel spinSliderPanel = new SpinSliderPanel( speedAffector );
        SelectionEnvelopPanel selectionEnvelopPanel = new SelectionEnvelopPanel( settingsAffector );
        DragFactorSliderPanel dragFactorSliderPanel = new DragFactorSliderPanel( cylinderPositioningAffector );

        GridBagLayout gridBagLayout = new GridBagLayout();
        this.setLayout( gridBagLayout );

        int gridX = 1;
        int gridY = 1;

        int gridWidth = 1;
        int gridHeight = 1;

        double weightX = 4.0;
        double weightY = 4.0;

        int anchor = GridBagConstraints.WEST;
        int fill = GridBagConstraints.HORIZONTAL;

        int insetsTop = 0;
        int insetsLeft = 5;
        int insetsBottom = 0;
        int insetsRight = 5;
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);

        int ipadx = 0;
        int ipady = 0;

        GridBagConstraints spinSliderConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( spinSliderPanel, spinSliderConstraints );

        gridY++;
        GridBagConstraints selectionEnvelopConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( selectionEnvelopPanel, selectionEnvelopConstraints );

        gridY++;
        GridBagConstraints dragFactorConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( dragFactorSliderPanel, dragFactorConstraints );

        JButton resetCylinderButton = new JButton( "Re-set Cylinder Position" );
        resetCylinderButton.addActionListener(new ResetCylinderPositionActionListener(cylinderPositioningAffector));
        gridY++;
        GridBagConstraints resetCylinderConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, GridBagConstraints.NONE, insets, ipadx, ipady
        );
        add( resetCylinderButton, resetCylinderConstraints );

        JCheckBox freezeCylinderCheckbox = new JCheckBox( "Freeze Cylinder Position" );
        freezeCylinderCheckbox.addActionListener( new FreezeCylinderActionListener( cylinderPositioningAffector ) );
        gridY++;
        GridBagConstraints freezeCylinderConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( freezeCylinderCheckbox, freezeCylinderConstraints );

        JCheckBox dragAroundYCheckbox = new JCheckBox( "Drag Cylinder around Y Axis Only" );
        dragAroundYCheckbox.addActionListener( new DragAroundYActionListener( cylinderPositioningAffector ) );
        gridY++;
        GridBagConstraints dragAroundYConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( dragAroundYCheckbox, dragAroundYConstraints );

        JCheckBox antiAliasCheckbox = new JCheckBox( "Antialias" );
        antiAliasCheckbox.addActionListener( new AntialiasActionListener( settingsAffector ) );
        gridY++;
        GridBagConstraints antiAliasConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( antiAliasCheckbox, antiAliasConstraints );

        //
        JCheckBox secDirLightCheckbox = new JCheckBox( "Second Directional Light" );
        secDirLightCheckbox.setSelected( true );  // On by default.
        secDirLightCheckbox.addActionListener( new SecondDirectionalLightActionListener( settingsAffector ) );
        gridY++;
        GridBagConstraints secDirConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( secDirLightCheckbox, secDirConstraints );
		/*
        GuiUtils.upperLeftLocation(this, WIDTH, HEIGHT);
        GuiUtils.setIcon(this);
        setResizable( false );
	    */
		invalidate();
		repaint();
    }
}
