/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.java_fx.events;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.shape.Shape3D;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.CameraModel;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.MouseLocationModel;
import self.lesfoster.framework.integration.SelectionModel;

/**
 * Handle mouse drag by updating stored position and camera.
 * @author Leslie L Foster
 */
public class MouseDraggedHandler implements EventHandler<MouseEvent> {
	private final MouseLocationModel mouseLocationModel;
	private final SelectionModel selectionModel;
	private final CameraModel cameraModel;
	private double modifierFactor = 0.1;
	private boolean useYAngle = true;
	
	public MouseDraggedHandler(MouseLocationModel mouseLocationModel, SelectionModel selectionModel, CameraModel cameraModel) {
		this.mouseLocationModel = mouseLocationModel;
		this.selectionModel = selectionModel;
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

		// Select the shape visually.
		PickResult pr = me.getPickResult();
		if (pr != null) {
			Node node = pr.getIntersectedNode();
			if (node != null && node instanceof Shape3D) {
				if (selectionModel.getSelectedShape() != null) {
					selectionModel.getSelectedShape().setMaterial(selectionModel.getUnselectedMaterialOfSelectedShape());
				}
				Shape3D shape = (Shape3D) node;
				selectionModel.setUnselectedMaterialOfSelectedShape(shape.getMaterial());
				selectionModel.setSelectedShape(shape);
				selectionModel.getSelectedShape().setMaterial(SelectionModel.SELECTED_MATERIAL);
				System.out.println("Selected: " + node.getId());
			}
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
