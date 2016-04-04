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
 * Cylinder XML Parser
 * Created on July 23, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.logging.Logger;
import java.util.*;
import java.io.*;
import javax.xml.bind.*;
import cyl.jaxb.*;

/**
 * Parses the Cylinder XML format into Entity objects, using
 * JAXB-generated classes.
 * 
 * @author Leslie L. Foster
 */
public class JAXBDataSource implements DataSource {

	private static final int CUTOFF_NUMBER = 100;  // over 100 entities kills the cylinder.
	private static final Logger log = Logger.getLogger(JAXBDataSource.class.getName());

    private List<Entity> entities = new ArrayList<Entity>();
    private int anchorLength = -1;

    /**
     * Parses a stream of incoming XML to use as a cylinder viewer.
     * 
     * @param inputStream incoming info.
     */
    public JAXBDataSource(InputStream inputStream) {
		try {
			// NOTE: if the classloader ref is omitted, the ObjectFactory
			// is never discovered, and the newInstance will fail.
			log.finest("Getting jaxb context.");
			JAXBContext context = JAXBContext.newInstance("cyl.jaxb", Cylinder.class.getClassLoader());
			log.finest("Got jaxb context of " + context);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
			log.finest("About to unmarshal with " + unmarshaller);
			Cylinder cylinder = (Cylinder)unmarshaller.unmarshal(inputStream);
			log.finest("Completed unmarshaling.");

			anchorLength = cylinder.getAnchorLength();

			// Look at all entities
			List jaxbEntityList = cylinder.getEntity();
			for (int i = 0; i < jaxbEntityList.size(); i++) {
				Cylinder.Entity jaxbEntity = (Cylinder.Entity)jaxbEntityList.get(i);
                List<SubEntity> subEntities = new ArrayList<SubEntity>();
                Entity entity = new ConcreteEntity(subEntities);

                // Look at all sub-entities.
                List jaxbSubEntityList = jaxbEntity.getSubentity();
                for (int j = 0; j < jaxbSubEntityList.size(); j++) {
                	Cylinder.Entity.Subentity jaxbSubEntity =
                		(Cylinder.Entity.Subentity)jaxbSubEntityList.get(j);

					int qStart = jaxbSubEntity.getQstart();
					int qEnd = jaxbSubEntity.getQend();
					int sStart = jaxbSubEntity.getSstart();
					int sEnd = jaxbSubEntity.getSend();
					int strand = Integer.parseInt(jaxbSubEntity.getStrand());
					int priority = jaxbSubEntity.getPriority();

					// Look at all properties
                	List jaxbProperties = jaxbSubEntity.getP();
                	Map<String, Object> properties = new HashMap<String, Object>();
                	for (int k = 0; k < jaxbProperties.size(); k++) {
                		Cylinder.Entity.Subentity.P property =
                			(Cylinder.Entity.Subentity.P)jaxbProperties.get(k);
                		properties.put(property.getName(), property.getValue());
                	}

					// Add props deducible from the entity.
				    properties.put(QUERY_RANGE, qStart + ".." + qEnd);
				    properties.put(SUBJECT_RANGE, sStart + ".." + sEnd);

				    if (!properties.containsKey(DataSource.URL_LINK_PROPERTY_NAME)) {
				    	String id = (String)properties.get(ACCESSION_PROPERTY_NAME);
				    	String type = (String)properties.get(SUB_ENTITY_TYPE_PROPERTY_NAME);
				    	if (id != null) {
				    		if (type.equals(DataSource.ENTITY_TYPE_BLAST_N_SUB_HIT))
				    	        properties.put(DataSource.URL_LINK_PROPERTY_NAME,
				    			        "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=nucleotide&val="
				    			        + id);
				    		else
				    	        properties.put(DataSource.URL_LINK_PROPERTY_NAME,
					    			    "http://www.ncbi.nlm.nih.gov/entrez/viewer.fcgi?db=protein&val="
					    			    + id);
				    	}
				    }

					String querySequence = (String)properties.get(DataSource.QUERY_ALIGNMENT);
					int[][] queryGapArr = new int[0][2];
					int[][] subjectGapArr = new int[0][2];

					if (querySequence != null)
					    queryGapArr = getGaps(querySequence);
					String subjectSequence = (String)properties.get(DataSource.SUBJECT_ALIGNMENT);
					if (subjectSequence != null)
					    subjectGapArr = getGaps(subjectSequence);
					
					// Second chance for query geometry.
					if (querySequence != null) {
						qEnd = qStart + querySequence.length() - 1;
					}
					SubEntity subEntity = new ConcreteSubEntity(qStart, qEnd, sStart, sEnd, strand, priority, queryGapArr, subjectGapArr, properties);
					subEntities.add(subEntity);
                }
                if (entities.size() <= CUTOFF_NUMBER)
                    entities.add(entity);
			}

		} catch (JAXBException jex) {
			log.severe("Input Source failed to unmarshal.");
			log.severe(jex.getMessage());
			jex.printStackTrace();
		}
    	
    }

    /**
     * Parses an XML file to use as a cylinder viewer input source.
     * @param inputFileName XML file in the cylinder format.
     */
	public JAXBDataSource(String inputFileName) throws IOException {
        this(new FileInputStream(inputFileName));
	}

	/** Expose anchor length found in input file. */
	@Override
	public int getAnchorLength() {
		return anchorLength;
	}

	@Override
	public List<Entity> getEntities() {
		return entities;
	}

	/**
	 * Given the sequence from an alignment (either subject or query), find all gaps, by
	 * looking at the hyphens.
	 * 
	 * @param sequence alignment sequence from BLAST[something]
	 * @return array of start/end positions.
	 */
	private int[][] getGaps(String sequence) {
		List<int[]> gapList = new ArrayList<int[]>();
        int pos = sequence.indexOf('-');
        int cumeGapCount = 0;
        while (-1 != pos) {
            int endPos = pos + 1;
            while (sequence.charAt(endPos) == '-') {
            	cumeGapCount ++;
                endPos ++;
            }
            // Now: have gap-geometry.  pos ... endpos represents a possibly multi-base gap.
            int[] gapGeometry = new int[] {pos, endPos};
            gapList.add(gapGeometry);

            // Advance the position for next gap.
            pos = sequence.indexOf('-', endPos + 1);
        }
        int[][] returnArray = new int[gapList.size()][2];
        for (int i = 0; i < gapList.size(); i++) {
        	int[] nextEntry = (int[])gapList.get(i);
        	returnArray[i][0] = nextEntry[0];
        	returnArray[i][1] = nextEntry[1];
        }
        return returnArray;
	}

	/**
	 * Special ordering key, to allow ordering to proceed as it should.
	 * @author Leslie L Foster
	 */
	class OrderKey implements Comparable {
		public float orderBy;
		public String tieBreaker;
		public OrderKey(float orderBy, String tieBreaker) {
			this.orderBy = orderBy;
			this.tieBreaker = tieBreaker;
		}

		public int compareTo(Object obj) {
			if (obj instanceof OrderKey) {
				OrderKey that = (OrderKey)obj;
				if (this.orderBy == that.orderBy) {
					return this.tieBreaker.compareTo(that.tieBreaker);
				}
				else {
					if (this.orderBy > that.orderBy)
						return 1;
					else
						return -1;
				}
			}
			else {
				return 0;
			}
		}
	}
}
