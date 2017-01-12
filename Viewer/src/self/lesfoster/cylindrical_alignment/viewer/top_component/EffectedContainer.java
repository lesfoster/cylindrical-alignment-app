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


package self.lesfoster.cylindrical_alignment.viewer.top_component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import self.lesfoster.cylindrical_alignment.effector.Effected;

/**
 * This is a container/switching yard for an 'Effected' object, upon which
 * effectors may act.
 *
 * @author Leslie L Foster
 */
public class EffectedContainer {
	private Collection<EffectorContainerListener> listeners = new ArrayList<>();
	private EffectedContainer() {}
	private static EffectedContainer instance = new EffectedContainer();

	private Effected effected;
	
	public static EffectedContainer getInstance() {
		return instance;
	}
	
	public synchronized void addListener(EffectorContainerListener ecl) {
		if (effected != null) {
			ecl.setEffected(effected);
		}
		listeners.add(ecl);
	}
	
	public synchronized void removeListener(EffectorContainerListener ecl) {
		listeners.remove(ecl);
	}
	
	public synchronized void close() {
		listeners.clear();
	}
	
	public void setEffected(Effected effected) {
		this.effected = effected;
		fireEffectedEvent();
	}

	private synchronized void fireEffectedEvent() {
		List<EffectorContainerListener> safeList = new ArrayList<>();
		safeList.addAll(listeners);
		for (EffectorContainerListener listener : safeList) {
			listener.setEffected(effected);
		}
	}

	public static interface EffectorContainerListener {

		void setEffected(Effected effected);
	}

}
