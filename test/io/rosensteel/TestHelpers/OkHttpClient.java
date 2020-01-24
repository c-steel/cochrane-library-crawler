package io.rosensteel.TestHelpers;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class OkHttpClient extends CloseableHttpClient {
    @Override
    protected CloseableHttpResponse doExecute(HttpHost httpHost, HttpRequest httpRequest, HttpContext httpContext) {
        return new OkResponseStub();
    }

    @Override
    public void close() {

    }

    @Override
    @Deprecated
    public HttpParams getParams() {
        return null;
    }

    @Override
    @Deprecated
    public ClientConnectionManager getConnectionManager() {
        return null;
    }
}
