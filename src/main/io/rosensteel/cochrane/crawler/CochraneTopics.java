package io.rosensteel.cochrane.crawler;

import io.rosensteel.http.WebReader;
import io.rosensteel.http.WebResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class CochraneTopics {
    private WebReader webReader;
    private CochraneTopicLinks topicLinks;
    private HashMap<String, CochraneReviews> reviews = new HashMap<>();

    private boolean isDisplayingProgress = false;

    public CochraneTopics(WebReader webReader, String topicListUrl) {
        new CochraneTopics(webReader, topicListUrl, false);
    }

    public CochraneTopics(WebReader webReader, String topicListUrl, boolean isDisplayingProgress) {
        try {
            this.webReader = webReader;
            this.isDisplayingProgress = isDisplayingProgress;
            WebResult topicListPage = webReader.read(topicListUrl);
            topicLinks = topicListPage.extractData(CochraneExtractors.topicLinkExtractor);
        } catch (Exception e){
            System.err.println("Could not read site (" + topicListUrl + ")");
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }

    public Set<String> getAllTopicNames() {
        return topicLinks.availableTopics();
    }

    public CochraneReviews getReviewsForTopic(String topicName) {

        if(reviews.containsKey(topicName)) {
            if (isDisplayingProgress) {
                System.out.println("Read from cache: " + topicName);
            }
        } else {
            if (isDisplayingProgress) {
                System.out.println("Getting reviews for topic: " + topicName);
            }
            crawlTopic(topicName);
        }
        return reviews.get(topicName);
    }

    public void crawlTopic(String topicName) {
        String lookupLink = topicLinks.get(topicName) + "&resultPerPage=200";
        CochraneReviews cochraneReviews = new CochraneReviews();
        boolean firstRun = true;

        while (!lookupLink.isEmpty()) {
            WebResult topicPage = null;

            try {
                topicPage = webReader.read(lookupLink);
            } catch (IOException e) {
                System.err.println("Error - could not read site (" + lookupLink + ")");
                e.printStackTrace();
                System.exit(1);
            }

            if (firstRun) {
                cochraneReviews.setExpectedReviewCount(topicPage.extractData(CochraneExtractors.expectedReviewCountExtractor));
                firstRun = false;
            }

            cochraneReviews.addReviews(topicPage);
            lookupLink = cochraneReviews.getNextPageLink(topicPage);

            if (isDisplayingProgress) {
                System.out.println("(" + topicName + ") " + "Progress:" + cochraneReviews.reviewCount() + "/" + cochraneReviews.getExpectedReviewCount());
            }
        }

        if (isDisplayingProgress) {
            System.out.println("(" + topicName + ") " + "Finished:" + cochraneReviews.reviewCount() + "/" + cochraneReviews.getExpectedReviewCount());
            if (!cochraneReviews.gotExpectedNumberOfReviews()) {
                System.err.println("(" + topicName + ") " + "Error - did not get expected number of reviews");
                System.exit(1);
            }
        }

        reviews.put(topicName, cochraneReviews);
    }

    public CochraneReviews getCrawledReviews() {
        CochraneReviews allReviews = new CochraneReviews();
        reviews.forEach((topic, reviews) -> allReviews.addReviews(reviews));
        return allReviews;
    }
}