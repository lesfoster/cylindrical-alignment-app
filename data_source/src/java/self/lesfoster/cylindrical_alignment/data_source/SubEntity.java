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
