package io.rosensteel.HTTP;

import org.apache.http.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Locale;

class OkResponseStub implements CloseableHttpResponse {

    public OkResponseStub() {

    }

    @Override
    public void close()  {

    }

    @Override
    public StatusLine getStatusLine() {
        return new BasicStatusLine(new ProtocolVersion("Stub",0,1), 200, "OK");
    }

    @Override
    public void setStatusLine(StatusLine statusLine) {

    }

    @Override
    public void setStatusLine(ProtocolVersion protocolVersion, int i) {

    }

    @Override
    public void setStatusLine(ProtocolVersion protocolVersion, int i, String s) {

    }

    @Override
    public void setStatusCode(int i)  {

    }

    @Override
    public void setReasonPhrase(String s) throws IllegalStateException {

    }

    @Override
    public HttpEntity getEntity() {
        BasicHttpEntity entity = new BasicHttpEntity();
        entity.setContent(new ByteArrayInputStream("SampleText".getBytes()));
        return entity;
    }

    @Override
    public void setEntity(HttpEntity httpEntity) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

    @Override
    public void setLocale(Locale locale) {

    }

    @Override
    public ProtocolVersion getProtocolVersion() {
        return null;
    }

    @Override
    public boolean containsHeader(String s) {
        return false;
    }

    @Override
    public Header[] getHeaders(String s) {
        return new Header[0];
    }

    @Override
    public Header getFirstHeader(String s) {
        return null;
    }

    @Override
    public Header getLastHeader(String s) {
        return null;
    }

    @Override
    public Header[] getAllHeaders() {
        return new Header[0];
    }

    @Override
    public void addHeader(Header header) {

    }

    @Override
    public void addHeader(String s, String s1) {

    }

    @Override
    public void setHeader(Header header) {

    }

    @Override
    public void setHeader(String s, String s1) {

    }

    @Override
    public void setHeaders(Header[] headers) {

    }

    @Override
    public void removeHeader(Header header) {

    }

    @Override
    public void removeHeaders(String s) {

    }

    @Override
    public HeaderIterator headerIterator() {
        return null;
    }

    @Override
    public HeaderIterator headerIterator(String s) {
        return null;
    }

    @Override
    @Deprecated
    public HttpParams getParams() {
        return null;
    }

    @Override
    @Deprecated
    public void setParams(HttpParams httpParams) {

    }
}
