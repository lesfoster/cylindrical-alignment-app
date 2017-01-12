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
 * Appearance Source
 * Created on Mar 3, 2005
 */
package self.lesfoster.cylindrical_alignment.viewer.appearance_source;

import javafx.scene.paint.PhongMaterial;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;

/**
 * A source for material appearances for the staves of the cylinder, shown in the Cylinder Viewer.
 * @author Leslie L. Foster
 */
public interface AppearanceSource {
	public static final float OPACITY = 1.0f;

    /**
     * Create the appearance to apply to the staves (representing sub-entites).
     * @param subEntity a model object which can be queried for relevant data.
     * @return an appropriate Java3D appearance object.
     */
    PhongMaterial createSubEntityAppearance(SubEntity subEntity);

    /**
     * Create the appearance to apply to the staves (representing sub-entites).
     * @param residue a residue, such as a base or amino acid.
     * @param isBase is it a base?  false->amino acid
     * @return an appropriate Java3D appearance object.
     */
    PhongMaterial createSubEntityAppearance(char residue, boolean isBase);

    /**
     * Create the appearance to apply to an insertion solid.
     * @param subEntity model object to query for app-relevant data.
     * @return an appropriate Java3D appearance object.
     */
    PhongMaterial createSubEntityInsertionAppearance(SubEntity subEntity);

    /**
     * Create the appearance to apply to a deletion.
     * @param subEntity model object to query for app-relevant data.
     * @return an appropriate Java3D appearance object.
     */
    PhongMaterial createPerforatedAppearance(SubEntity subEntity);
	
	float[] getSelectionColor();
	
	/**
	 * Disposal method.
	 */
	void clear();
}
