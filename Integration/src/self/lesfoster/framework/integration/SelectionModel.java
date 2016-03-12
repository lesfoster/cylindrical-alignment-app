/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.framework.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	public static final Color SELECTION_COLOR = Color.GOLD;
	public static final java.awt.Color SELECTION_COLOR_2D = new java.awt.Color(255,215,0);
	static {
		PhongMaterial mat = new PhongMaterial();
		mat.setDiffuseColor(SELECTION_COLOR);
		mat.setSpecularColor(Color.GOLDENROD);
		SELECTED_MATERIAL = mat;
	}
	
	private String selectedId;
	private Material unselectedMaterialOfSelectedShape;
	private Map<String,Object> idToObject = new HashMap<String,Object>();
	private List<SelectionModelListener> selectionListeners = new ArrayList<>();

	private static final SelectionModel selectionModel = new SelectionModel();
	public static SelectionModel getSelectionModel() {
		return selectionModel;
	}
	
	private SelectionModel() {}
	
	public void setIdToObject(String id, Object obj) {
		idToObject.put(id, obj);
	}
	
	public Object getObjectForId(String id) {
		return idToObject.get(id);
	}
	
	public void clear() {
		idToObject.clear();
	}
	
	/**
	 * Direct selection of id.
	 * @param id will be selected.
	 */
	public void setSelectedId(String id) {
		this.selectedId = id;
		fireEvent();
	}
	
	/**
	 * @return the selectedShape
	 */
	public String getSelectedId() {
		return selectedId;
	}

	/**
	 * @param selectedShape the selectedShape to set
	 */
	public void setSelectedShape(Shape3D selectedShape) {
		this.selectedId = selectedShape.getId();
		fireEvent();
	}
	
	public void setSelectedObject(Object selectedObject) {
		if (selectedObject instanceof Shape3D) {
			setSelectedShape((Shape3D) selectedObject);
		}
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
	/**
	 * Add a listener for cases where something in the model has been selected.
	 *
	 * @param listener what to tell when it happens.
	 */
	public synchronized void addListener(SelectionModelListener listener) {
		selectionListeners.add(listener);
	}

	public synchronized void removeListener(SelectionModelListener listener) {
		selectionListeners.remove(listener);
	}
	
	/**
	 * Notify all listeners
	 */
	private synchronized void fireEvent() {
		for (SelectionModelListener listener : selectionListeners) {
			listener.selected(selectedId);
		}
	}
}
