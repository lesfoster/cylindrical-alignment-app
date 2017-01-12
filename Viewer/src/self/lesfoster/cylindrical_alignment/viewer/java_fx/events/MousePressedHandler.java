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
