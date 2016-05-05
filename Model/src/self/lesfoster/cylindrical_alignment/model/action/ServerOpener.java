/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.DataSourceFactory;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;
import self.lesfoster.cylindrical_alignment.model.data_source.Model;

@ActionID(
		category = "File",
		id = "self.lesfoster.cylindrical_alignment.model.action.ServerOpener"
)
@ActionRegistration(
		displayName = "#CTL_ServerOpener"
)
@ActionReference(path = "Menu/File", position = 1550)
@Messages("CTL_ServerOpener=Fetch from Server")
public final class ServerOpener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent e) {
		HostBean hostBean = new HostBean();
		hostBean.setCurrentProtocol("https");
		hostBean.setCurrentPort(443);
		hostBean.setCurrentHost("jbosswildfly-cylalignvwr.rhcloud.com");
		hostBean.setCurrentUser("lesfoster");
		hostBean.setCurrentPass("lesfoster");
		final String id = "2";
		final DataSource dataSource = DataSourceFactory.getSourceForServerId(id, hostBean);
		DataSource model = new DataSource() {

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
		Model container = Model.getInstance();
		container.setDataSource(model);
	}
}
