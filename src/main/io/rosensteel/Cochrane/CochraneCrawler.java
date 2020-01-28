package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebReader;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.Set;

public class CochraneCrawler {
    private String cochraneBaseUri = "https://wwww.cochranelibrary.com";
    private String topicsUrl = "https://www.cochranelibrary.com/cdsr/reviews/topics";
    private WebReader webReader = new WebReader(HttpClients.createDefault(), cochraneBaseUri);
    private boolean reportProgress = true;
    CochraneTopics topicLinks = null;

    public Set<String> listAvailableTopics() {
        readTopicsIfNull();
        return topicLinks.getAllTopicNames();
    }

    public CochraneReviews getReviewsForTopic(String topicName) {
        readTopicsIfNull();
        return topicLinks.getReviewsForTopic(topicName);
    }

    private void readTopicsIfNull() {
        if(topicLinks == null) {
            topicLinks = new CochraneTopics(webReader, topicsUrl, reportProgress);
        }
    }

}
