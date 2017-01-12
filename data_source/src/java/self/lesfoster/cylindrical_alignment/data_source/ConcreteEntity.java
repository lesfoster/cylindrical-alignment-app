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
 * Concrete Entity.
 * Created on Feb 26, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.List;

/**
 * Implementation of entity--a thing with sub hits.
 * @author Leslie L. Foster
 */
public class ConcreteEntity implements Entity {

	private List<SubEntity> entities;

	/**
	 * Build the entity with all sub-entities passed in.
	 * @param entities list of sub-'hits'
	 */
	public ConcreteEntity(List<SubEntity> entities) {
		this.entities = entities;
	}
	/* (non-Javadoc)
	 * @see self.lesfoster.cylindrical_alignment.data_source.Entity#getSubEntities()
	 */
	public List getSubEntities() {
		return entities;
	}

}
