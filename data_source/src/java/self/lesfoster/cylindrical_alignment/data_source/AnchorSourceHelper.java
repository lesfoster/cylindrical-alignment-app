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
 * Created on Feb 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Anchor Source Helper.
 * @author Leslie L. Foster
 */
public class AnchorSourceHelper { 
	/**
	 * Creates an anchor entity, out of information given in the params.
	 * @param filename name of input file
	 * @param axisLength length of axis.
	 * @param axisEntityName name of axis.
	 * @param entityList where to add.
	 */
	public static void addAnchor(String filename, int anchorLength, String axisEntityName, List<Entity> entityList) {
		Map<String, Object> anchorProperties = new HashMap<String, Object>();
		anchorProperties.put("Anchor ID", axisEntityName);
		anchorProperties.put("Anchor Length", new Integer(anchorLength));
		anchorProperties.put("FILE", filename);
		anchorProperties.put(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME, DataSource.ANCHOR_TYPE);
        // Add the axis entity.
        SubEntity anchorSubEntity = new ConcreteSubEntity(
        		0,
				anchorLength,
				0,
				anchorLength,
				SubEntity.STRAND_NOT_APPLICABLE,
				SubEntity.DEFAULT_PRIORITY,
				new int[][]{},
				new int[][]{},
				anchorProperties
				);
        List<SubEntity> anchorSubList = new ArrayList<SubEntity>();
        anchorSubList.add(anchorSubEntity);
        Entity anchorEntity = new ConcreteEntity(anchorSubList);
		entityList.add(0, anchorEntity);
	}


}
