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
package self.lesfoster.framework.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.paint.Material;
import javafx.scene.shape.Shape3D;

/**
 * Holds currently selected shape.
 *
 * @author Leslie L Foster
 */
public class SelectionModel {

    private String selectedId;
    private Material unselectedMaterialOfSelectedShape;
    private final Map<String, Object> idToObject = new HashMap<>();
    private final List<SelectionModelListener> selectionListeners = new ArrayList<>();

    private static final SelectionModel selectionModel = new SelectionModel();

    public static SelectionModel getSelectionModel() {
        return selectionModel;
    }

    private SelectionModel() {
    }

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
     *
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
     * @param unselectedMaterialOfSelectedShape the
     * unselectedMaterialOfSelectedShape to set
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
        selectionListeners.forEach(l -> l.selected(selectedId));
    }
}
