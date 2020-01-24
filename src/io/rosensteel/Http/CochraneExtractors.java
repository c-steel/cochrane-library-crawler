package io.rosensteel.Http;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class CochraneExtractors {
    public static WebDataExtractor<HashMap<String, String>> topicLinkExtractor = dom -> {
        HashMap<String, String> links = new HashMap<>();

        Element subsection = dom.select("div:contains(Browse by topic)").last();
        Elements linkElements = subsection.select("a[href]:has(button)");
        for(Element linkElement: linkElements) {
            links.put(linkElement.text(), linkElement.attr("abs:href") + "&resultPerPage=200");
        }

        return links;
    };

    public static WebDataExtractor<HashMap<String, String>> articleLinkExtractor = dom -> {
        HashMap<String, String> links = new HashMap<>();

        Elements elements = dom.select("div h3 a[href]");
        for(Element element: elements) {
            links.put(element.text(), element.attr("abs:href"));
        }

        return links;
    };

    public static WebDataExtractor<String> nextPageExtractor = dom -> {
        String link = "";
        Elements nextButtonSelection = dom.select("a[href]:containsOwn(Next)");
        if(!nextButtonSelection.isEmpty()) {
            Element nextButton = nextButtonSelection.last();
            link = nextButton.attr("abs:href");
        }
        return link;
    };


}
