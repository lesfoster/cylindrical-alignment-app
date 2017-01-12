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
