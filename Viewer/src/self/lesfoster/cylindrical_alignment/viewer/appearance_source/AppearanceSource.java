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
	
	/**
	 * Disposal method.
	 */
	void clear();
}
