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
import javafx.scene.input.KeyEvent;
//import javafx.util.Duration;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.CameraModel;

/**
 *
 * @author Leslie L Foster
 */
public class KeyEventHandler implements EventHandler<KeyEvent> {

	private final static double DELTA_MULTIPLIER = 200.0;
    private final static double CONTROL_MULTIPLIER = 0.1;
    private final static double SHIFT_MULTIPLIER = 0.1;
    private final static double ALT_MULTIPLIER = 0.5;
	
	private CameraModel cameraModel;
	
	public KeyEventHandler(CameraModel cameraModel) {
		this.cameraModel = cameraModel;
	}
	
	@Override
	public void handle(KeyEvent event) {
//		Duration currentTime;
		switch (event.getCode()) {
			case Z:
				if (event.isShiftDown()) {
					cameraModel.getCameraXform().ry.setAngle(0.0);
					cameraModel.getCameraXform().rx.setAngle(0.0);
					cameraModel.getCamera().setTranslateZ(-300.0);
				}
				cameraModel.getCameraXform2().t.setX(0.0);
				cameraModel.getCameraXform2().t.setY(0.0);
				break;
//			case X:
//				if (event.isControlDown()) {
//					if (axisGroup.isVisible()) {
//						System.out.println("setVisible(false)");
//						axisGroup.setVisible(false);
//					} else {
//						System.out.println("setVisible(true)");
//						axisGroup.setVisible(true);
//					}
//				}
//				break;
//			case S:
//				if (event.isControlDown()) {
//					if (moleculeGroup.isVisible()) {
//						moleculeGroup.setVisible(false);
//					} else {
//						moleculeGroup.setVisible(true);
//					}
//				}
//				break;
//			case SPACE:
//				if (timelinePlaying) {
//					timeline.pause();
//					timelinePlaying = false;
//				} else {
//					timeline.play();
//					timelinePlaying = true;
//				}
//				break;
			case UP:
				if (event.isControlDown() && event.isShiftDown()) {
					cameraModel.getCameraXform2().t.setY(cameraModel.getCameraXform2().t.getY() - 10.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown() && event.isShiftDown()) {
					cameraModel.getCameraXform().rx.setAngle(cameraModel.getCameraXform().rx.getAngle() - 10.0 * ALT_MULTIPLIER);
				} else if (event.isControlDown()) {
					cameraModel.getCameraXform2().t.setY(cameraModel.getCameraXform2().t.getY() - 1.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown()) {
					cameraModel.getCameraXform().rx.setAngle(cameraModel.getCameraXform().rx.getAngle() - 2.0 * ALT_MULTIPLIER);
				} else if (event.isShiftDown()) {
					double z = cameraModel.getCamera().getTranslateZ();
					double newZ = z + 5.0 * SHIFT_MULTIPLIER;
					cameraModel.getCamera().setTranslateZ(newZ);
				}
				break;
			case DOWN:
				if (event.isControlDown() && event.isShiftDown()) {
					cameraModel.getCameraXform2().t.setY(cameraModel.getCameraXform2().t.getY() + 10.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown() && event.isShiftDown()) {
					cameraModel.getCameraXform().rx.setAngle(cameraModel.getCameraXform().rx.getAngle() + 10.0 * ALT_MULTIPLIER);
				} else if (event.isControlDown()) {
					cameraModel.getCameraXform2().t.setY(cameraModel.getCameraXform2().t.getY() + 1.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown()) {
					cameraModel.getCameraXform().rx.setAngle(cameraModel.getCameraXform().rx.getAngle() + 2.0 * ALT_MULTIPLIER);
				} else if (event.isShiftDown()) {
					double z = cameraModel.getCamera().getTranslateZ();
					double newZ = z - 5.0 * SHIFT_MULTIPLIER;
					cameraModel.getCamera().setTranslateZ(newZ);
				}
				break;
			case RIGHT:
				if (event.isControlDown() && event.isShiftDown()) {
					cameraModel.getCameraXform2().t.setX(cameraModel.getCameraXform2().t.getX() + 10.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown() && event.isShiftDown()) {
					cameraModel.getCameraXform().ry.setAngle(cameraModel.getCameraXform().ry.getAngle() - 10.0 * ALT_MULTIPLIER);
				} else if (event.isControlDown()) {
					cameraModel.getCameraXform2().t.setX(cameraModel.getCameraXform2().t.getX() + 1.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown()) {
					cameraModel.getCameraXform().ry.setAngle(cameraModel.getCameraXform().ry.getAngle() - 2.0 * ALT_MULTIPLIER);
				}
				break;
			case LEFT:
				if (event.isControlDown() && event.isShiftDown()) {
					cameraModel.getCameraXform2().t.setX(cameraModel.getCameraXform2().t.getX() - 10.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown() && event.isShiftDown()) {
					cameraModel.getCameraXform().ry.setAngle(cameraModel.getCameraXform().ry.getAngle() + 10.0 * ALT_MULTIPLIER);  // -
				} else if (event.isControlDown()) {
					cameraModel.getCameraXform2().t.setX(cameraModel.getCameraXform2().t.getX() - 1.0 * CONTROL_MULTIPLIER);
				} else if (event.isAltDown()) {
					cameraModel.getCameraXform().ry.setAngle(cameraModel.getCameraXform().ry.getAngle() + 2.0 * ALT_MULTIPLIER);  // -
				}
				break;
		}

	}
	
}
