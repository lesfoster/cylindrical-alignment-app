/*
 * Copyright Leslie L. Foster, 2016, all rights reserved.
 */
package self.lesfoster.cylindrical_alignment.data_source.web_client;

/**
 * Simplified wrapper around web client.
 * @author Leslie L Foster
 */
public interface WebClient {
    Response post( String url, String preFormattedParams, String contentType ) throws Exception;
    Response get( String url, String contentType ) throws Exception;
    Response get( String url, String contentType, String cookieName, String cookieValue ) throws Exception;

    public static interface Response {
        int getCode(); // Ex: 404, 200
        String getContent();
    }
}