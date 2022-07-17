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
import org.openide.util.NbPreferences;
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
