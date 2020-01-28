package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebDataExtractor;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class CochraneExtractors {

    public static WebDataExtractor<HashMap<String, String>> topicLinkExtractor = dom -> {
        HashMap<String, String> nameToLinkMap = new HashMap<>();

        Element subsection = dom.select("div:contains(Browse by topic)").last();
        Elements linkElements = subsection.select("a[href]:has(button)");
        for(Element linkElement: linkElements) {
            nameToLinkMap.put(linkElement.text(), linkElement.attr("abs:href"));
        }

        return nameToLinkMap;
    };

    public static WebDataExtractor<ArrayList<CochraneReview>> reviewExtractor = dom -> {
        ArrayList<CochraneReview> reviews = new ArrayList<>();

        Elements elements = dom.select("div.search-results-item");
        for(Element element: elements) {
            String url = element.selectFirst("h3.result-title a").absUrl("href");
            String title = element.selectFirst("h3.result-title").text();
            String topic = dom.selectFirst("span#searchResultText").text();
            String author = element.selectFirst("div.search-result-authors").text();
            String date = element.selectFirst("div.search-result-date").text();
            reviews.add(new CochraneReview(url, topic, title, author, date));
        }

        return reviews;
    };

    public static WebDataExtractor<String> nextPageLinkExtractor = dom -> {
        String link = "";
        Elements nextButtonSelection = dom.select("a[href]:containsOwn(Next)");
        if(!nextButtonSelection.isEmpty()) {
            Element nextButton = nextButtonSelection.last();
            link = nextButton.attr("abs:href");
        }
        return link;
    };

    public static WebDataExtractor<Integer> expectedReviewCountExtractor = dom -> {
        Element resultsCount = dom.selectFirst("span.results-number");
        return Integer.parseInt(resultsCount.text());
    };


}
