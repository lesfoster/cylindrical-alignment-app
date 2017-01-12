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


package self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model;

import javafx.scene.PerspectiveCamera;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.group.TransformableGroup;

/**
 * Holds camera-related things.
 * @author Leslie L Foster
 */
public class CameraModel {
	private PerspectiveCamera camera = new PerspectiveCamera(true);	
	private TransformableGroup cameraXform = new TransformableGroup();
	private TransformableGroup cameraXform2 = new TransformableGroup();
	private TransformableGroup cameraXform3 = new TransformableGroup();

	public CameraModel() {
	}
	
	/**
	 * @return the camera
	 */
	public PerspectiveCamera getCamera() {
		return camera;
	}

	/**
	 * @param camera the camera to set
	 */
	public void setCamera(PerspectiveCamera camera) {
		this.camera = camera;
	}

	/**
	 * @return the cameraXform
	 */
	public TransformableGroup getCameraXform() {
		return cameraXform;
	}

	/**
	 * @param cameraXform the cameraXform to set
	 */
	public void setCameraXform(TransformableGroup cameraXform) {
		this.cameraXform = cameraXform;
	}

	/**
	 * Optional.  May be omitted. Please check for null.
	 * @return the cameraXform2
	 */
	public TransformableGroup getCameraXform2() {
		return cameraXform2;
	}

	/**
	 * Optional.  May be omitted. Please check for null.
	 * @param cameraXform2 the cameraXform2 to set
	 */
	public void setCameraXform2(TransformableGroup cameraXform2) {
		this.cameraXform2 = cameraXform2;
	}

	/**
	 * Optional.  May be omitted. Please check for null.
	 * @return the cameraXform3
	 */
	public TransformableGroup getCameraXform3() {
		return cameraXform3;
	}

	/**
	 * Optional.  May be omitted. Please check for null.
	 * @param cameraXform3 the cameraXform3 to set
	 */
	public void setCameraXform3(TransformableGroup cameraXform3) {
		this.cameraXform3 = cameraXform3;
	}
}
