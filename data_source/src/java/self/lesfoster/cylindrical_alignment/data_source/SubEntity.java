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
 * Sub Entity
 * Created on Feb 26, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;

import java.util.Map;

/**
 * Sub parts of an entity.  Might be shown as multiple pieces arrayed horizontally, to make
 * up the whole entity.
 * 
 * @author Leslie L. Foster
 */
public interface SubEntity {

	/*
	 * Define a few constants to use in the returns below. 
	 */
	public static int POSITIVE_STRAND = 1;
	public static int NEGATIVE_STRAND = -1;
	public static int STRAND_NOT_APPLICABLE = 0;
	public static int DEFAULT_PRIORITY = 0;

	/**
	 * Geometry methods.
	 * @return positions as their names imply.
	 */
	int getStartOnQuery();
	int getEndOnQuery();
	int getStartOnSubject();
	int getEndOnSubject();

	/**
	 * Tells which strand the entity is on.  Use the constants above. 
	 * @return pos, neg, n/a constants above.
	 */
	int getStrand();
	
	/**
	 * Returns gap positions.  Suggested: can be null.  Sub arrays are of
	 * size two. sub-array pos 0 is start, sub-array pos 1 is end.
	 * 
	 * @return array of positions relative to start pos as implied (see above).
	 */
	int[][] getGapsOnQuery();
	int[][] getGapsOnSubject();

	/**
	 * Returns a priority of a feature.  Required in the event of overlaps.
	 * 
	 * @return priority.  May be used in display (hint), and lower number is lower importance.
	 */
	int getPriority();

	/**
	 * Returns all displayable name/values that apply to the entity.
	 * 
	 * @return name/value pairs of relevant information.
	 */
	Map<String, Object> getProperties();
}
