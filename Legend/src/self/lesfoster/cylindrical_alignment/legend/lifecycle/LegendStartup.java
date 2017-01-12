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
