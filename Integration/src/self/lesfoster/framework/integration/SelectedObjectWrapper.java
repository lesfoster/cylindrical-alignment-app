/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.framework.integration;

/**
 * Holds the selected object.
 *
 * @author Leslie L Foster
 */
public class SelectedObjectWrapper {
	private Object selectedObject;

	/**
	 * @return the selectedObject
	 */
	public Object getSelectedObject() {
		return selectedObject;
	}

	/**
	 * @param selectedObject the selectedObject to set
	 */
	public SelectedObjectWrapper setSelectedObject(Object selectedObject) {
		this.selectedObject = selectedObject;
		return this;
	}
}
