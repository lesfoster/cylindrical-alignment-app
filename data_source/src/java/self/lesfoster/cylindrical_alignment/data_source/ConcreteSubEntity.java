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
