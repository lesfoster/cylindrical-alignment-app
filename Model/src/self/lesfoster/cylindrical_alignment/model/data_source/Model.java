/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
	private Collection<DataSourceListener> listeners = new ArrayList<>();

	private Model() {
	}
	private static Model instance = new Model();

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
