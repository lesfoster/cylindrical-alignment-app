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
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.DataSourceFactory;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser chooser = new JFileChooser();
		final File infile = chooser.getSelectedFile();
		if (infile != null) {
			if (infile.canRead()) {
				final DataSource dataSource = DataSourceFactory.getSourceForFile(infile.getAbsolutePath());
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
