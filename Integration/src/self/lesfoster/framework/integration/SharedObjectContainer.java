/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.framework.integration;

/**
 *
 * @author Leslie L Foster
 */
import java.util.ArrayList;
import java.util.Collection;

/**
 * This is where the Legend Model is stored for safe keeping.
 *
 * @author Leslie L Foster
 */
public class SharedObjectContainer<T> {
	private Collection<ContainerListener> listeners = new ArrayList<>();
	private T value;
	
	public synchronized void addListener(ContainerListener listener) {
		if (value != null) {
			listener.setValue(value);
		}
		listeners.add(listener);
	}

	public synchronized void close() {
		listeners.clear();
	}

	public void setValue(T value) {
		this.value = value;
		fireContainerEvent();
	}

	public static interface ContainerListener<T> {

		void setValue(T value);
	}

	private synchronized void fireContainerEvent() {
		for (ContainerListener listener : listeners) {
			listener.setValue(value);
		}
	}
}

