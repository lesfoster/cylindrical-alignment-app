/*
Cylindrical Blast Viewer: allows viewing of Biomolecular
alignment data, in 3D, by forming the alignments into a
cylinder, such that the alignments area arranged like the staves
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
