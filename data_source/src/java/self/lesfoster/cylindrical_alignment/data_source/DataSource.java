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
 * Data Source
 * Created on Feb 26, 2005
 */
package self.lesfoster.cylindrical_alignment.data_source;
import java.util.List;

/**
 * Provides all entities about which it knows.
 * @author Leslie L. Foster
 */
public interface DataSource {
	/** This property is meant to be a live WEB URL to gain additional information on the hit and/or sub hit  */
	public static final String URL_LINK_PROPERTY_NAME = "URL/Link";

	/**  This property name will be used to lookup an appearance factory to apply to the sub hit  */
	public static final String SUB_ENTITY_TYPE_PROPERTY_NAME = "subEntityType";
	public static final String NAME_PROPERTY = "name";

	public static final String SCORE_PROPERTY = "Score";
	public static final String SUBJECT_ID_PROPERTY = "Subject";
	public static final String ANCHOR_ID_PROPERTY = "Anchor ID";
	public static final String ENTITY_TYPE_BLAST_SUB_HIT = "Blast Sub Hit";
	public static final String ENTITY_TYPE_BLAST_N_SUB_HIT = "blastn";
	public static final String ENTITY_TYPE_BLAST_P_SUB_HIT = "blastp";

	/**  Pair of properties to aid in determining gaps, using sequence. */
	public static final String QUERY_ALIGNMENT = "query_align";
	public static final String SUBJECT_ALIGNMENT = "subject_align";

	/** This property is for ordering of the hits, in descending fashion. */
	public static final String ORDER_BY_PROPERTY_NAME = "order by";
	public static final String ORDER_BY_TIE_BREAKER_PROPERTY_NAME = "order by tiebreaker";

	public static final String ANCHOR_TYPE = "Anchor";

	public static final String ACCESSION_PROPERTY_NAME = "accession";
	public static final String BIT_SCORE_PROPERTY_NAME = "bit_score";

	public static final String SUBJECT_RANGE = "Subjct Range";
	public static final String QUERY_RANGE = "Query Range";

	/** list of all sub hits. */
	List<Entity> getEntities();
	int getAnchorLength();
}
