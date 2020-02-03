package io.rosensteel.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WebReader {

    private HttpClient client;
    private String baseUri;

    public WebReader(HttpClient client, String baseUri) {
        this.client = client;
        this.baseUri = baseUri;
    }

    public WebResult read(String url) throws IOException {
        HttpGet request = makeDefaultRequest(url);
        return client.execute(request, webResultHandler);
    }

    private HttpGet makeDefaultRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "text/html");
        request.setHeader("Accept-Language", "en-US,en");
        request.setHeader("User-Agent", "Rosensteel/0.01");
        return request;
    }

    private ResponseHandler<WebResult> webResultHandler = httpResponse -> {
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            HttpEntity entity = httpResponse.getEntity();
            return new WebResult(EntityUtils.toString(entity), baseUri);
        } else {
            throw new HttpResponseException(statusCode, httpResponse.getStatusLine().getReasonPhrase());
        }
    };

}
