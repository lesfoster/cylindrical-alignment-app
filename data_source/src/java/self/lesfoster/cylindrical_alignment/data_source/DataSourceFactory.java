/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments are arranged like the staves
of a barrel. The cylinder can spin, and if it spins, it can
do so at two different speeds.  When stopped, properties can
be seen for the aligned values.
Copyright (C) 2005 Leslie L. Foster

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

/*
 * Data Source Factory
 * Created on Mar 1, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import self.lesfoster.cylindrical_alignment.viewer.statistics.RemotePingCounter;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * Creates data sources based on hints gathered from the file name.
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
}
