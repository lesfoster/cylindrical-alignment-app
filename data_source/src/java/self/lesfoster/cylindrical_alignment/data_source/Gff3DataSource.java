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


package self.lesfoster.cylindrical_alignment.data_source;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import self.lesfoster.cylindrical_alignment.data_source.gff3.*;

public class Gff3DataSource implements DataSource {


/* 
Column 1: "seqid"

    The ID of the landmark used to establish the coordinate system for the current feature. 
    IDs may contain any characters, but must escape any characters not in the set [a-zA-Z0-9.:^*$@!+_?-|]. 
    In particular, IDs may not contain unescaped whitespace and must not begin with an unescaped ">". 

    To escape a character in this, or any of the other GFF3 fields, replace it with the percent sign followed by its hexadecimal 
    representation. 
    For example, ">" becomes "%E3". See URL Encoding (or: 'What are those "%20" codes in URLs?') for details. 

Column 2: "source"

    The source is a free text qualifier intended to describe the algorithm or operating procedure that generated this feature. 
    Typically this is the name of a piece of software, such as "Genescan" or a database name, such as "Genbank." 
    In effect, the source is used to extend the feature ontology by adding a qualifier to the type creating a new composite 
    type that is a subclass of the type in the type column. It is not necessary to specify a source. 
    If there is no source, put a "." (a period) in this field. 

Column 3: "type"

    The type of the feature (previously called the "method"). This is constrained to be either: 
    (a) a term from the "lite" sequence ontology, SOFA; or 
    (b) a SOFA accession number. The latter alternative is distinguished using the syntax SO:000000. This field is required. 

Columns 4 & 5: "start" and "end"

    The start and end of the feature, in 1-based integer coordinates, relative to the landmark given in column 1. Start is always 
    less than or equal to end. 

    For zero-length features, such as insertion sites, start equals end and the implied site is to the right of the indicated base in the 
    direction of the landmark. These fields are required. 

Column 6: "score"

    The score of the feature, a floating point number. As in earlier versions of the format, the semantics of the score are ill-defined. 
    It is strongly recommended that E-values be used for sequence similarity features, and that P-values be used for ab initio gene prediction 
    features. If there is no score, put a "." (a period) in this field. 

Column 7: "strand"

    The strand of the feature. + for positive strand (relative to the landmark), - for minus strand, and . for features that are not stranded. 
    In addition, ? can be used for features whose strandedness is relevant, but unknown. 

Column 8: "phase"

    For features of type "CDS", the phase indicates where the feature begins with reference to the reading frame. 
    The phase is one of the integers 0, 1, or 2, indicating the number of bases that should be removed from the beginning of 
    this feature to reach the first base of the next codon. In other words, a phase of "0" indicates that the next codon begins 
    at the first base of the region described by the current line, a phase of "1" indicates that the next codon begins at the 
    second base of this region, and a phase of "2" indicates that the codon begins at the third base of this region. 
    This is NOT to be confused with the frame, which is simply start modulo 3. If there is no phase, put a "." (a period) in this field. 

    For forward strand features, phase is counted from the start field. For reverse strand features, phase is counted from the end field. 

    The phase is required for all CDS features. 

Column 9: "attributes"

    A list of feature attributes in the format tag=value. Multiple tag=value pairs are separated by semicolons. 
    URL escaping rules are used for tags or values containing the following characters: ",=;". 
    Spaces are allowed in this field, but tabs must be replaced with the %09 URL escape. This field is not required. 

Column 9 Tags

Column 9 tags have predefined meanings:

ID
    Indicates the unique identifier of the feature. IDs must be unique within the scope of the GFF file. 

Name
    Display name for the feature. This is the name to be displayed to the user. Unlike IDs, there is no requirement that the 
    Name be unique within the file. 

Alias
    A secondary name for the feature. It is suggested that this tag be used whenever a secondary identifier for the feature is needed, 
    such as locus names and accession numbers. Unlike ID, there is no requirement that Alias be unique within the file. 

Parent
    Indicates the parent of the feature. A parent ID can be used to group exons into transcripts, transcripts into genes, and so forth. 
    A feature may have multiple parents. Parent can *only* be used to indicate a partof relationship. 

Target
    Indicates the target of a nucleotide-to-nucleotide or protein-to-nucleotide alignment.
    The format of the value is "target_id start end [strand]", where strand is optional and may be "+" or "-".
    If the target_id contains spaces, they must be escaped as hex escape %20. 

Gap
    The alignment of the feature to the target if the two are not collinear (e.g. contain gaps).
    The alignment format is taken from the CIGAR format described in the Exonerate documentation.
    (([0-9]*M)|([0-9]*D)|([0-9]*I)|([0-9]*F)|([0-9]*R))*
    Example: M3D54M2IM  has 1 match, 3 deletions, 54 matches, 2 insertions and finally one base match. 
    
    Alternately, as in the Dictostelium discoideum chromosomes, the format may be:
    ((M[0-9]* )|(I[0-9]* ))+
    Example: M65 I122 M452 I1544 M66

Derives_from
    Used to disambiguate the relationship between one feature and another when the relationship is a temporal one 
    rather than a purely structural "part of" one. This is needed for polycistronic genes. 
    See the GFF3 specification for more information. 

Note
    A free text note. 

Dbxref
    A database cross reference. See the GFF3 specification for more information. 

Ontology_term
    A cross reference to an ontology term. See the GFF3 specification for more information. 

Multiple attributes of the same type are indicated by separating the values with the comma "," character, as in: 
*/
	public static String ID_ATTRIBUTE_NAME = "ID";

