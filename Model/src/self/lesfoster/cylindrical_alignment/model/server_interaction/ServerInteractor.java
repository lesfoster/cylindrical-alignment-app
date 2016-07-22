/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.server_interaction;

import java.util.logging.Logger;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.swing.SwingWorker;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.DataSourceFactory;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.data_source.PrecomputedBlastXmlDataSource;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;
import self.lesfoster.cylindrical_alignment.model.data_source.Model;
import self.lesfoster.cylindrical_alignment.data_source.SearchResult;

/**
 *
 * @author Leslie L Foster
 */
public class ServerInteractor {
	private HostBean hostBean;
	private Logger logger = Logger.getLogger(ServerInteractor.class.getSimpleName());
	
	public ServerInteractor() {
		hostBean = new HostBean();
		hostBean.setCurrentProtocol("https");
		hostBean.setCurrentPort(443);
		hostBean.setCurrentHost("jbosswildfly-cylalignvwr.rhcloud.com");
		hostBean.setCurrentUser("lesfoster");
		hostBean.setCurrentPass("lesfoster");
	}
	
	public List<SearchResult> find(LocalDate localDate) throws Exception {
		if (localDate == null) {
			return Collections.EMPTY_LIST;
		}
		PrecomputedBlastXmlDataSource dataSource = new PrecomputedBlastXmlDataSource();
		dataSource.setHostBean(hostBean);
		// Call the finder for the stuff.
		// NOTE: must roll back month by one day to compensate for
		//   more sensible "Local Date" versus old "Calendar Date".
		//   LocalDate month is 1-based, Calendar is 0-based month.
		List<SearchResult> rtnVal = dataSource.getDateSearchResults(
				localDate.getYear(), localDate.getMonthValue() - 1, localDate.getDayOfMonth()
		);
		return rtnVal;
	}
	
	public List<SearchResult> findBySpecies(String species) throws Exception {
		PrecomputedBlastXmlDataSource dataSource = new PrecomputedBlastXmlDataSource();
		dataSource.setHostBean(hostBean);
		// Call the finder for the stuff.
		List<SearchResult> rtnVal = dataSource.getBySingleStringSearchResults(
				PrecomputedBlastXmlDataSource.StringSearchType.SPECIES, species
		);
		return rtnVal;
	}
	
	public void fetch(final String id) {
		final DataSource dataSource = DataSourceFactory.getSourceForServerId(id, hostBean);
		DataSource descriptiveDataSource = new DataSource() {

			@Override
			public List<Entity> getEntities() {
				return dataSource.getEntities();
			}

			@Override
			public int getAnchorLength() {
				return dataSource.getAnchorLength();
			}

			@Override
			public String toString() {
				return "Remote Document " + id;
			}

		};
		Model model = Model.getInstance();
		SwingWorker swingWorker = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				descriptiveDataSource.getAnchorLength(); // Eager initialization.
				return Boolean.TRUE;
			}

			@Override
			protected void done() {
				model.setDataSource(descriptiveDataSource);
			}

		};
		swingWorker.execute();
	}
	
}
