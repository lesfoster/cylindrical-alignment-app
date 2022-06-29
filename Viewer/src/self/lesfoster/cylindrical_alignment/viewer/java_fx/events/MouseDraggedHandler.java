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


package self.lesfoster.cylindrical_alignment.viewer.java_fx.events;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.CameraModel;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.MouseLocationModel;

/**
 * Handle mouse drag by updating stored position and camera.
 * @author Leslie L Foster
 */
public class MouseDraggedHandler implements EventHandler<MouseEvent> {
	private final MouseLocationModel mouseLocationModel;
	private final CameraModel cameraModel;
	private double modifierFactor = 0.1;
	private boolean useYAngle = true;
			
	public MouseDraggedHandler(
			MouseLocationModel mouseLocationModel, 
			CameraModel cameraModel) {
		this.mouseLocationModel = mouseLocationModel;
		this.cameraModel = cameraModel;
	}
	
	@Override
	public void handle(MouseEvent me) {
		// Save old pos.
		mouseLocationModel.setMouseOldX(mouseLocationModel.getMousePosX() );
		mouseLocationModel.setMouseOldY(mouseLocationModel.getMousePosY() );
	
		mouseLocationModel.setMousePosX( me.getSceneX() );
		mouseLocationModel.setMousePosY( me.getSceneY() );
		
		mouseLocationModel.setMouseDeltaX(mouseLocationModel.getMouseOldX() - mouseLocationModel.getMousePosX());
		mouseLocationModel.setMouseDeltaY(mouseLocationModel.getMouseOldY() - mouseLocationModel.getMousePosY());

		double modifier = 1.0;

		if (me.isControlDown()) {
			modifier = 0.1;
		}
		if (me.isShiftDown()) {
			modifier = 10.0;
		}
		if (me.isPrimaryButtonDown()) {
			cameraModel.getCameraXform().ry.setAngle(cameraModel.getCameraXform().ry.getAngle() - mouseLocationModel.getMouseDeltaX() * modifierFactor * modifier * 2.0);  // +
			if (useYAngle)
				cameraModel.getCameraXform().rx.setAngle(cameraModel.getCameraXform().rx.getAngle() + mouseLocationModel.getMouseDeltaY() * modifierFactor * modifier * 2.0);  // -
		} else if (me.isSecondaryButtonDown()) {
			double z = cameraModel.getCamera().getTranslateZ();
			double newZ = z + mouseLocationModel.getMouseDeltaX() * modifierFactor * modifier;
			cameraModel.getCamera().setTranslateZ(newZ);
		} else if (me.isMiddleButtonDown()) {			
			if (useYAngle)
				cameraModel.getCameraXform2().t.setX(cameraModel.getCameraXform2().t.getX() + mouseLocationModel.getMouseDeltaX() * modifierFactor * modifier * 0.3);  // -
			cameraModel.getCameraXform2().t.setY(cameraModel.getCameraXform2().t.getY() + mouseLocationModel.getMouseDeltaY() * modifierFactor * modifier * 0.3);  // -
		}

	}
	
	public double getModifierFactor() {
		return modifierFactor;
	}
	
	public void setModifierFactor(double modifierFactor) {
		this.modifierFactor = modifierFactor;
	}
	
	public void setDefaultPosition() {		
		cameraModel.getCameraXform().ry.setAngle(MouseLocationModel.DEFAULT_Y_ANGLE);
		cameraModel.getCameraXform().rx.setAngle(0.0);
	}
	
	public void setUseYAngle(boolean useYAngle) {
		this.useYAngle = useYAngle;
	}
}