	private static int NUM_GFF3_FIELDS = 9;

	private static int SEQID_COL_NUM = 0;
	private static int SOURCE_COL_NUM = 1;
	private static int TYPE_COL_NUM = 2;
	private static int START_COL_NUM = 3;
	private static int END_COL_NUM = 4;
	private static int SCORE_COL_NUM = 5;
	private static int STRAND_COL_NUM = 6;
	private static int PHASE_COL_NUM = 7;
	private static int ATTRIBUTES_COL_NUM = 8;
	
	private static String PARENT_ATTRIBUTE_NAME = "Parent";
	private static String NAME_ATTRIBUTE_NAME = "Name";
	private static String TARGET_ATTRIBUTE_NAME = "Target";
	private static String GAP_ATTRIBUTE_NAME = "Gap";
	
	private static final int CUTOFF_NUMBER = 100;  // over 100 entities kills the cylinder.

	private Set<String> parentEntitySet;
	private Map <String,String> catVsName;
	private Map <String,String> nameVsCat;
	
	private Gff3FeatureTreeNode root;
	
	private String anchorName;
	
	private int anchorStart;
	private int anchorEnd;
	
	private int inputLineNumber;
	
	private List<Entity> entityList;
	
	private GapConverter gapConverter;

	private String filename;
	public Gff3DataSource( String filename ) {
		this.filename = filename;
	}

	@Override
	public int getAnchorLength() {
		if ( entityList == null )
			parseGff3();
		// Add one, because GFF is 1-based.
		return Math.abs( anchorEnd - anchorStart + 1 );
	}

	@Override
	public List<Entity> getEntities() {
		if ( entityList == null )
			parseGff3();
		return entityList;
	}
	
