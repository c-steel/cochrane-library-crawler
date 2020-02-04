package io.rosensteel.cochrane.crawler;

import io.rosensteel.http.WebReader;
import org.apache.http.impl.client.HttpClients;

import java.util.Set;

public class CochraneCrawler {
    private String cochraneBaseUri = "https://wwww.cochranelibrary.com";
    private String topicsUrl = "https://www.cochranelibrary.com/cdsr/reviews/topics";
    private WebReader webReader = new WebReader(HttpClients.createDefault(), cochraneBaseUri);
    CochraneTopics topicLinks = new CochraneTopics(webReader, topicsUrl);

    public Set<String> listAvailableTopics() {
        return topicLinks.getAllTopicNames();
    }

    public CochraneReviews getReviewsForTopic(String topicName) {
        return topicLinks.getReviewsForTopic(topicName);
    }

    public void crawlTopic(String topicName) {
        topicLinks.crawlTopic(topicName);
    }

    public CochraneReviews getCrawledReviews() {
        return topicLinks.getCrawledReviews();
    }

}
