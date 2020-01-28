package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebReader;
import io.rosensteel.Http.WebResult;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class CochraneCrawler {
    private String cochraneBaseUri = "https://wwww.cochranelibrary.com";
    private WebReader webReader = new WebReader(HttpClients.createDefault(), cochraneBaseUri);


    // Structure of the page
    // Topic, Review List*, Individual Review
    // Each entry in a review list is going to take a while to visit.

    // Getting the review list of a topic and turning the review list into entries should be two separate processes.
    // Getting the topic list from the review list should also be separate
    // Let's efficiently gather the review list first.


    // Structure of my data
    // Reviews (Key, Topic, Title, Link, etc...)
    // After extraction, links do not matter.
    // And I will be able to search and group them if I want.



    public void crawl() {
        try {
            TopicLinks topicLinks = new TopicLinks(webReader, "https://www.cochranelibrary.com/cdsr/reviews/topics", true);
            String firstTopic = topicLinks.getAllTopicNames().iterator().next();
            topicLinks.getReviewLinksForTopic("Allergy & intolerance");
            CochraneReview firstReview = topicLinks.getReview("Allergy & intolerance", 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
