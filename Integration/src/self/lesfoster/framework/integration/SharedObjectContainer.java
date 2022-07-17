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

    private final Collection<ContainerListener> listeners = new ArrayList<>();
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
