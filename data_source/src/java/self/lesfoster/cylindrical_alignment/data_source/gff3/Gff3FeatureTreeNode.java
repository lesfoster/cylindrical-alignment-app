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
package self.lesfoster.cylindrical_alignment.data_source.gff3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import self.lesfoster.cylindrical_alignment.data_source.Gff3DataSource;
import self.lesfoster.cylindrical_alignment.data_source.SubEntity;

/**
 * Can be used to create a tree of sub-entities, suitable for building the "prioritized hierarchy" that can be
 * used by the viewer.  NOTE: NOT thread safe!
 * 
 * @author Leslie L Foster
 */
public class Gff3FeatureTreeNode {
	enum State { BUILD, WALK, WALKED }
	
	private static State sState = State.BUILD;
	
	private static Map<String,Gff3FeatureTreeNode> sKnownNodes = new HashMap<String,Gff3FeatureTreeNode>();
	
	private SubEntity contents;
	private String nodeId;
	private Gff3FeatureTreeNode parentNode;
	private String parentFeatureId;
	private List<Gff3FeatureTreeNode> childrenNodes;
		
	private Gff3FeatureTreeNode() {} // Force use of factory methods.
	
	/** Counts how many of the known parent features have their ids joined with their file info. */
	public static int countCompletedParentFeatures( Set<String> knownParentFeatureIDs ) {
		int retVal = 0;
		for ( String id: knownParentFeatureIDs ) {
			Gff3FeatureTreeNode node = sKnownNodes.get( id );
			if ( node != null  &&  node.getContents() != null )
				retVal ++;
		}
		return retVal;
	}
	
	public static Gff3FeatureTreeNode getGff3FeatureTreeNode( String id ) {
		Gff3FeatureTreeNode rtnNode = null;

		Gff3FeatureTreeNode oldNode = sKnownNodes.get( id );
		if ( oldNode == null ) {
			if ( sState != State.BUILD ) {
				throw new IllegalStateException( "Cannot create features after build state." );
			}
			Gff3FeatureTreeNode newNode = new Gff3FeatureTreeNode();
			sKnownNodes.put( id, newNode );
			rtnNode = newNode;
		}
		else {
			rtnNode = oldNode;
		}
		
		return rtnNode;
	}
	
	public static Gff3FeatureTreeNode getGff3FeatureTreeNode( SubEntity contents ) {
		Gff3FeatureTreeNode rtnNode = null;

		String id = contents.getProperties().get( Gff3DataSource.ID_ATTRIBUTE_NAME ).toString();
		Gff3FeatureTreeNode oldNode = sKnownNodes.get( id );
		if ( oldNode == null ) {
			if ( sState != State.BUILD ) {
				throw new IllegalStateException( "Cannot create features after build state." );
			}

			Gff3FeatureTreeNode newNode = new Gff3FeatureTreeNode();
			sKnownNodes.put( id, newNode );	
			rtnNode = newNode;
		}
		else {
			oldNode.setContents( contents );
			rtnNode = oldNode;
		}

		return rtnNode;
	}
	
	/** Uses the getter above, but only for internal use. */
	public static void trackParent( String id, Gff3FeatureTreeNode subNode) {
		Gff3FeatureTreeNode parentNode = getGff3FeatureTreeNode( id );
		parentNode.addChild( subNode );
	}

	/** The "contents" are what get stored in this tree. */
	public void setContents(SubEntity contents) {
		this.contents = contents;
	}

	public SubEntity getContents() {
		return contents;
	}
	
	/** Parent node is where we point upwards, or the GFF3 parent. */
	public void setParentNode(Gff3FeatureTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	public Gff3FeatureTreeNode getParentNode() {
		return parentNode;
	}

	/** Child nodes are made for/from GFF3 features that have the 'contents' object as their parent. */
	public void addChild( Gff3FeatureTreeNode childNode ) {
		if ( childrenNodes == null ) {
			childrenNodes = new ArrayList<Gff3FeatureTreeNode>();
		}
		childrenNodes.add( childNode );
	}
	
	public List<Gff3FeatureTreeNode> getChildrenNodes() {
		return childrenNodes;
	}

	/** Set this if there will never be any contents node. */
	public void setNodeId( String nodeId ) {
		this.nodeId = nodeId;
	}
	
	/** Returns ID of the contents node. */
	public String getNodeId() {
		if ( nodeId != null )
			return nodeId;

		if ( contents == null )
			return null;

		return (String)contents.getProperties().get( Gff3DataSource.ID_ATTRIBUTE_NAME );
	}

	/** Parent Feature ID may be necessary if the parent node itself has not yet been established. */
	public void setParentFeatureId(String parentFeatureId) {
		this.parentFeatureId = parentFeatureId;
	}

	public String getParentFeatureId() {
		return parentFeatureId;
	}
}
