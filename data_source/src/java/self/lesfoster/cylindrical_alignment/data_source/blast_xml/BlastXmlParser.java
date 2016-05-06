/*
 * Copyright Leslie L Foster, 2014, all rights reserved.
 */
package self.lesfoster.cylindrical_alignment.data_source.blast_xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import self.lesfoster.cylindrical_alignment.data_source.*;

import java.io.IOException;
import java.io.Reader;
import java.util.*;
import java.util.logging.Logger;

/**
 * Uses BLAST XML directly, using XmlPull technology.
 *
 * Created by Leslie L Foster on 4/3/14.
 */
public class BlastXmlParser {
    public static final String BLAST_OUTPUT_QUERYDEF = "BlastOutput_query-def";
    public static final String BLAST_OUTPUT_PROGRAM = "BlastOutput_program";
    public static final String BLAST_OUTPUT_ANCHOR_LENGTH = "BlastOutput_query-len";

    public static final String HIT_LEN = "Hit_len";
    public static final String HIT_ACCESSION = "Hit_accession";
    public static final String HIT_DEF = "Hit_def";
    public static final String HIT_ID = "Hit_id";
    public static final String HSP_NUM = "Hsp_num";
    public static final String HSP_BIT_SCORE = "Hsp_bit-score";
    public static final String HSP_SCORE = "Hsp_score";
    public static final String HSP_QUERY_FROM = "Hsp_query-from";
    public static final String HSP_QUERY_TO = "Hsp_query-to";
    public static final String HSP_HIT_FROM = "Hsp_hit-from";
    public static final String HSP_HIT_TO = "Hsp_hit-to";
    public static final String HSP_QUERY_FRAME = "Hsp_query-frame";
    public static final String HSP_IDENTITY = "Hsp_identity";
    public static final String HSP_ALIGN_LEN = "Hsp_align-len";
    public static final String HSP_POSITIVE = "Hsp_positive";
    public static final String HSP_GAPS = "Hsp_gaps";
    public static final String HSP_QSEQ = "Hsp_qseq";
    public static final String HSP_ALIGN_LEN1 = "Hsp_align-len";
    public static final String HSP_HSEQ = "Hsp_hseq";
    public static final String HSP_MIDLINE = "Hsp_midline";
    public static final String HSP = "Hsp";
    public static final String ITERATION_HITS = "Iteration_hits";

    private static String LOG_CONTEXT = "BlastXmlParser";
    private static final int MAX_RETURNED_ENTITES = 50;
    private static final int MAX_RETURNED_SUBHITS = 240;

    private int maxQueryPos = Integer.MIN_VALUE;
    private int minQueryPos = Integer.MAX_VALUE;
	private final Logger log = Logger.getLogger(BlastXmlParser.class.getName());

    private Reader reader;
    private List<Entity> entities;
    private int anchorLength;
    private String queryId;
    private int cumulativeHspCount;
    private boolean capacityRemaining = true;

    private String programName;

    public BlastXmlParser( Reader reader ) {
        this.reader = reader;
    }

    public List<Entity> getEntities() {
        if ( entities == null ) {
            try {
                entities = new ArrayList<Entity>();
                parseXml( reader );
            } catch ( Exception ex ) {
                entities = Collections.EMPTY_LIST;
                anchorLength = 0;
                log.severe("Failed to parse XML.");
                ex.printStackTrace();
            }
        }
        return entities;
    }

    public int getAnchorLength() {
        return anchorLength;
    }

    public String getQueryId() {
        return queryId;
    }


    public int getStartOfQueryRange() {
        return minQueryPos;
    }

    public int getEndOfQueryRange() {
        return maxQueryPos;
    }

    private void parseXml(Reader reader) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( reader );
        xpp.next();
        //xpp.nextTag();

