/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
