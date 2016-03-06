/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.viewer.top_component;

import java.util.ArrayList;
import java.util.Collection;
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
	
	public void addListener(EffectorContainerListener ecl) {
		if (effected != null) {
			ecl.setEffected(effected);
		}
		listeners.add(ecl);
	}
	
	public void close() {
		listeners.clear();
	}
	
	public void setEffected(Effected effected) {
		this.effected = effected;
		fireEffectedEvent();
	}

	public static interface EffectorContainerListener {
		void setEffected(Effected effected);
	}

	private void fireEffectedEvent() {
		for (EffectorContainerListener listener : listeners) {
			listener.setEffected(effected);
		}
	}
}
