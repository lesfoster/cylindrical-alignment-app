/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source.blast_xml;

import self.lesfoster.cylindrical_alignment.input_stream_source.InputStreamSource;
import self.lesfoster.cylindrical_alignment.input_stream_source.StringInputStreamSource;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import self.lesfoster.cylindrical_alignment.data_source.web_client.WebClient;

/*
 * Copyright Leslie L. Foster, 2014.  All rights reserved.
 */

/**
 * This is a client to the BLAST service at NCBI.  Initial design is from the
 * NCBI Perl example code.
 *
 * @author Leslie L Foster
 */
public class BlastServiceClient {
    private static final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private static final String SERVICE_URL_PROP = "service_url";
    private static final String LAUNCH_CMD_FORMAT = "CMD=Put&PROGRAM=%s&DATABASE=%s&QUERY=%s";
    private static final String POLL_CMD_FORMAT = "?CMD=Get&FORMAT_OBJECT=SearchInfo&RID=%s";
    //private static final String FETCH_RESULT_CMD_FORMAT = "?CMD=Get&FORMAT_TYPE=Text&RID=%s";
    private static final String FETCH_RESULT_CMD_FORMAT = "?CMD=Get&FORMAT_TYPE=XML&RID=%s";
    private String serviceURL;
    private Properties serviceProperties = getServiceProperties();

    private Map<String,String> progToParam;
    private WebClient webClient;
    private Logger logger;

    /**
     * See https://www.ncbi.nlm.nih.gov/BLAST/blastcgihelp.shtml
     */
    public enum DatabaseChoices {
        // Both Nt and AA
        nr, pat, pdb, month,
        // Just AA
        refseq, swissprot, env_nr,
        // Just Nt
        est, est_human, est_mouse, est_others, gss, htgs, dbsts, chromosome, env_nt
    }

    public enum BlastPollResult {
        hasResults, running, noResults, unknown
    }

    /**
     * @param webClient
     * @param logger
     * @throws Exception
     */
    public BlastServiceClient(WebClient webClient, Logger logger) throws Exception {
        this.webClient = webClient;
        this.logger = logger;

        serviceURL = serviceProperties.getProperty(SERVICE_URL_PROP);
        progToParam = new HashMap<String,String>();
        progToParam.put( "megablast", "blastn&MEGABLAST=on" );
        progToParam.put( "rpsblast", "blastp&SERVICE=rpsblast" );
        progToParam.put( "blastn", "blastn" );
        progToParam.put( "blastp", "blastp" );
        progToParam.put( "blastx", "blastx" );
    }

    /**
     * Start a blast search.  Return the identifier for later check and fetch.
     *
     * URL-escape each query which is brought in.  Then concatenate into a whole
     * Multi-fasta
     *
     * @return the RID
     * @throw on IO or remote request failure.
     */
    public LaunchResponse initSearch( String program, String database, String query ) throws Exception {
        DatabaseChoices confirmedChoice = DatabaseChoices.valueOf( database );
        if ( confirmedChoice == null ) {
            logger.warning(String.format("Database %s unknown.", database ));
        }
        String progParam = progToParam.get( program );
        if ( progParam == null ) {
            logger.warning( String.format("Unknown program %s, proceeding.", program) );
            progParam = program;
        }
        String postContent = String.format( LAUNCH_CMD_FORMAT, progParam, database, query );
        WebClient.Response response = webClient.post(serviceURL, postContent, CONTENT_TYPE);
        errorCheck(response, progParam);
        // Now, figure out what the remote site replied.
        return parseLaunchResponse(response);
    }

    /**
     * This will carry out one poll iteration against blast results for
     * the request whose id is given.  At termination, the return value will
     * be non-null. If results were found, the result will be true.
     */
    public BlastPollResult checkBlastResults( String rid ) throws Exception {
        BlastPollResult rtnVal = BlastPollResult.running;

        String cmd = String.format( POLL_CMD_FORMAT, rid );
        String fullURL = serviceURL + cmd;
        WebClient.Response response = webClient.get( fullURL, CONTENT_TYPE );
        errorCheck( response, cmd );
        PollResponse pollResponse = parsePollResponse(response);
        if ( pollResponse.getStatus().equals( PollStatus.Failed ) ) {
            throw new Exception( "Failed with: " + response.getContent() );
        }
        else if (pollResponse.getStatus().equals( PollStatus.Ready )) {
            rtnVal = pollResponse.isGotResults() ?
                    BlastPollResult.hasResults :
                    BlastPollResult.noResults;
        }
        else if (pollResponse.getStatus().equals( PollStatus.Unknown )) {
            rtnVal = BlastPollResult.unknown;
        }
        return rtnVal;
    }

    /**
     * Will get the results of having launched BLAST earlier.
     *
     * @param rid should have been checked to yield TRUE for done, and
     * @return result of a GET against the fetching URL.
     * @throws Exception thrown by web client.
     */
    public InputStreamSource fetchBlastResults( String rid ) throws Exception {
        InputStreamSource inputStreamSource = null;
        String cmd = String.format( FETCH_RESULT_CMD_FORMAT, rid );
        String fullURL = serviceURL + cmd;
        WebClient.Response response = webClient.get( fullURL, CONTENT_TYPE );
        final int responseCode = response.getCode();
        if (responseCode == 404) {
            logger.warning("Not found");
        }
        else {
            inputStreamSource = new StringInputStreamSource(response.getContent());
        }
        return inputStreamSource;
    }

