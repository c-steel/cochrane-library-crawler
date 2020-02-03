package io.rosensteel.http;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class WebResult {
    private String body;

    private Document dom;

    public WebResult(String body, String baseUri) {
        this.body = body;

        this.dom = Jsoup.parse(body);
        this.dom.setBaseUri(baseUri);
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
