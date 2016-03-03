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
package self.lesfoster.cylindrical_alignment.effector;

import javafx.application.Platform;
import javax.swing.SwingUtilities;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.events.MouseDraggedHandler;

/*
 * Implements a way to change how mouse drags on the cylinder are interpreted.
 * 
 * todo find a way to pass in an interface off the Component, that will let this
 * affecter query for the "latest" of the behavior and the rotate object, on-the-fly.
 * 
 * Perhaps make this interface large enough to include more affectors. 
 */
public class ConcreteCylinderPositioningEffector implements CylinderPositioningEffector {
	private CylinderPositioningEffectorTarget positioningTarget;
	private double mouseRotateFactor = 0.1;
	private boolean mouseRotateIsFrozen = false;
	private boolean mouseRotateIsAboutYOnly = false;
	
	public ConcreteCylinderPositioningEffector( 
			CylinderPositioningEffectorTarget positioningTarget
		) {
		this.positioningTarget = positioningTarget;
	}
	
	/** These effectors take their inputs and apply changes to the mouse-rotate behavior. */
	@Override
	public void setFrozenMouseRotator( boolean isFrozen ) {
		this.mouseRotateIsFrozen = isFrozen;
		setBehaviorCharacteristics();
	}
	@Override
	public void setMouseRotatorFactor( double factor ) {
		this.mouseRotateFactor = factor;
		setBehaviorCharacteristics();
	}
	@Override
	public void setYOnlyMouseRotator( boolean isYOnly ) {
		this.mouseRotateIsAboutYOnly = isYOnly;
		setBehaviorCharacteristics();
	}
	
	/** This affector sets the cylinder's position to whatever it was at the beginning. */
	@Override
	public void setDefaultCylinderPosition() {
		
		Platform.runLater(new Runnable() {
			public void run() {
				MouseDraggedHandler mouseDraggedHandler = positioningTarget.getMouseDraggedHandler();
				mouseDraggedHandler.setDefaultPosition();
			}
		});    			
		
	}
	
	/** Helper to ensure that all characteristics are applied consistently, w.r.t. rules of precedence. */
	private void setBehaviorCharacteristics() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// The rules of precedence are:
				//   if is-frozen, apply zero as factor, overriding member variable.
				//   if is-y-only, use zero for y-component.
				//   finally, use whatever current factor, if it has not been bypassed above.
				double finalModifierFactor = 0;
				if (mouseRotateIsFrozen) {
					finalModifierFactor = 0.0;
				}
				else {
					finalModifierFactor = mouseRotateFactor;
				}
				final MouseDraggedHandler mouseDraggedHandler = positioningTarget.getMouseDraggedHandler();
				mouseDraggedHandler.setModifierFactor(finalModifierFactor);
				mouseDraggedHandler.setUseYAngle(! mouseRotateIsAboutYOnly);
				
			}
		});    			

	}
}
