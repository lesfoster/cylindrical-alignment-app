/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import  org.openide.util.NbPreferences;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.DataSourceFactory;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;
import self.lesfoster.cylindrical_alignment.model.data_source.Model;

@ActionID(
		category = "File",
		id = "self.lesfoster.cylindrical_alignment.model.action.FileOpener"
)
@ActionRegistration(
		displayName = "#CTL_FileOpener"
)
@ActionReference(path = "Menu/File", position = 1300)
@Messages("CTL_FileOpener=Open")
public final class FileOpener implements ActionListener {
	
	public static final String DEFAULT_LOC_KEY = "JFileChooser.Current.Directory";

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		// Set starting point to most recently used starting directory.
		chooser.setCurrentDirectory(
			new File(NbPreferences.forModule(FileOpener.class).get(DEFAULT_LOC_KEY, System.getProperty("user.home")))
		);
		chooser.showOpenDialog(null);
		final File infile = chooser.getSelectedFile();
		if (infile != null) {
			String directoryStr = infile.getParent();
			// Save preferred starting directory.
			NbPreferences.forModule(FileOpener.class).put(DEFAULT_LOC_KEY, directoryStr);
			if (infile.canRead()) {
//				final DataSource dataSource = DataSourceFactory.getSourceForFile(infile.getAbsolutePath());
				HostBean hostBean = new HostBean();
				hostBean.setCurrentProtocol("http");
				hostBean.setCurrentPort(443);
				hostBean.setCurrentHost("jbosswildfly-cylalignvwr.rhcloud.com");
				hostBean.setCurrentUser("lesfoster");
				hostBean.setCurrentPass("lesfoster");
				final DataSource dataSource = DataSourceFactory.getSourceForServerId("2", hostBean);
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
						return infile.getAbsolutePath();
					}

				};
				Model container = Model.getInstance();
				container.setDataSource(model);
			}
		}
	}
}
