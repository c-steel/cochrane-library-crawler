package io.rosensteel.Http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebResult {
    private boolean isSuccess;
    private String body;

    private Document dom;

    public WebResult(boolean isSuccess, String body, String baseUri) {
        this.isSuccess = isSuccess;
        this.body = body;

        this.dom = Jsoup.parse(body);
        this.dom.setBaseUri(baseUri);
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getBody() {
        return body;
    }

    public Document getDom() {
        return dom;
    }

    public <T> T extractData(WebDataExtractor<? extends T> extractor) {
        return extractor.extractData(this.dom);
    }

}
