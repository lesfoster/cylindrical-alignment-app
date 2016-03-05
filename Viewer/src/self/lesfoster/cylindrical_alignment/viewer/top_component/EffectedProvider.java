/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.top_component;

import org.openide.util.lookup.ServiceProvider;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;
import self.lesfoster.cylindrical_alignment.effector.Effected;
import self.lesfoster.cylindrical_alignment.effector.Effector;
import self.lesfoster.cylindrical_alignment.viewer.java_fx.CylinderContainer;

/**
 * This provides the lookup for the CylinderContainer/Effected object.
 *
 * @author Leslie L Foster
 * @deprecated this did not solve the problem.
 */
@ServiceProvider(service=Effected.class)
public class EffectedProvider implements Effected {
	public EffectedProvider() {
		
	}
	
	@Override
	public Effector[] getEffectors() {
		Effected eff = getCylinderContainer();
		if (eff == null) {
			return null;
		} else {
			return eff.getEffectors();
		}
	}

	private CylinderContainer getCylinderContainer() {
		CylinderContainer rtnVal = null;
		TopComponent viewer = WindowManager.getDefault().findTopComponent(ViewerTopComponent.PREFERRED_ID);		
		if (viewer != null) {
			rtnVal = ((ViewerTopComponent)viewer).getContainer();
		}
		System.out.println("Provider returning " + rtnVal);
		return rtnVal;
	}

}