	//-------------------------------------------HELPERS
	/**
	 * Digest the input data from the file, into something that can easily be converted back into the
	 * internal, proprietary XML.
	 */
	private List<Entity> parseGff3() {
		boolean inProcessing = true;
		parentEntitySet = new HashSet<String>();
		entityList = new ArrayList<Entity>();
		gapConverter = new GapConverter();
		loadSofaInfo();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			String inbuf = null;
			Set<String> nonSOFAFeatureTypes = new HashSet<String>();
			StringBuilder emptyFeatureIdBuffer = new StringBuilder();
			StringBuilder emptyNameBuffer = new StringBuilder();
			while(null != (inbuf = reader.readLine())) {
				inputLineNumber ++;
				// Bail when too many entities for the viewer to handle, have been processed.
				if ( Gff3FeatureTreeNode.countCompletedParentFeatures( parentEntitySet ) >= CUTOFF_NUMBER ) {
					break;
				}
				
				if ( root != null  &&   root.getChildrenNodes() != null  &&  root.getChildrenNodes().size() >= CUTOFF_NUMBER ) {
					break;
				}
				
				if ( inbuf.toUpperCase().startsWith( "##FASTA" ) ) {
					inProcessing = false;
				}
				else if ( inbuf.startsWith( "#" )   ||   (! inProcessing ) ) {
					// Skip comments and FASTA data.
					continue;
				}
				else {
					String[] fields = inbuf.split( "\t" );
					// Check number of fields.
					if ( fields.length < NUM_GFF3_FIELDS ) {
						throw new Exception( 
								"Input line \n\t[" + inbuf + "]\n does not contain the number of fields specified for GFF3, which is " +
								NUM_GFF3_FIELDS );
					}
					else {
						// The sequence ID is "landmark for the coordinate system".  This viewer can accommodate only one such feature,
						// which it uses as the axis (and root).  Here, this is established.  An anchor name may be injected into this
						// data source.  If not, pick up the first referenced "landmark" as axis.
						String seqId = fields[ SEQID_COL_NUM ];
						if ( root == null ) {
							if ( anchorName == null   ||   anchorName.equalsIgnoreCase( seqId ) ) {
								root = Gff3FeatureTreeNode.getGff3FeatureTreeNode( seqId );
								root.setNodeId( seqId );
								anchorName = seqId;   // Ensure that if anchor name HAD been null, it won't be now.
							}
						}
						
						// Given that an anchor name has been established (through injection or discovery), only its features will be aligned.
						if ( seqId.equals( anchorName ) ) {
							int start = Integer.parseInt( fields[ START_COL_NUM ] );
							int end = Integer.parseInt( fields[ END_COL_NUM ] );
							String featureType = fields[ TYPE_COL_NUM ];
                            String strandField = fields[STRAND_COL_NUM].trim();
                            int strand = 0;
                            switch ( strandField.charAt( 0 ) ) {
                                case '.' : strand = SubEntity.STRAND_NOT_APPLICABLE;
                                           break;
                                case '+' : strand = SubEntity.POSITIVE_STRAND;
                                           break;
                                case '-' : strand = SubEntity.NEGATIVE_STRAND;
                                           break;
                                default:
                                           strand = SubEntity.STRAND_NOT_APPLICABLE;

                            }
						
							Map<String, Object> attributes = handleAttributes(
									fields, start, end, featureType);

							String idAttribute = getNonNullAttribute( attributes, ID_ATTRIBUTE_NAME );
							if ( idAttribute.length() == 0 ) {
								idAttribute = getNonNullAttribute( attributes, NAME_ATTRIBUTE_NAME );
								emptyFeatureIdBuffer.append( " " + inputLineNumber );
							}
							if ( idAttribute.length() == 0 ) {
								idAttribute = "Uknown (line " + inputLineNumber + ")";
								emptyNameBuffer.append( " " + inputLineNumber );
							}
							attributes.put( ID_ATTRIBUTE_NAME, idAttribute );       // Ensure best id is used.
							attributes.put( ACCESSION_PROPERTY_NAME, idAttribute );
							attributes.put( ANCHOR_ID_PROPERTY, seqId );
							
							// Stick with name instead of category.  Preserve category as another feature.
							if ( catVsName.containsKey( featureType ) ) {
								attributes.put( "SOFA_Category", featureType ); // Special attribute for old feature type, which is category.
								featureType = catVsName.get( featureType );
							}

							if ( ! catVsName.containsValue( featureType ) ) {
								nonSOFAFeatureTypes.add( featureType );
							}
							else {
								attributes.put( "SOFA_Category", nameVsCat.get( featureType ) );
							}
							
							String parentAttribute = getNonNullAttribute( attributes, PARENT_ATTRIBUTE_NAME );

							if ( parentAttribute.length() == 0  &&  seqId.equalsIgnoreCase( idAttribute ) ) {
								// This must be the anchor.  Setup the start and the end.  NOTE that this format is 1-based!								
								anchorStart = start;
								anchorEnd = end;
								
								AnchorSourceHelper.addAnchor( filename, getAnchorLength(), seqId, entityList );
							}
							else if ( featureType.equalsIgnoreCase( "gap" ) ) {
								handleNonoverlappedFeature(start, end, strand,
										attributes, idAttribute, featureType);							
							}
							else if ( parentAttribute.equals( seqId ) ) {
								if ( featureType.equalsIgnoreCase( "contig" ) ) {
									handleChildFeature( start, end, strand, attributes, idAttribute, seqId );
								}
								else {
									// This must be a parent at first level.
									Gff3FeatureTreeNode node = Gff3FeatureTreeNode.getGff3FeatureTreeNode( idAttribute );
									node.setNodeId( idAttribute );
									root.addChild( node );

									if ( ! parentEntitySet.contains( idAttribute ) ) {
										parentEntitySet.add( idAttribute );
									}
									
								}
							}
							else {
								handleChildFeature(start, end, strand,
										attributes, idAttribute, parentAttribute);
							}
							
						}

						
					}
					
				}
				
			}

			makeParentEntities();

			if ( emptyFeatureIdBuffer.length() > 0 ) {
				System.out.println( "WARNING: Empty ID attributes for GFF3 features in " + filename + " at line numbers: " + emptyFeatureIdBuffer.toString() );
			}
			if ( emptyNameBuffer.length() > 0 ) {
				System.out.println( "WARNING: Empty name attribute for GFF3 features in " + filename + " at line numbers: " + emptyNameBuffer.toString() );
			}

			if ( nonSOFAFeatureTypes.size() > 0 ) {
				System.out.println( "WARNING: these feature types were given, but not included in SOFA feature type list:" );
				for ( String featureType: nonSOFAFeatureTypes ) {
					System.out.print(" " + featureType);
				}
				System.out.println();
			}
			
		} catch (Exception e) {
			System.out.println("ERROR: Failed to parse GFF3 file " + filename);
			e.printStackTrace();
		}
		
