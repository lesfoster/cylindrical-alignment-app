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
package self.lesfoster.cylindrical_alignment.model.lifecycle;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import org.openide.modules.OnStart;
import java.util.logging.Logger;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.DataSourceFactory;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.data_source.JAXBDataSource;
import self.lesfoster.cylindrical_alignment.model.data_source.Model;

/**
 * This is fired at module startup.
 *
 * @author Leslie L Foster
 */
@OnStart
public class StartupHook implements Runnable {

    private static final String SAMPLES_WIN_PREFIX = "C:\\current_projects\\gitfiles\\CylindricalAlignmentApp\\";
    private static final String LAST_PREFIX = "samples";
    private static final String WIN_BYSSAL_PREFIX = "\\zebra_mussel_byssal\\";
    private static final String STARNIX_BYSSAL_PREFIX = "~/zebra_mussel_byssal/";
    private static final String XML_FILENAME = "AF265353_1.cyl.xml";
    private static final Logger logger = Logger.getLogger(StartupHook.class.getName());

    public void run() {
        try {
            System.setProperty("netbeans.buildnumber", "1.0.1");
            DataSource ds = getInitialDataSource();
            Model.getInstance().setDataSource(ds);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Failed to run startup hook.");
            ex.printStackTrace();
        }
    }

    private DataSource getInitialDataSource() {
        String filePrefix = SAMPLES_WIN_PREFIX + LAST_PREFIX;
        //String filePath = filePrefix + "\\pyrimidinergic_receptor\\gi66932905.BlastOutput.xml";
        //String filePath = filePrefix + "\\h4_muscle_histones\\h4_muscle.cyl.xml";
        //String filePath = filePrefix + "\\gene_machine\\gene_machine_2.cyl.xml";
        //String filePath = filePrefix + "\\gff3\\chromosome_3F.gff";
        String dosFilePath = filePrefix + WIN_BYSSAL_PREFIX + XML_FILENAME;
        String unixFilePath = STARNIX_BYSSAL_PREFIX + XML_FILENAME;
        final String filePath
                = (System.getProperty("os.name").toLowerCase().contains("windows"))
                ? dosFilePath
                : unixFilePath;

        InputStream inputStream = null;
        if (!new File(filePath).exists()) {
            // Try pulling the file in via classpath.
            inputStream = this.getClass().getClassLoader().getResourceAsStream(LAST_PREFIX + "/" + XML_FILENAME);
            if (inputStream == null) {
                throw new IllegalArgumentException(filePath + " does not exist.");
            }
        }
        final DataSource dataSource = getSelectedDataSource(inputStream, filePath);
        DataSource descriptiveDataSourceWrapper = new DataSource() {

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
                return filePath;
            }

        };
        return descriptiveDataSourceWrapper;
    }

    private DataSource getSelectedDataSource(InputStream inputStream, String filePath) {
        if (inputStream != null) {
            logger.info("Pulling data from input stream.");
            return new JAXBDataSource(inputStream);
        } else {
            logger.info(() -> "Pulling data from file " + filePath);
            return DataSourceFactory.getSourceForFile(filePath);
        }
    }
}
