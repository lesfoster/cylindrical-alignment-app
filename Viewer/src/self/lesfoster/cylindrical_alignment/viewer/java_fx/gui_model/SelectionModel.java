/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Material;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Shape3D;

/**
 * Holds currently selected shape.
 * @author Leslie L Foster
 */
public class SelectionModel {
	public static final Material SELECTED_MATERIAL;
	static {
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(Color.GOLD);
		mat.setSpecularColor(Color.GOLDENROD);
		SELECTED_MATERIAL = mat;
	}
	
	private Shape3D selectedShape;
	private Material unselectedMaterialOfSelectedShape;

	/**
	 * @return the selectedShape
	 */
	public Shape3D getSelectedShape() {
		return selectedShape;
	}

	/**
	 * @param selectedShape the selectedShape to set
	 */
	public void setSelectedShape(Shape3D selectedShape) {
		this.selectedShape = selectedShape;
	}

	/**
	 * @return the unselectedMaterialOfSelectedShape
	 */
	public Material getUnselectedMaterialOfSelectedShape() {
		return unselectedMaterialOfSelectedShape;
	}

	/**
	 * @param unselectedMaterialOfSelectedShape the unselectedMaterialOfSelectedShape to set
	 */
	public void setUnselectedMaterialOfSelectedShape(Material unselectedMaterialOfSelectedShape) {
		this.unselectedMaterialOfSelectedShape = unselectedMaterialOfSelectedShape;
	}
}