        readBlastOutput( xpp );
    }

    private void readBlastOutput( XmlPullParser parser ) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "BlastOutput");

        Map<String,String> nameValue = new HashMap<String,String>();
        nameValue.put( BLAST_OUTPUT_PROGRAM, null );
        nameValue.put( BLAST_OUTPUT_QUERYDEF, null );
        nameValue.put( BLAST_OUTPUT_ANCHOR_LENGTH, null );
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // Find various single-datum tags.
            boolean foundTag = getSubtagContent(nameValue, parser);

            // Pick this up before subsequent use.
            queryId = nameValue.get(BLAST_OUTPUT_QUERYDEF);
            programName = nameValue.get( BLAST_OUTPUT_PROGRAM );
            if ( programName == null ) {
                programName = DataSource.ENTITY_TYPE_BLAST_N_SUB_HIT;
            }

            if ( ! foundTag ) {
                foundTag = readBlastOutputIteration( "BlastOutput_iterations", parser );
                if ( ! foundTag )
                    skip(parser);
            }
        }
        String anchorLenStr = nameValue.get(BLAST_OUTPUT_ANCHOR_LENGTH);
        if ( anchorLenStr != null ) {
            anchorLength = Integer.parseInt( anchorLenStr.trim() );
        }
        //return entries;
    }

    private boolean getSubtagContent( Map<String,String> nameValue, XmlPullParser parser ) throws XmlPullParserException, IOException {
        boolean rtnVal = false;
        final String tagName = parser.getName();
        if ( nameValue.keySet().contains( tagName) ) {
            parser.require(XmlPullParser.START_TAG, null, tagName);
            nameValue.put(tagName, readText(parser));
            parser.require(XmlPullParser.END_TAG, null, tagName);
            rtnVal = true;
        }
        return rtnVal;
    }

    private boolean readBlastOutputIteration( String subtagName, XmlPullParser parser )throws XmlPullParserException, IOException {
        boolean rtnVal = false;
        Map<String,String> nameValue = new HashMap<String,String>();
        nameValue.put( "Iteration_iter-num", null );
        nameValue.put( "Iteration_query-ID", null );
        nameValue.put( "Iteration_query-def", null );
        nameValue.put( "Iteration_query-len", null );

        if (parser.getName().equals(subtagName)) {
            parser.require(XmlPullParser.START_TAG, null, subtagName);
            while ( parser.next() != XmlPullParser.START_TAG );
            parser.require(XmlPullParser.START_TAG, null, "Iteration");

            // Collect iteration descriptions. Until finds iteration-hits
            populateMap( parser, nameValue, ITERATION_HITS );
            parser.require(XmlPullParser.START_TAG, null, ITERATION_HITS);

            // Now collect all  hit data.
            while (parser.next() != XmlPullParser.END_TAG ) {
                if ( parser.getEventType() != XmlPullParser.START_TAG ) {
                    continue;
                }
                if ( parser.getName().equals("Hit")) {
                    List<SubEntity> subEntityList = new ArrayList<SubEntity>();
                    // Getting hit contents.
                    readHit( parser, subEntityList, nameValue );
                    Entity entity = new ConcreteEntity( subEntityList );
                    // Need to shield memory use. Check both number of hits, and number of hsps-within-hits.
                    if (capacityRemaining) {
                        int hspCount = subEntityList.size();
                        if ( cumulativeHspCount + hspCount  <=  BlastXmlParser.MAX_RETURNED_SUBHITS ) {
                            cumulativeHspCount += hspCount;
                            if ( entities.size() <= MAX_RETURNED_ENTITES ) {
                                entities.add(entity);
                            }
                            else {
                                capacityRemaining = false;
                            }
                        }
                        else {
                            log.info("No more hits at " + cumulativeHspCount + " HSPs/" + entities.size() + " Hits.  Next hit contains " + hspCount + " HSPs.");
                            capacityRemaining = false;
                        }
                    }
                }
            }
            rtnVal = true;

        }
        return rtnVal;
    }

    private void populateMap( XmlPullParser parser, Map<String,String> nameValue, String terminatingStartTag )
            throws XmlPullParserException, IOException {
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if (terminatingStartTag != null  &&  parser.getName().equals(terminatingStartTag)) {
                break;
            }

            // Find various single-datum tags.
            boolean foundTag = getSubtagContent(nameValue, parser);
            if ( ! foundTag ) {
                skip(parser);
            }
        }

        //for ( String key: nameValue.keySet() ) {
        //    System.out.println(key + "=" + nameValue.get(key));
        //}

    }

    private void readHit( XmlPullParser parser, List<SubEntity> subEntityList, Map<String,String> nameValue ) throws XmlPullParserException, IOException {
        /*
          <Hit_num>1</Hit_num>
            <Hit_id>gi|41054222|ref|NM_199798.1|</Hit_id>
            <Hit_def>Danio rerio Ras-related associated with diabetes (rrad), mRNA &gt;gi|32766278|gb|BC055120.1| Danio rerio Ras-related associated with diabetes, mRNA (cDNA clone MGC:63471 IMAGE:2600346), complete cds</Hit_def>
            <Hit_accession>NM_199798</Hit_accession>
            <Hit_len>1778</Hit_len>
        */
        Map<String,String> subNameValue = new HashMap<String,String>();
        subNameValue.put(HIT_ID, null);
        subNameValue.put(HIT_DEF, null);
        subNameValue.put(HIT_ACCESSION, null);
        subNameValue.put(HIT_LEN, null);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // Find various single-datum tags.
            boolean foundTag = getSubtagContent(subNameValue, parser);
            if ( parser.getName().equals("Hit_hsps")) {
                while ( parser.next() != XmlPullParser.START_TAG ); // Read to next tag
                // Get all the HSP tags below.
                String tagName = parser.getName();
                while (tagName != null  &&  tagName.equals( "Hsp" ) ) {
                    readHsp( parser, subEntityList );
                    // Eat the end tag.
					parser.nextToken();
					//		.nextTag();  // Available on Android.
                    tagName = parser.getName();

					// This block of skip-code is needed on desktop, but
					// not on Android.
                    if ( parser.getEventType() == XmlPullParser.END_TAG ) {
                        // Eat the Hit_hsps end tag.
                        parser.nextTag();
                    }
                }
                foundTag = true;

            }
            if ( ! foundTag ) {
                skip( parser );
            }
        }

        //System.out.println("Hit: " + subNameValue.get(HIT_ID) + ", " + subNameValue.get(HIT_DEF) + ", " + subNameValue.get(HIT_ACCESSION));
    }

    private void readHsp( XmlPullParser parser, List<SubEntity> subEntityList ) throws XmlPullParserException, IOException {
        Map<String,String> hspNameValue = new HashMap<String,String>();
        hspNameValue.put(HSP_NUM, null);
        hspNameValue.put(HSP_BIT_SCORE, null);
        hspNameValue.put(HSP_SCORE, null);
        hspNameValue.put(HSP_QUERY_FROM, null);
        hspNameValue.put(HSP_QUERY_TO, null);
        hspNameValue.put(HSP_HIT_FROM, null);
        hspNameValue.put(HSP_HIT_TO, null);
        hspNameValue.put(HSP_QUERY_FRAME, null);
        hspNameValue.put(HSP_IDENTITY, null);
        hspNameValue.put(HSP_ALIGN_LEN, null);
        hspNameValue.put(HSP_POSITIVE, null);
        hspNameValue.put(HSP_GAPS, null);
        hspNameValue.put(HSP_QSEQ, null);
        hspNameValue.put(HSP_ALIGN_LEN1, null);
        hspNameValue.put(HSP_HSEQ, null);
        hspNameValue.put(HSP_MIDLINE, null);
        populateMap(parser, hspNameValue, null);  //!!!  This is not terminated with start tag.  Stops with end tag for HSP

        Map<String,Object> properties = new HashMap<String,Object>();
        for ( String key: hspNameValue.keySet() ) {
            String value = hspNameValue.get( key );
            if ( value != null ) {
                String newKey = null;
                if ( key.startsWith("Hsp_") ) {
                    newKey = key.substring( 4 );
                }
                else {
                    newKey = key;
                }
                properties.put( newKey, value.trim() );
            }
        }

        int startOnQuery = Integer.parseInt( hspNameValue.get(HSP_QUERY_FROM) );
        if (startOnQuery > maxQueryPos) {
            maxQueryPos = startOnQuery;
        }
        if (startOnQuery < minQueryPos) {
            minQueryPos = startOnQuery;
        }
        int endOnQuery = Integer.parseInt( hspNameValue.get(HSP_QUERY_TO) );
        if (endOnQuery > maxQueryPos) {
            maxQueryPos = endOnQuery;
        }
        if (endOnQuery < minQueryPos) {
            minQueryPos = endOnQuery;
        }

        int startOnSubject = Integer.parseInt( hspNameValue.get(HSP_HIT_FROM) );
        int endOnSubject = Integer.parseInt( hspNameValue.get(HSP_HIT_TO) );
        int strand = SubEntity.POSITIVE_STRAND;
        if ( endOnQuery < startOnQuery ) {
            strand = SubEntity.NEGATIVE_STRAND;
            int temp = endOnSubject;
            endOnSubject = startOnSubject;
            startOnQuery = temp;
        }
        else if ( endOnSubject < startOnSubject ) {
            strand = SubEntity.NEGATIVE_STRAND;
            int temp = endOnSubject;
            endOnSubject = startOnSubject;
            startOnSubject = temp;
        }
        final String subjectSequence = hspNameValue.get(HSP_HSEQ);
        properties.put( DataSource.SUBJECT_ALIGNMENT, subjectSequence );
        int[][] subjectGaps = getGaps(subjectSequence);
        final String querySequence = hspNameValue.get(HSP_QSEQ);
        properties.put( DataSource.QUERY_ALIGNMENT, querySequence );
        int[][] queryGaps = getGaps(querySequence);
        int priority = SubEntity.DEFAULT_PRIORITY;
        properties.put(DataSource.SUB_ENTITY_TYPE_PROPERTY_NAME, programName);
        SubEntity subEntity = new ConcreteSubEntity(
                startOnQuery,
                endOnQuery,
                startOnSubject,
                endOnSubject,
                strand,
                priority,
                queryGaps,
                subjectGaps,
                properties
        );
        subEntityList.add( subEntity );

    }

    /**
     * Given the sequence from an alignment (either subject or query), find all gaps, by
     * looking at the hyphens.
     *
     * @param sequence alignment sequence from BLAST[something]
     * @return array of start/end positions.
     */
    private int[][] getGaps(String sequence) {
        if ( sequence == null ) {
            return new int[0][2];
        }
        List<int[]> gapList = new ArrayList<int[]>();
        int pos = sequence.indexOf('-');
        while (-1 != pos) {
            int endPos = pos + 1;
            while (sequence.charAt(endPos) == '-') {
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

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.next();
            //parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException,
            IOException {

        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}



