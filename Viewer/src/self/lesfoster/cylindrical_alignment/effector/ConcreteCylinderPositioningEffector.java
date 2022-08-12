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

    private final CylinderPositioningEffectorTarget positioningTarget;
    private double mouseRotateFactor = 0.1;
    private boolean mouseRotateIsFrozen = false;
    private boolean mouseRotateIsAboutYOnly = false;

    public ConcreteCylinderPositioningEffector(
            CylinderPositioningEffectorTarget positioningTarget
    ) {
        this.positioningTarget = positioningTarget;
    }

    /**
     * These effectors take their inputs and apply changes to the mouse-rotate
     * behavior.
     */
    @Override
    public void setFrozenMouseRotator(boolean isFrozen) {
        this.mouseRotateIsFrozen = isFrozen;
        setBehaviorCharacteristics();
    }

    @Override
    public void setMouseRotatorFactor(double factor) {
        this.mouseRotateFactor = factor;
        setBehaviorCharacteristics();
    }

    @Override
    public void setYOnlyMouseRotator(boolean isYOnly) {
        this.mouseRotateIsAboutYOnly = isYOnly;
        setBehaviorCharacteristics();
    }

    /**
     * This affector sets the cylinder's position to whatever it was at the
     * beginning.
     */
    @Override
    public void setDefaultCylinderPosition() {

        Platform.runLater(() -> {
            MouseDraggedHandler mouseDraggedHandler = positioningTarget.getMouseDraggedHandler();
            mouseDraggedHandler.setDefaultPosition();
        });

    }

    /**
     * Helper to ensure that all characteristics are applied consistently,
     * w.r.t. rules of precedence.
     */
    private void setBehaviorCharacteristics() {
        SwingUtilities.invokeLater(() -> {
            // The rules of precedence are:
            //   if is-frozen, apply zero as factor, overriding member variable.
            //   if is-y-only, use zero for y-component.
            //   finally, use whatever current factor, if it has not been bypassed above.
            double finalModifierFactor = 0;
            if (mouseRotateIsFrozen) {
                finalModifierFactor = 0.0;
            } else {
                finalModifierFactor = mouseRotateFactor;
            }
            final MouseDraggedHandler mouseDraggedHandler = positioningTarget.getMouseDraggedHandler();
            mouseDraggedHandler.setModifierFactor(finalModifierFactor);
            mouseDraggedHandler.setUseYAngle(!mouseRotateIsAboutYOnly);
        });

    }
}
