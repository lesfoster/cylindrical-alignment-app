/*
 * Copyright Leslie L. Foster, 2014.  All rights reserved.
 */
package self.lesfoster.cylindrical_alignment.data_source.web_client;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Mediate use of HTTP Client from Apache, to return data in standardized
 * form.
 *
 * @author Leslie L Foster
 */
public class ApacheWebClient implements WebClient {

    /**
     * This is from the quickstart page for http components.
     * See http://hc.apache.org/httpcomponents-client-ga/quickstart.html
     */
    //@Override
    public WebClient.Response post(String url, String preFormattedParams, String contentType) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        if (contentType != null) {
            Header contentTypeHeader = new BasicHeader("ContentType", contentType);
            httpPost.setHeader(contentTypeHeader);
        }
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        parseFromPreFormat( nvps, preFormattedParams );
        httpPost.setEntity( new UrlEncodedFormEntity( nvps ) );

        HttpResponse apacheResponse = httpclient.execute(httpPost);
        Response response = null;

        try {
            StatusLine statusLine = apacheResponse.getStatusLine();
            HttpEntity entity = apacheResponse.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            StringBuilder builder = consumeResponse(entity);
            response = new ApacheWebClientResponse(statusLine.getStatusCode(), builder.toString());
        } finally {
            //apacheResponse.close();
        }

        return response;
    }

    /**
     * This is from the quickstart page for http components.
     *  http://hc.apache.org/httpcomponents-client-ga/quickstart.html
     *
     * @param url what to read for data.
     * @return filled out response bean as expected by caller.
     * @throws Exception
     */
    //@Override
    public Response get(String url, String contentType) throws Exception {
        return get(url, contentType, null, null);
    }

    /**
     * This is from the quickstart page for http components.
     *  http://hc.apache.org/httpcomponents-client-ga/quickstart.html
     *
     * @param url what to read for data.
     * @param contentType optional.
     * @param cookieName, cookieValue for login.
     * @return filled out response bean as expected by caller.
     * @throws Exception
     */
    //@Override
    public Response get(String url, String contentType, String cookieName, String cookieValue) throws Exception {
        Response response = null;
        HttpClient httpclient = new DefaultHttpClient(getHttpParams());
        HttpGet httpGet = new HttpGet(url);
        if (contentType != null) {
            Header contentTypeHeader = new BasicHeader("ContentType", contentType);
            httpGet.setHeader(contentTypeHeader);
        }
        if (cookieName != null  &&  cookieValue != null) {
            httpGet.setHeader("Cookie", cookieName + "=" + cookieValue);
        }
        HttpResponse apacheResponse = httpclient.execute(httpGet);

        try {
            StatusLine statusLine = apacheResponse.getStatusLine();
            HttpEntity entity = apacheResponse.getEntity();
            // do something useful with the response body
            // and ensure it is fully consumed
            //EntityUtils.consume(entity);
            StringBuilder builder = consumeResponse(entity);

            response = new ApacheWebClientResponse(statusLine.getStatusCode(), builder.toString());
        } finally {
            //apacheResponse.close();
        }
        return response;
    }

    private StringBuilder consumeResponse(HttpEntity entity) throws IllegalStateException, IOException {
        StringBuilder builder = new StringBuilder();
        String inline = null;
        InputStream inStr = entity.getContent();
        if (inStr != null) {
            BufferedReader br = new BufferedReader(new InputStreamReader(inStr));
            while (null != (inline = br.readLine())) {
                builder.append(inline);
                builder.append("\n");
            }
            br.close();
        }
        return builder;
    }

    private void parseFromPreFormat( List<NameValuePair> nvps, String urlPreFormatted ) {
        String[] nameValues = urlPreFormatted.split("&");

        for ( String nameValue: nameValues ) {
            String[] nameValArr = nameValue.split("=");
            nvps.add( new BasicNameValuePair( nameValArr[ 0 ], nameValArr[ 1 ] ) );
        }
    }

    public static class ApacheWebClientResponse implements WebClient.Response {

        private int code;
        private String content;
        public ApacheWebClientResponse( int code, String content ) {
            this.code = code;
            this.content = content;
        }

        //@Override
        public int getCode() {
            return code;
        }

        //@Override
        public String getContent() {
            return content;
        }

    }

    private HttpParams getHttpParams() {
        HttpParams connectionParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(connectionParameters, 10000);
        HttpConnectionParams.setSoTimeout(connectionParameters, 10000);
        return connectionParameters;
    }

}