		return entityList;
	}

	/**
	 * Start at root, make a primary parent entity for each of root's direct children, and add all descendants.
	 */
	private void makeParentEntities() {
		//entityList = new ArrayList<Entity>();

		// Walk through root's children.
		if ( root.getChildrenNodes() == null ) {
			return;
		}
		for ( Gff3FeatureTreeNode nextNode: root.getChildrenNodes() ) {
			List<SubEntity> collector = new ArrayList<SubEntity>();
			
			ConcreteEntity nextParent = new ConcreteEntity( collector );
			if ( nextNode.getContents() != null ) {
				collector.add( nextNode.getContents() );
			}
			collectEntities( nextNode, collector, 0 );
			
			entityList.add( nextParent );
			
		}
	}
	
	/**
	 * Recursively accumulate child nodes into first-ancestor collection, increasing priority with each
	 * new level of children.
	 * 
	 * @param node may or may not have children to add.
	 * @param collector will have all the descendant nodes.
	 * @param priority advances with each level descendant.
	 */
	private void collectEntities( Gff3FeatureTreeNode node, List<SubEntity> collector, int priority ) {
		List<Gff3FeatureTreeNode>subNodes = node.getChildrenNodes(); 
		if ( subNodes != null ) {
			int childPriority = priority + 10;
			for ( Gff3FeatureTreeNode nextSubNode: subNodes ) {
				ConcreteSubEntity contentEntity = (ConcreteSubEntity)nextSubNode.getContents();
				contentEntity.setPriority( childPriority );
				collector.add( contentEntity );
				
				collectEntities( nextSubNode, collector, childPriority );
			}

		}
	}

	/**
	 * Certain feature types are guaranteed never to overlap each other (or to be non-informative when they do.  For these,
	 * we grant them one common parent, and have them all be subfeatures of that.
	 */
	private void handleNonoverlappedFeature(int start, int end, int strand,
			Map<String, Object> attributes, String idAttribute,
			String featureType) {

		String parentAttribute = "_" + featureType;

		// Create a node for this entity.
		Gff3FeatureTreeNode node = Gff3FeatureTreeNode.getGff3FeatureTreeNode( idAttribute );
		ConcreteSubEntity cse = createSubEntity( start, end,
				strand, attributes );
		node.setContents( cse );

		Gff3FeatureTreeNode mockParentNode = Gff3FeatureTreeNode.getGff3FeatureTreeNode( parentAttribute );

		// Either the parent entity is known or it needs to be added.
		if ( ! parentEntitySet.contains( parentAttribute ) ) {
			parentEntitySet.add( parentAttribute );

			// Now add the mock-parent to the root feature tree node.
			mockParentNode.setParentNode( root );
			root.addChild( mockParentNode );
			
		}
		// Associate the mock parent node with the current-feature node. 
		mockParentNode.addChild( node );

	}

	private void handleChildFeature(int start, int end, int strand,
			Map<String, Object> attributes, String idAttribute,
			String parentAttribute) {
		// Special, non-parented feature.  Make itself its own "guardian".
		Gff3FeatureTreeNode node = Gff3FeatureTreeNode.getGff3FeatureTreeNode( idAttribute );
		String trackedParent = parentAttribute;
		if ( parentAttribute.length() == 0 ) {
			parentAttribute = "_Self_" + idAttribute;
			trackedParent = root.getNodeId();
		}

		// Keep track of the parent by ID.
		Gff3FeatureTreeNode.trackParent( trackedParent, node );

		// Either the parent entity is known or it needs to be added.
		if ( ! parentEntitySet.contains( parentAttribute ) ) {
			parentEntitySet.add( parentAttribute );
		}

		ConcreteSubEntity cse = createSubEntity( start, end,
				strand, attributes );
		node.setContents( cse );
	}

	private Map<String, Object> handleAttributes(String[] fields, int start,
			int end, String featureType) {
		// Take all attributes first at face value.
		Map<String,Object> attributes = parseAttributes( fields[ ATTRIBUTES_COL_NUM ] );
		
		// Then enforce a specific naming scheme.
		attributes.put( SUB_ENTITY_TYPE_PROPERTY_NAME, featureType );
		attributes.put( NAME_PROPERTY, getNonNullAttribute( attributes, NAME_ATTRIBUTE_NAME ) );
		attributes.put( "Type", featureType );
		attributes.put( "Source",  fields[ SOURCE_COL_NUM ] );
		attributes.put( "Phase", fields[ PHASE_COL_NUM ] );
		attributes.put( "Strand", fields[ STRAND_COL_NUM ] );
		attributes.put( SCORE_PROPERTY, fields[ SCORE_COL_NUM ] );
		attributes.put( QUERY_RANGE, start + ".." + end );            
		attributes.put( SUBJECT_RANGE, 1 + ".." + (end - start + 1) );      // Labeling subject range 1-based.
		return attributes;
	}

	private ConcreteSubEntity createSubEntity(int start, int end, int strand,
			Map<String, Object> attributes) {

        String subjAlign = (String)attributes.get( SUBJECT_ALIGNMENT );
        String qryAlign = (String)attributes.get( QUERY_ALIGNMENT );
		gapConverter.setGff3Gap(
                (String)attributes.get( GAP_ATTRIBUTE_NAME ),
                qryAlign,
                subjAlign
        );

        int qaLen = end - start;
        if ( qryAlign != null ) {
            qaLen = qryAlign.length();
        }
        int saLen = end - start;
        if ( subjAlign != null ) {
            saLen = subjAlign.length();
        }
		ConcreteSubEntity cse = new ConcreteSubEntity(
				start,
			    start + qaLen,
				0,
				saLen,
				strand,
				0,
				gapConverter.getQueryGaps(),
				gapConverter.getSubjectGaps(),
				attributes
				
		);
		return cse;
	}

	private String getNonNullAttribute(Map<String, Object> attributes, String name ) {
		Object value = attributes.get( name );
		return value == null ? "" : value.toString();
	}
		
	/**
	 * Breaks up the attribute field into mappings of name vs value.
	 * @param attribStr format of x1=y1,x2=y2,x3=y3
	 * @return mapping of x's vs y's
	 */
	private Map<String,Object> parseAttributes( String attribStr ) {
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		
		String[] attribArr = attribStr.split( ";" );
		for ( String nameValue: attribArr ) {
			String[] nameVsValue = nameValue.split( "=" );
			if ( nameVsValue.length == 2 )
				rtnMap.put( nameVsValue[ 0 ], percentHexConvert( nameVsValue[ 1 ] ) );
			else
				rtnMap.put( nameValue, "?" );
		}
		
		return rtnMap;
	}
	
	/**
	 * GFF3 files may contained escaped special characters.  These characters could otherwise be construed as separators, or
	 * it could be that they are not easily printable in ascii.  We back translate these here.
	 * 
	 * @param value may or may not have %'s in it.
	 * @return one without escape values.
	 */
	private String percentHexConvert( String value ) {
		if ( -1 == value.indexOf( "%" ) )
			return value;
		
		// Must go through looking for the percent sign.
		int nextPos = 0;
		StringBuilder modifiedValue = new StringBuilder();
		int pos;
		while ( -1 != (pos = value.indexOf( "%", nextPos ) ) ) {
			modifiedValue.append( value.substring( nextPos, pos ) );
			try {
				char ch = (char)Integer.parseInt( value.substring( pos + 1, pos + 3 ), 16 );
				modifiedValue.append( ch );
				nextPos = pos + 3;
				
			} catch ( RuntimeException re ) {
				// If bad number, or no more string, then the percent-escaped sequence was not intended.
				modifiedValue.append( "%" );
				nextPos = pos + 1;

			}
		}
		if ( nextPos < value.length() ) {
			modifiedValue.append( value.substring( nextPos ) );
		}
		
		return modifiedValue.toString();
	}
	
	/**
	 * Find the SOFA info file, and load it into a map for later use.
	 */
	private void loadSofaInfo() {
		boolean loaded = false;
		try {
			
			catVsName = new HashMap<String,String>();
			nameVsCat = new HashMap<String,String>();
			String mappingResource = "/self/lesfoster/cylindrical_viewer/data_source/gff3/SOFA_catVsName.txt";
			InputStream is = this.getClass().getResourceAsStream( mappingResource );
			if ( is != null ) {
				String inbuf = null;
				BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
				while ( null != ( inbuf = br.readLine() ) ) {
					String[] inbufParts = inbuf.split( " " );
					if ( inbufParts.length == 2 ) {
						catVsName.put( inbufParts[ 0 ], inbufParts[ 1 ] );
						nameVsCat.put( inbufParts[ 1 ], inbufParts[ 0 ] );
					}
				}
				br.close();
				
				loaded = true;
			}
			else {
				loaded = false;
			}
		} catch ( Exception ex ) {
			loaded = false;
		}
		
		if (! loaded ) {
			System.out.println("ERROR: failed to load all of SOFA categories.");
		}
	}

}
