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
	 * @param anchorLength length of axis.
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
