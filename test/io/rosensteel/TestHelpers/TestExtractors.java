package io.rosensteel.TestHelpers;

import io.rosensteel.Http.WebDataExtractor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;

public class TestExtractors {
    public static WebDataExtractor<HashMap<String, String>> basicLinkExtractor = dom -> {
        HashMap<String, String> links = new HashMap<>();

        Elements linkElements = dom.select("a[href]");
        for(Element linkElement: linkElements) {
            links.put(linkElement.text(), linkElement.attr("abs:href"));
        }

        return links;
    };
}
