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
package self.lesfoster.cylindrical_alignment.model.data_source;

import java.util.ArrayList;
import java.util.Collection;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;

/**
 * This is where the data source is stored for safe keeping.
 *
 * @author Leslie L Foster
 */
public class Model {

    private final Collection<DataSourceListener> listeners = new ArrayList<>();

    private Model() {
    }
    private static final Model instance = new Model();

    private DataSource dataSource;

    public static Model getInstance() {
        return instance;
    }

    public synchronized void addListener(DataSourceListener dsl) {
        if (dataSource != null) {
            dsl.setDataSource(dataSource);
        }
        listeners.add(dsl);
    }

    public synchronized void close() {
        listeners.clear();
    }

    public void setDataSource(DataSource effected) {
        this.dataSource = effected;
        fireDataSourceEvent();
    }
    
    public static interface DataSourceListener {

        void setDataSource(DataSource effected);
    }

    private synchronized void fireDataSourceEvent() {
        for (DataSourceListener listener : listeners) {
            listener.setDataSource(dataSource);
        }
    }
}
