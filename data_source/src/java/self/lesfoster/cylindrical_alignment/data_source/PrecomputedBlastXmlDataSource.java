/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source;

import self.lesfoster.cylindrical_alignment.data_source.blast_xml.BlastServiceClient;
import self.lesfoster.cylindrical_alignment.data_source.blast_xml.BlastXmlParser;
import self.lesfoster.cylindrical_alignment.data_source.web_client.ApacheWebClient;
import self.lesfoster.cylindrical_alignment.input_stream_source.InputStreamSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import self.lesfoster.cylindrical_alignment.data_source.web_client.HostBean;

/**
 * Fetches hits that have been precomputed, and remain at the ready.  Fetches by ID.  Searches by date.
 * Created by Leslie L Foster on 11/23/2015.
 */
public class PrecomputedBlastXmlDataSource implements DataSource, Serializable {
    private final Logger logger = Logger.getLogger(PrecomputedBlastXmlDataSource.class.getName());

    private final static String FETCH_BY_ID_URL_FMT = "%s://%s:%d/CAV/results/getResultById?id=%s";

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

