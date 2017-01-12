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

import self.lesfoster.cylindrical_alignment.data_source.blast_xml.BlastServiceClient;
import self.lesfoster.cylindrical_alignment.data_source.blast_xml.BlastXmlParser;
import self.lesfoster.cylindrical_alignment.data_source.web_client.ApacheWebClient;
import self.lesfoster.cylindrical_alignment.input_stream_source.InputStreamSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Logger;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;

/**
 * Fetches hits that have been precomputed, and remain at the ready.  Fetches by ID.  Searches by date.
 * Created by Leslie L Foster on 11/23/2015.
 */
public class PrecomputedBlastXmlDataSource implements DataSource, Serializable {
    public enum StringSearchType{ SPECIES, PROGRAM }

	private final Logger logger = Logger.getLogger(PrecomputedBlastXmlDataSource.class.getName());

    private final static String FETCH_BY_ID_URL_FMT = "%s://%s:%d/CAV/results/getResultById?id=%s";
    private final static String FETCH_BY_SPECIES_URL_FMT = "%s://%s:%d/CAV/results/getResultBySpecies?species=%s";
    private final static String FETCH_BY_PROGRAM_URL_FMT = "%s://%s:%d/CAV/results/getResultByProgram?program=%s";
    private final static String FETCH_BY_DATE_URL_FMT = "%s://%s:%d/CAV/results/getResultByDate?date=%d";

    private String id;
    private int anchorLength = -1;
    private List<Entity> entities = null;
	private HostBean hostBean;

    public PrecomputedBlastXmlDataSource() {
    }

    public void setId( String id ) {
        this.id = id;
    }
	
	public void setHostBean(HostBean hostBean) {
		this.hostBean = hostBean;
	}

	@Override
    public List<Entity> getEntities() {
        if ( entities == null ) {
            fetchEntities();
        }
        return entities;
    }

	@Override
    public int getAnchorLength() {
        if ( entities == null ) {
            fetchEntities();
        }
        return anchorLength;
    }

	public List<SearchResult> getDateSearchResults(int year, int month, int day) {
		List<SearchResult> rtnVal = null;
		try {
			final String user = hostBean.getCurrentUser();
			final String pass = hostBean.getCurrentPass();
			final String currentHost = hostBean.getCurrentHost();
			final int currentPort = hostBean.getCurrentPort();
			final String currentProtocol = hostBean.getCurrentProtocol();

			String cookie = getCookie(user, pass, currentHost, currentPort, currentProtocol);
			Calendar cal = GregorianCalendar.getInstance();
			cal.set(Calendar.YEAR, year);
			cal.set(Calendar.MONTH, month);  // NOTE: expect 0-based 0..11 months number on input.
			cal.set(Calendar.DAY_OF_MONTH, day);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Long dateTime = cal.getTime().getTime();

			final ApacheWebClient webClient = new ApacheWebClient();
			BlastServiceClient client = new BlastServiceClient(
					webClient, logger
			);
			String targetUrl = String.format(FETCH_BY_DATE_URL_FMT, currentProtocol, currentHost, currentPort, dateTime);
			logger.info(targetUrl);
			String resultsAsString = client.fetchContents(targetUrl, LoginCookieFetcher.FORM_SESSION_COOKIE, cookie);
			if (resultsAsString != null) {
				// Time to pull the results list.
				ResultsParser parser = new ResultsParser();
				rtnVal = parser.parseHits(resultsAsString);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return rtnVal;
	}

	public List<SearchResult> getBySingleStringSearchResults(StringSearchType type, String value) {
		List<SearchResult> rtnVal = null;
		try {
			final String user = hostBean.getCurrentUser();
			final String pass = hostBean.getCurrentPass();
			final String currentHost = hostBean.getCurrentHost();
			final int currentPort = hostBean.getCurrentPort();
			final String currentProtocol = hostBean.getCurrentProtocol();

			String cookie = getCookie(user, pass, currentHost, currentPort, currentProtocol);

			final ApacheWebClient webClient = new ApacheWebClient();
			BlastServiceClient client = new BlastServiceClient(
					webClient, logger
			);
			String singleStringSearchFormat = (type == StringSearchType.SPECIES)
					? FETCH_BY_SPECIES_URL_FMT
					: FETCH_BY_PROGRAM_URL_FMT;
			String targetUrl
					= String.format(singleStringSearchFormat, currentProtocol, currentHost, currentPort, value)
					.replace(' ', '+');
			logger.info(targetUrl);
			String resultsAsString = client.fetchContents(targetUrl, LoginCookieFetcher.FORM_SESSION_COOKIE, cookie);
			if (resultsAsString != null) {
				// Time to pull the results list.
				ResultsParser parser = new ResultsParser();
				rtnVal = parser.parseHits(resultsAsString);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return rtnVal;
	}

	/**
     * Pull entities into memory; can throw runtime exceptions.
     */
    private void fetchEntities() {
        try {
            final String user = hostBean.getCurrentUser();
            final String pass = hostBean.getCurrentPass();
            final String currentHost = hostBean.getCurrentHost();
            final int currentPort = hostBean.getCurrentPort();
            final String currentProtocol = hostBean.getCurrentProtocol();

            String cookie = getCookie(user, pass, currentHost, currentPort, currentProtocol);

            final ApacheWebClient webClient = new ApacheWebClient();
            BlastServiceClient client = new BlastServiceClient(
                    webClient, logger
            );
            // Need to parse them all out. Build entities collection and populate anchor length.
            String targetUrl = String.format(FETCH_BY_ID_URL_FMT, currentProtocol, currentHost, currentPort, id).replace(' ', '+');
            InputStreamSource source = client.fetchUrlContents(targetUrl, LoginCookieFetcher.FORM_SESSION_COOKIE, cookie);
            if (source != null) {
                final InputStream inputStream = source.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                BlastXmlParser parser = new BlastXmlParser(reader);

                entities = parser.getEntities();
                anchorLength = parser.getAnchorLength();
                //startOfSubRange = parser.getStartOfQueryRange();
                //endOfSubRange = parser.getEndOfQueryRange();

                if (entities == null  ||  entities.isEmpty()) {
                    return;
                }
                String queryId = parser.getQueryId();
                AnchorSourceHelper.addAnchor(id, getAnchorLength(), queryId, entities);

                reader.close();
            }
            else {
                entities = Collections.EMPTY_LIST;
                anchorLength = 0;
                logger.warning("No results returned from " + targetUrl);
            }
        } catch ( Exception ex ) {
            logger.severe("Failed to get entities for data source: " + ex.getMessage());
            ex.printStackTrace();
            throw new RuntimeException( ex );
        }
    }

    private String getCookie(String user, String pass, String currentHost, int currentPort, String currentProtocol) throws Exception {
        String cookie = null;
        if (user != null  &&  user.trim().length() > 0) {
            LoginCookieFetcher fetcher = new LoginCookieFetcher();
            fetcher.setApacheLoginLocation(currentProtocol, currentHost, currentPort, "CAV");
            cookie = fetcher.fetch(user, pass);
            logger.info("Got a cookie: " + cookie);
        }
        return cookie;
    }

}

