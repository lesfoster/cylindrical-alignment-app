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
 * Concrete Sub-Entity
 * Created on Feb 26, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.Map;

/**
 * Factory-populatable implementation of a sub entity.
 * @author Leslie L. Foster
 */
public class ConcreteSubEntity implements SubEntity {

	// geometry values.
	private int startOnQuery;
	private int endOnQuery;
	private int startOnSubject;
	private int endOnSubject;
	private int strand;
	// Gap info--subject versus query.
	private int[][] subjectGaps;
	private int[][] queryGaps;
	// Additional displayable data.
	private Map<String, Object> properties;

	private int priority;

	/**
	 * Constructor populates this 'model.
	 */
	public ConcreteSubEntity(
			int startOnQuery,
			int endOnQuery,
			int startOnSubject,
			int endOnSubject,
			int strand,
			int priority,
			int[][] queryGaps,
			int[][] subjectGaps,
			Map<String, Object> properties) {

		// Test values for validity.
		if (startOnQuery < 0 || endOnQuery < 0 || startOnSubject < 0 || endOnSubject < 0)
			throw new IllegalArgumentException("No negative values allowed for geometry.");
		if (strand != SubEntity.NEGATIVE_STRAND && strand != SubEntity.POSITIVE_STRAND && strand != SubEntity.STRAND_NOT_APPLICABLE)
			throw new IllegalArgumentException("Strand must be set to a STRAND constant as specified in sub entity interface");
		if (startOnSubject > endOnSubject || startOnQuery > endOnQuery)
			throw new IllegalArgumentException("Geometries should always start low, end high, and let directionality be set by strand value" +
					"Seeing Subject:" + startOnSubject + " " + endOnSubject + ", Query:" + startOnQuery + " " + endOnQuery);

		// Assign all internal values from constructor.
		this.startOnQuery = startOnQuery;
		this.endOnQuery = endOnQuery;
		this.startOnSubject = startOnSubject;
		this.endOnSubject = endOnSubject;
		this.strand = strand;
		this.priority = priority;
		this.subjectGaps = subjectGaps;
		this.queryGaps = queryGaps;
		this.properties = properties;
		
	}
	
	/**
	 * Helps certain data sources to change the priority to a new value from that at construction time.
	 */
	public void setPriority( int priority ) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getPriority()
	 */
	public int getPriority() {
		return priority;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getStartOnQuery()
	 */
	public int getStartOnQuery() {
		return startOnQuery;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getEndOnQuery()
	 */
	public int getEndOnQuery() {
		return endOnQuery;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getStartOnSubject()
	 */
	public int getStartOnSubject() {
		return startOnSubject;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getEndOnSubject()
	 */
	public int getEndOnSubject() {
		return endOnSubject;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getStrand()
	 */
	public int getStrand() {
		return strand;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getGapsOnQuery()
	 */
	public int[][] getGapsOnQuery() {
		return queryGaps;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getGapsOnSubject()
	 */
	public int[][] getGapsOnSubject() {
		return subjectGaps;
	}

	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.SubEntity#getProperties()
	 */
	public Map<String, Object> getProperties() {
		return properties;
	}

	/**
	 * Override to aid in debug.
	 */
	public String toString() {
		if (properties.get("name") != null)
			return properties.get("name").toString();
		else
			return startOnQuery+","+endOnQuery;
	}
}
