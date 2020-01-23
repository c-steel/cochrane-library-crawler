package io.rosensteel.HTTP;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.HeaderGroup;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WebReader {

    private HttpClient client;

    public WebReader(HttpClient client) {
        this.client = client;
    }

    public WebResult read(String url) throws IOException {
        WebResult webResult;

        HttpGet request = makeDefaultRequest(url);
        webResult = client.execute(request, responseHandler);

        return webResult;
    }

    private static HttpGet makeDefaultRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "text/html");
        request.setHeader("Accept-Language", "en-US,en");
        request.setHeader("User-Agent", "Rosensteel/0.01");
        return request;
    }

    private static ResponseHandler<WebResult> responseHandler = httpResponse -> {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = httpResponse.getEntity();
            return new WebResult(true, EntityUtils.toString(entity));
        } else {
            return new WebResult(false, "");
        }
    };

}
