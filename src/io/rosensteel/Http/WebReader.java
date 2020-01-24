package io.rosensteel.Http;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class WebReader {

    private HttpClient client;
    String baseUri;

    public WebReader(HttpClient client, String baseUri) {
        this.client = client;
        this.baseUri = baseUri;
    }

    public WebResult read() throws IOException {
        return read(baseUri);
    }

    public WebResult read(String url) throws IOException {
        WebResult webResult;

        HttpGet request = makeDefaultRequest(url);
        webResult = client.execute(request, responseHandler);

        return webResult;
    }

    private HttpGet makeDefaultRequest(String url) {
        HttpGet request = new HttpGet(url);
        request.setHeader("Accept", "text/html");
        request.setHeader("Accept-Language", "en-US,en");
        request.setHeader("User-Agent", "Rosensteel/0.01");
        return request;
    }

    private ResponseHandler<WebResult> responseHandler = httpResponse -> {
        int status = httpResponse.getStatusLine().getStatusCode();
        if (status == 200) {
            HttpEntity entity = httpResponse.getEntity();
            return new WebResult(true, EntityUtils.toString(entity), baseUri);
        } else {
            return new WebResult(false, "", baseUri);
        }
    };

}
