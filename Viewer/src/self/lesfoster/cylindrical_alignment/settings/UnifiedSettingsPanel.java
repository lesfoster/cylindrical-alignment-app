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
import java.util.Collection;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.Lookups;
import self.lesfoster.cylindrical_alignment.effector.Effector;
import self.lesfoster.cylindrical_alignment.effector.CylinderPositioningEffector;
import self.lesfoster.cylindrical_alignment.effector.Effected;
import self.lesfoster.cylindrical_alignment.effector.SettingsEffector;
import self.lesfoster.cylindrical_alignment.effector.SpeedEffector;

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
	
	private Lookup.Result<Effected> effectedLookupResult;
	public LookupListener effectedLookupListener;

	public UnifiedSettingsPanel() {
		initWhenReady();
	}

    public UnifiedSettingsPanel(
            SpeedEffector speedEffector,
            SettingsEffector settingsEffector,
            CylinderPositioningEffector cylinderPositioningEffector ) {
        //super( "Adjust Settings" );
        initGui( speedEffector, settingsEffector, cylinderPositioningEffector );
    }

	private void initWhenReady() {
		SwingWorker swingWorker = new SwingWorker() {
			private Effected effected;
			@Override
			protected Object doInBackground() throws Exception {
				final Lookup lookup = Utilities.actionsGlobalContext();
				//final Lookup lookup = Lookups.forPath("Viewer/CylinderContainer");
				//effected = (Effected)WindowManager.getDefault().findTopComponent("ViewerTopComponent").getLookup().lookupResult(Effected.class);
				effectedLookupResult = lookup.lookupResult(Effected.class);				
				effectedLookupListener = new LookupListener() {
					@Override
					public void resultChanged(LookupEvent le) {
						Collection<? extends Effected> effectedList = effectedLookupResult.allInstances();
						if (effectedList.size() >= 1) {
							effected = effectedList.iterator().next();
							launchInit(effected);
						}
					}
					
				};
				effectedLookupResult.addLookupListener(effectedLookupListener);
				return effectedLookupResult;
			}
			
			@Override
			protected void done() {
			}
			
		};
		swingWorker.execute();
	}

	public void launchInit(Effected effected) {
		System.out.println("Launching settings panel...");
		final Effector[] effectors = effected.getEffectors();
		SpeedEffector speedEffector = null;
		SettingsEffector settingsEffector = null;
		CylinderPositioningEffector cylPosEffector = null;
		for (Effector effector : effectors) {
			if (effector instanceof SpeedEffector) {
				speedEffector = (SpeedEffector) effector;
			} else if (effector instanceof SettingsEffector) {
				settingsEffector = (SettingsEffector) effector;
			} else if (effector instanceof CylinderPositioningEffector) {
				cylPosEffector = (CylinderPositioningEffector) effector;
			}
		}
		initGui(speedEffector, settingsEffector, cylPosEffector);
	}

	/** Build out the GUI with all the effectors known here. */
    private void initGui(
            SpeedEffector speedEffector,
            SettingsEffector settingsEffector,
            CylinderPositioningEffector cylinderPositioningEffector ) {

        SpinSliderPanel spinSliderPanel = new SpinSliderPanel( speedEffector );
        SelectionEnvelopPanel selectionEnvelopPanel = new SelectionEnvelopPanel( settingsEffector );
        DragFactorSliderPanel dragFactorSliderPanel = new DragFactorSliderPanel( cylinderPositioningEffector );

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
        resetCylinderButton.addActionListener(new ResetCylinderPositionActionListener(cylinderPositioningEffector));
        gridY++;
        GridBagConstraints resetCylinderConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, GridBagConstraints.NONE, insets, ipadx, ipady
        );
        add( resetCylinderButton, resetCylinderConstraints );

        JCheckBox freezeCylinderCheckbox = new JCheckBox( "Freeze Cylinder Position" );
        freezeCylinderCheckbox.addActionListener(new FreezeCylinderActionListener( cylinderPositioningEffector ) );
        gridY++;
        GridBagConstraints freezeCylinderConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( freezeCylinderCheckbox, freezeCylinderConstraints );

        JCheckBox dragAroundYCheckbox = new JCheckBox( "Drag Cylinder around Y Axis Only" );
        dragAroundYCheckbox.addActionListener(new DragAroundYActionListener( cylinderPositioningEffector ) );
        gridY++;
        GridBagConstraints dragAroundYConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( dragAroundYCheckbox, dragAroundYConstraints );

		/*
		   Anti-Alias is always-on for JavaFX.  May drop this later. LLF
		
        JCheckBox antiAliasCheckbox = new JCheckBox( "Antialias" );
        antiAliasCheckbox.addActionListener(new AntialiasActionListener( settingsEffector ) );
        gridY++;
        GridBagConstraints antiAliasConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( antiAliasCheckbox, antiAliasConstraints );
		*/

        /*
			2nd light is not supported as yet.  Consider again later.
		
        JCheckBox secDirLightCheckbox = new JCheckBox( "Second Directional Light" );
        secDirLightCheckbox.setSelected( true );  // On by default.
        secDirLightCheckbox.addActionListener(new SecondDirectionalLightActionListener( settingsEffector ) );
        gridY++;
        GridBagConstraints secDirConstraints = new GridBagConstraints(
                gridX, gridY, gridWidth, gridHeight, weightX, weightY, anchor, fill, insets, ipadx, ipady
        );
        add( secDirLightCheckbox, secDirConstraints );
		*/
		/*
        GuiUtils.upperLeftLocation(this, WIDTH, HEIGHT);
        GuiUtils.setIcon(this);
        setResizable( false );
	    */
		validate();
		invalidate();
		repaint();
    }
}
