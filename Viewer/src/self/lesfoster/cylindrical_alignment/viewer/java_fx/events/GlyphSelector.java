/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.java_fx.events;

import java.util.HashMap;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.shape.Shape3D;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;
import self.lesfoster.framework.integration.SelectionModel;

/**
 * Code to select one colored glyph and unselect any existing one.  This
 * selection is all about the visual.  It does not change the selection model.
 *
 * @author Leslie L Foster
 */
public class GlyphSelector {
	private final SelectionModel selectionModel;
	private final Map<String,SubEntity> idToSubEntity;
	private final Map<String,Node> idToShape;
	private final Map<SubEntity,Node> subEntityToNode;
	
	public GlyphSelector(SelectionModel selectionModel, Map<String,Node> idToShape, Map<String,SubEntity> idToSubEntity) {
		this.selectionModel = selectionModel;
		this.idToShape = idToShape;
		this.idToSubEntity = idToSubEntity;
		subEntityToNode = new HashMap<>();
		idToSubEntity.keySet().stream().forEach((id) -> {
			subEntityToNode.put(idToSubEntity.get(id), idToShape.get(id));
		});
	}
	
	/** Selection for the caller who knows sub-entities. */
	public void select(SubEntity subEntity) {
		Runnable runnable = () -> {
			Node node = subEntityToNode.get(subEntity);
			if (node != null) {
				select(node);
			}
		};
		if (Platform.isFxApplicationThread()) {
			runnable.run();
		}
		else {
			Platform.runLater(runnable);
		}
	}
	
	/** Selection for the caller who knows nodes. */
	public void select(Node node) {
		if (selectionModel.getSelectedId() != null) {
			Shape3D selectedShape = (Shape3D) idToShape.get(selectionModel.getSelectedId());
			if (selectedShape != null) {
				selectedShape.setMaterial(selectionModel.getUnselectedMaterialOfSelectedShape());
			}
		}
		Shape3D shape = (Shape3D) node;
		selectionModel.setUnselectedMaterialOfSelectedShape(shape.getMaterial());
		selectionModel.setSelectedShape(shape);
		shape.setMaterial(SelectionModel.SELECTED_MATERIAL);
		//((Shape3D)idToShape.get(selectionModel.getSelectedId())).setMaterial(SelectionModel.SELECTED_MATERIAL);
		System.out.println("Selected: " + node.getId());
		
	}
	
}
