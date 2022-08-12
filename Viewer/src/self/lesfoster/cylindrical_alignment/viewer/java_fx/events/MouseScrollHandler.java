package self.lesfoster.cylindrical_alignment.viewer.java_fx.events;

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

import javafx.event.EventHandler;
import javafx.scene.input.ScrollEvent;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.CameraModel;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.MouseLocationModel;

/**
 * Handle scroll wheel by updating stored position and camera.
 * @author Leslie L Foster
 */
public class MouseScrollHandler implements EventHandler<ScrollEvent> {
    private CameraModel cameraModel;
    private double modifierFactor = 0.1;

    public MouseScrollHandler(
            MouseLocationModel mouseLocationModel,
            CameraModel cameraModel) {
        this.cameraModel = cameraModel;
    }

    @Override
    public void handle(ScrollEvent me) {
        repositionDisplay(me);
    }

    public double getModifierFactor() {
        return modifierFactor;
    }

    public void setModifierFactor(double modifierFactor) {
        this.modifierFactor = modifierFactor;
    }

    /** Eliminate all resources and allow this to gracefully go away. */
    public void close() {
        this.cameraModel = null;
    }

    private void repositionDisplay(ScrollEvent event) {
        double scrollDeltaY = event.getDeltaY();

        double z = cameraModel.getCamera().getTranslateZ();
        double modifier = 1.0;  // Placeholder?
        double newZ = z - scrollDeltaY * modifierFactor * modifier;
        cameraModel.getCamera().setTranslateZ(newZ);

    }

}