    /**
     * Get blast results from wherever.
     *
     * @param fullURL fully-specific URL for resource..
     * @return source for the contents of some blast result.
     * @throws Exception
     */
    public InputStreamSource fetchUrlContents(String fullURL, String cookieName, String cookieValue) throws Exception {
        WebClient.Response response = webClient.get( fullURL, CONTENT_TYPE, cookieName, cookieValue );
        final int responseCode = response.getCode();
        if (responseCode < 200  ||  responseCode > 299) {
            logger.warning("Response code was " + responseCode);
            return null;
        }
        else {
            return new StringInputStreamSource(response.getContent());
        }
    }

    /**
     * Get blast results from wherever.
     *
     * @param fullURL fully-specific URL for resource..
     * @return source for the contents of some blast result.
     * @throws Exception
     */
    public String fetchContents(String fullURL, String cookieName, String cookieValue) throws Exception {
        WebClient.Response response = webClient.get( fullURL, CONTENT_TYPE, cookieName, cookieValue );
        final int responseCode = response.getCode();
        if (responseCode < 200  ||  responseCode > 299) {
            logger.warning("Response code was " + responseCode);
            return null;
        }
        else {
            return response.getContent();
        }
    }

    /**
     * Break out the strings from the bland web client, into things expected
     * of a more-specific BLAST launch client.
     *
     * @param response with string of returned info.
     * @return marshalled for convenient use.
     */
    private LaunchResponse parseLaunchResponse( WebClient.Response response ) {
        LaunchResponse rtnVal = new LaunchResponse();
        String[] responseLines = response.getContent().split( "\n");
        for ( String line: responseLines ) {
            String[] nameValue = line.split("=");
            if ( nameValue.length < 2 ) {
                continue;
            }
            String name = nameValue[0].trim();
            String value = nameValue[1].trim();
            if ( name.equalsIgnoreCase( "RID" ) ) {
                rtnVal.setRid( value );
                System.out.println(line);
            }
            else if ( name.equalsIgnoreCase( "RTOE" ) ) {
                rtnVal.setEta( Long.parseLong( value ) );
            }
        }
        return rtnVal;
    }

    /**
     * Break out the strings from the bland web client, into things expected
     * of a more-specific BLAST launch client.
     *
     * @param response with string of returned info.
     * @return marshalled for convenient use.
     */
    private PollResponse parsePollResponse( WebClient.Response response ) {
        PollResponse rtnVal = new PollResponse();
        String[] responseLines = response.getContent().split( "\n");
        for ( String line: responseLines ) {
            String[] nameValue = line.split("=");
            if ( nameValue.length < 2 ) {
                continue;
            }
            String name = nameValue[0].trim();
            String value = nameValue[1].trim();
            if ( name.equalsIgnoreCase( "Status" ) ) {
                if ( value.equalsIgnoreCase("WAITING") ) {
                    rtnVal.setStatus(PollStatus.Waiting);
                }
                else if ( value.equalsIgnoreCase("FAILED") ) {
                    rtnVal.setStatus(PollStatus.Failed);
                }
                else if ( value.equalsIgnoreCase("UNKNOWN") ) {
                    rtnVal.setStatus(PollStatus.Unknown);
                }
                else if ( value.equalsIgnoreCase("READY") ) {
                    rtnVal.setStatus(PollStatus.Ready);
                }
            }
            else if ( name.equalsIgnoreCase("ThereAreHits")  &&  value.equalsIgnoreCase("yes") ) {
                rtnVal.setGotResults( true );
            }
        }
        return rtnVal;
    }

    private void errorCheck(WebClient.Response response, String command) throws Exception {
        if ( response.getCode() != 200 ) {
            final String msg = String.format( "Failed to launch request %s?%s: response code %d", serviceURL, command, response.getCode() );
            logger.severe( msg );
            throw new Exception( msg);
        }
    }

    /**
     * Initialize the service properties for getting their values later.
     * @return props object to query
     * @throws Exception thrown on load.
     */
    private Properties getServiceProperties() throws Exception {
        Properties props;
        props = new Properties();
        InputStream propStream =
                this.getClass().getResourceAsStream("/service.properties");
        props.load(propStream);

        return props;
    }

    public static class LaunchResponse {
        private long eta;
        private String rid;

        /**
         * @return the eta
         */
        public long getEta() {
            return eta;
        }

        /**
         * @param eta the eta to set
         */
        public void setEta(long eta) {
            this.eta = eta;
        }

        /**
         * @return the rid
         */
        public String getRid() {
            return rid;
        }

        /**
         * @param rid the rid to set
         */
        public void setRid(String rid) {
            this.rid = rid;
        }
    }

    public static enum PollStatus {
        Waiting, Failed, Unknown, Ready
    }

    public static class PollResponse {
        private PollStatus status;
        private String payload;
        private boolean gotResults;

        /**
         * @return the status
         */
        public PollStatus getStatus() {
            return status;
        }

        /**
         * @param status the status to set
         */
        public void setStatus(PollStatus status) {
            this.status = status;
        }

        /**
         * @return the payload
         */
        public String getPayload() {
            return payload;
        }

        /**
         * @param payload the payload to set
         */
        public void setPayload(String payload) {
            this.payload = payload;
        }

        /**
         * @return the gotResults
         */
        public boolean isGotResults() {
            return gotResults;
        }

        /**
         * @param gotResults the gotResults to set
         */
        public void setGotResults(boolean gotResults) {
            this.gotResults = gotResults;
        }
    }
}

