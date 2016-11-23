/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.effector;

import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.WindowManager;
import self.lesfoster.cylindrical_alignment.viewer.top_component.ViewerTopComponent;

/**
 * Service provider to cough up an effected object on demand.
 *
 * @author Leslie L Foster
 */
@ServiceProvider(service=Effected.class)
public class EffectedProvider implements Effected {
	
	public EffectedProvider() {}

	@Override
	public Effector[] getEffectors() {
		ViewerTopComponent vtc = (ViewerTopComponent)
				WindowManager.getDefault()
						.findTopComponent(ViewerTopComponent.PREFERRED_ID);
		if (vtc == null) {
			return null;
		}
		else {
			return vtc.getContainer().getEffectors();
		}
	}
	
}
