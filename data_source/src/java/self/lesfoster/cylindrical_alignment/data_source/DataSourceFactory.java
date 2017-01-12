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



/*
 * Data Source Factory
 * Created on Mar 1, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import self.lesfoster.cylindrical_alignment.viewer.statistics.RemotePingCounter;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;

/**
 * Creates data sources appropriate to filename or URL.
 * @author FosterLL
 */
public class DataSourceFactory {
	private static final Logger log = Logger.getLogger(DataSourceFactory.class.getName());
	
    public static DataSource getSourceForFile(String filename) {
		if (filename.endsWith(".Project.xml")) {
            new RemotePingCounter().registerUsage( "ProjectXMLOpen" );

    		Converter converter = GeneralXsltConverter.getConverter(filename);
    		if (converter != null) {
    			return new JAXBDataSource(new ProjectPostProcessor(converter.getStream()).getStream());
    		}
    		return null;
		}
    	else if (filename.endsWith(".xml")) {
    		Converter converter = GeneralXsltConverter.getConverter(filename);
    		if (converter != null) {
                new RemotePingCounter().registerUsage( "JAXB1Open" );
    			return new JAXBDataSource(converter.getStream());
    		}
    		else {
    			try {
                    new RemotePingCounter().registerUsage( "JAXB2Open" );
    		        return new JAXBDataSource(filename);
    		    } catch (IOException ioe) {
    		    	log.severe(filename + " Failed to unmarshal");
    		    	log.severe(ioe.getMessage());
    		    	return null;
    		    }
    		}
    		//return new CylinderXmlParser(filename);
    	}
		else if (filename.endsWith(".clustal.txt")) {
            new RemotePingCounter().registerUsage( "ClustalOpen" );
			return new JAXBDataSource(new ClustalWConverter(filename).getStream());
		}
		else if (filename.endsWith(".gff")) {
            new RemotePingCounter().registerUsage( "GFFOpen" );
			return new Gff3DataSource(filename);
		}
    	else {
            new RemotePingCounter().registerUsage( "UnknownSource" );
    		return null;
    	}
    }
	
	/**
	 * Returns a data source that can pull data from remote server.
	 * 
	 * @param id identifier from server, that is unique.
	 * @param hostBean info for finding host and filling out URL.
	 * @return a Data Source that can deliver the goods on the ID.
	 */
	public static DataSource getSourceForServerId(String id, HostBean hostBean) {
		PrecomputedBlastXmlDataSource dataSource = new PrecomputedBlastXmlDataSource();
		dataSource.setId(id);
		dataSource.setHostBean(hostBean);
		return dataSource;
	}
}
