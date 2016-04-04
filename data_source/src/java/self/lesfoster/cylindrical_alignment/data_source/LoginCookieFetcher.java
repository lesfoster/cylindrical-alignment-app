/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package self.lesfoster.cylindrical_alignment.data_source;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;

/**
 * Use this to communicate with the server for login purposes.
 * Created by Leslie L Foster on 9/23/2015.
 */
public class LoginCookieFetcher {

    public final static String LOGIN_URL_FMT = "%s://%s:%d/%s/j_security_check";
    public static final String FORM_SESSION_COOKIE = "JSESSIONID";
    public static final String COOKIE_RETURN_HEADER = "Set-Cookie";
    private final static String LOGIN_URL = "http://localhost:8080/CAV/j_security_check";

    private String loginUrl = LOGIN_URL;

    public void setApacheLoginLocation( String protocol, String server, int port, String context ) {
        setLoginUrl(String.format(LOGIN_URL_FMT, protocol, server, port, context));
    }

    /** Override the default local host version of the URL. */
    public void setLoginUrl( String loginUrl ) {
        this.loginUrl = loginUrl;
    }

    public String fetch(String username, String password) throws Exception {
        // Use HTTP Client mechanisms, to collect a login cookie.  That cookie
        // may then be used in subsequent visits to the site.
        String rtnVal = null;

        // Post a request.
        HttpClient httpclient = new DefaultHttpClient(getHttpParams());
        String url = loginUrl;
        HttpPost httpPost = new HttpPost(url);
        ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("j_username", username));
        nvps.add(new BasicNameValuePair("j_password", password));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));

        // Get the response, for the cookie header.
        HttpResponse response = httpclient.execute(httpPost);
        Header cookieHeader = response.getFirstHeader(COOKIE_RETURN_HEADER);
        if (cookieHeader != null) {
            rtnVal = cookieHeader.getValue();
            if (rtnVal.startsWith(FORM_SESSION_COOKIE)) {
                rtnVal = rtnVal.substring(FORM_SESSION_COOKIE.length() + 1);
            }
        }
        else {
            System.out.println(String.format("Failed login with %s/%s", username, password));
            for (Header header: response.getAllHeaders()) {
                System.out.println(header.getName() + "=" + header.getValue());
            }
        }

        if (rtnVal != null) {
            String[] cookieParts = rtnVal.split(";");
            rtnVal = cookieParts[0].trim();
        }

        return rtnVal;
    }

    private HttpParams getHttpParams() {
        HttpParams connectionParameters = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(connectionParameters, 10000);
        HttpConnectionParams.setSoTimeout(connectionParameters, 5000);
        return connectionParameters;
    }

}

