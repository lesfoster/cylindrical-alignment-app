/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.model.lifecycle;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import org.openide.modules.OnStart;
import java.util.logging.Logger;
import self.lesfoster.cylindrical_alignment.data_source.DataSource;
import self.lesfoster.cylindrical_alignment.data_source.DataSourceFactory;
import self.lesfoster.cylindrical_alignment.data_source.Entity;
import self.lesfoster.cylindrical_alignment.model.data_source.Model;

/**
 * This is fired at module startup.
 *
 * @author Leslie L Foster
 */
@OnStart
public class StartupHook implements Runnable {
	private static final Logger logger = Logger.getLogger(StartupHook.class.getName());
	
	public void run() {
		try {
			DataSource ds = getInitialDataSource();
			Model.getInstance().setDataSource(ds);
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Failed to run startup hook.");
			ex.printStackTrace();
		}
	}
	
	private DataSource getInitialDataSource() {
		String filePrefix = "C:\\current_projects\\gitfiles\\CylindricalAlignmentApp\\samples";
		//String filePath = filePrefix + "\\pyrimidinergic_receptor\\gi66932905.BlastOutput.xml";
		//String filePath = filePrefix + "\\h4_muscle_histones\\h4_muscle.cyl.xml";
		//String filePath = filePrefix + "\\gene_machine\\gene_machine_2.cyl.xml";
		//String filePath = filePrefix + "\\gff3\\chromosome_3F.gff";
		String dosFilePath = filePrefix + "\\zebra_mussel_byssal\\AF265353_1.cyl.xml";
		String unixFilePath = "~/zebra_mussel_byssal/AF265353_1.cyl.xml";
		final String filePath
				= (System.getProperty("os.name").toLowerCase().contains("windows"))
						? dosFilePath
						: unixFilePath;

		if (!new File(filePath).exists()) {
			throw new IllegalArgumentException(filePath + " does not exist.");
		}
		final DataSource dataSource = DataSourceFactory.getSourceForFile(filePath);
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
}
