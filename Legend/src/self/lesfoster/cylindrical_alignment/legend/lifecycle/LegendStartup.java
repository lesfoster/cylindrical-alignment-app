/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.legend.lifecycle;

import org.openide.modules.OnStart;
import self.lesfoster.cylindrical_alignment.legend.widget.ConcreteLegendModel;
import self.lesfoster.framework.integration.LegendModelContainer;
import self.lesfoster.framework.integration.LegendModel;

/**
 * Take care of legend setup, at startup.
 *
 * @author Leslie L Foster
 */
@OnStart
public class LegendStartup implements Runnable {
	@Override
	public void run() {
		try {
			LegendModel legendModel = new ConcreteLegendModel();
			LegendModelContainer container = LegendModelContainer.getInstance();
			container.setValue(legendModel);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
