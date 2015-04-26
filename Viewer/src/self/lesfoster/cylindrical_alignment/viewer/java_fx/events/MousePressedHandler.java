/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.java_fx.events;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.gui_model.MouseLocationModel;

/**
 *
 * @author Leslie L Foster
 */
public class MousePressedHandler implements EventHandler<MouseEvent> {
	
	private final MouseLocationModel model;
	public MousePressedHandler(MouseLocationModel model) {
		this.model = model;
	}

	@Override
	public void handle(MouseEvent me) {
		model.setMousePosX( me.getSceneX() );
		model.setMousePosY( me.getSceneY() );
		model.setMouseOldX( me.getSceneX() );
		model.setMouseOldY( me.getSceneY() );
	}
	
}
