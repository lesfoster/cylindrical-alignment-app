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

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import org.apache.http.impl.client.HttpClientBuilder;

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
		HttpClient httpclient = HttpClientBuilder.create().build();
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

}

