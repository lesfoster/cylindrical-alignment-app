package self.lesfoster.cylindrical_alignment.data_source.gff3;

import java.util.List;
import java.util.ArrayList;

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


public class GapConverter {
	private static final int[][] EMPTY_GAP_ARRAY = new int[][] {};

	private String gff3Gap;
	
	private int[][] subjectGaps;
	private int[][] queryGaps;

	/**
	 * Gap attributes have formats that need to be re-interpreted for use in the viewer.
	 */
	public void setGff3Gap( String gff3Gap, String queryAlign, String subjectAlign ) {
		if ( gff3Gap == null  ||  gff3Gap.trim().length() == 0 ) {
			subjectGaps = EMPTY_GAP_ARRAY;
			queryGaps = EMPTY_GAP_ARRAY;
            setHyphenatedGap( queryAlign, subjectAlign );
		}
		else {
			this.gff3Gap = gff3Gap.toUpperCase();
			if ( Character.isDigit( gff3Gap.charAt( 0 ) )  ||  Character.isUpperCase( gff3Gap.charAt( gff3Gap.length() - 1 ) ) ) {
				// Starts with digit or ends with directive letter -> nMnDnI, etc.
				convertFromPrefixedGff3ToIntArrays();
			}
			else {
				convertFromSuffixedGff3ToIntArrays();
			}
			
		}
	}

	public int[][] getSubjectGaps() {
		return subjectGaps;
	}
	
	public int[][] getQueryGaps() {
		return queryGaps;
	}

    /**
     * Establish gap info based on NCBI-style dashes-in-sequence.
     */
    private void setHyphenatedGap( String queryAlign, String subjectAlign ) {
        queryGaps = getHyphenatedGaps( queryAlign );
        subjectGaps = getHyphenatedGaps( subjectAlign );
    }

    /**
     * Get gaps from NCBI-style subject and query.
     * Given the sequence from an alignment (either subject or query), find all gaps, by
     * looking at the hyphens.
     *
     * @param sequence alignment sequence from BLAST[something]
     * @return array of start/end positions.
     */
    private int[][] getHyphenatedGaps(String sequence) {
        List<int[]> gapList = new ArrayList<int[]>();
        int pos = sequence == null ? -1 : sequence.indexOf('-');
        int cumeGapCount = 0;
        while (-1 != pos) {
            int endPos = pos + 1;
            while (sequence.charAt(endPos) == '-') {
                cumeGapCount ++;
                endPos ++;
            }
            // Now: have gap-geometry.  pos ... endpos represents a possibly multi-base gap.
            int[] gapGeometry = new int[] {pos, endPos};
            gapList.add(gapGeometry);

            // Advance the position for next gap.
            pos = sequence.indexOf('-', endPos + 1);
        }
        int[][] returnArray = new int[gapList.size()][2];
        for (int i = 0; i < gapList.size(); i++) {
            int[] nextEntry = (int[])gapList.get(i);
            returnArray[i][0] = nextEntry[0];
            returnArray[i][1] = nextEntry[1];
        }
        return returnArray;
    }

    private void convertFromPrefixedGff3ToIntArrays() {
		try {
			if ( gff3Gap != null ) {
				List<int[]> subjectGapList = new ArrayList<int[]>();
				List<int[]> queryGapList = new ArrayList<int[]>();
				int strPos = 0;
				int alignmentPos = 0;
				while ( strPos < gff3Gap.length() ) {
					
					// Find the modifier.
					int modifier = 1;
					if ( Character.isDigit( gff3Gap.charAt( strPos ) )) {
						int strPos2 = strPos + 1;
						while ( strPos2 < gff3Gap.length()  &&  Character.isDigit( gff3Gap.charAt( strPos2 ) ) ) {
							strPos2 ++;  // Walk out to end of the int prefix.
						}
						String numStr = gff3Gap.substring( strPos, strPos2 );
						
						modifier = Integer.parseInt( numStr );
						strPos = strPos2;
					}

					// Apply the directive.
					char directive = gff3Gap.charAt( strPos );

					if ( directive == 'M' ) {
						alignmentPos += modifier;
					}
					else if ( directive == 'I' ) {
						computeGap( alignmentPos, modifier, subjectGapList );
					}
					else if ( directive == 'D' ) {
						computeGap( alignmentPos, modifier, queryGapList );
					}
					else {
						System.out.println("WARNING: Unknown Gaps= directive " + directive + ": ignoring.  This could negatively impact gap calculations!" );						
					}
					
					strPos ++;
				}

                subjectGaps = new int[ subjectGapList.size() ][];
                int i = 0;
                for ( int[] next: subjectGapList ) {
                    subjectGaps[ i++ ] = next;
                }
                queryGaps = new int[ queryGapList.size() ][];
                i = 0;
                for ( int[] next: queryGapList ) {
                    queryGaps[ i++ ] = next;
                }
			}

		} catch ( Exception ex ) {
			System.out.println("WARNING: failed to compute gaps for string " + gff3Gap + ".  Using empty gap setting.");
			subjectGaps = EMPTY_GAP_ARRAY;
			queryGaps = EMPTY_GAP_ARRAY;
		}
	}

	private void convertFromSuffixedGff3ToIntArrays() {
		try {
			if ( gff3Gap != null ) {
				List<int[]> subjectGapList = new ArrayList<int[]>();
				List<int[]> queryGapList = new ArrayList<int[]>();
				int pos = 0;
				String[] alignmentInstructions = gff3Gap.split( " " );
				for ( String instruction: alignmentInstructions ) {
					char directive = instruction.charAt( 0 );
					
					int modifier = Integer.parseInt( instruction.substring( 1 ) );

					if ( directive == 'M' ) {
						// Skip the match.
						pos += modifier;
					}
					else if ( directive == 'I' ) {
						pos = computeGap(pos, modifier, subjectGapList);
					}
					else if ( directive == 'D' ) {
						pos = computeGap(pos, modifier, queryGapList);
					}
					else {
						System.out.println("WARNING: Unknown Gaps= directive " + directive + ": ignoring.  This could negatively impact gap calculations!" );
					}
				}

				// Re-copy back to arrays.
				subjectGaps = copyGaps( subjectGapList );
				queryGaps = copyGaps( queryGapList );
				
			}
						
		} catch ( Exception ex ) {
			System.out.println("WARNING: failed to compute gaps for string " + gff3Gap + ".  Using empty gap setting.");
			subjectGaps = EMPTY_GAP_ARRAY;
			queryGaps = EMPTY_GAP_ARRAY;
			
		}
	}
	
	/** Move gap info from list to array. */
	private int[][] copyGaps( List<int[]> gapList ) {
		int[][] rtnGaps = new int[ gapList.size() ][2];
		for ( int i = 0; i < gapList.size(); i++ ) {
			rtnGaps[ i ] = gapList.get( i );
		}
		return rtnGaps;
	}
	
	/** Make a two-place gap array and add it to the relevant list. */
	private int computeGap( int pos, int modifier, List<int[]> gapList ) {
		int[] nextGap = new int[ 2 ];
		nextGap[ 0 ] = pos;
		nextGap[ 1 ] = pos + modifier - 1;
		pos += modifier;
		gapList.add( nextGap );
		return pos;
	}
		
}
