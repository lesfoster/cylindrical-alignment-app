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
