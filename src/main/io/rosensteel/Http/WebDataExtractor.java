package io.rosensteel.Http;

import org.jsoup.nodes.Document;

public interface WebDataExtractor<T> {
    T extractData(Document dom);
}
