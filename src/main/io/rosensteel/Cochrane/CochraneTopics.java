package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebReader;
import io.rosensteel.Http.WebResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class CochraneTopics {
    private WebReader webReader;
    private HashMap<String, String> topicLinks;
    private HashMap<String, CochraneReviews> reviews = new HashMap<>();

    private boolean isDisplayingProgress = false;

    public CochraneTopics(WebReader webReader, String topicListUrl) throws IOException {
        new CochraneTopics(webReader, topicListUrl, false);
    }

    public CochraneTopics(WebReader webReader, String topicListUrl, boolean isDisplayingProgress) throws IOException {
        this.webReader = webReader;
        this.isDisplayingProgress = isDisplayingProgress;
        WebResult topicListPage = webReader.read(topicListUrl);
        topicLinks = topicListPage.extractData(CochraneExtractors.topicLinkExtractor);
    }

    public Set<String> getAllTopicNames() {
        return topicLinks.keySet();
    }

    public CochraneReviews getReviewsForTopic(String topicName) throws IOException {
        CochraneReviews cochraneReviews = new CochraneReviews();

        if(!topicLinks.containsKey(topicName))
            throw new IllegalArgumentException("TopicLinks is missing topic:" + topicName);
        if(reviews.containsKey(topicName)) {
            if (isDisplayingProgress) {
                System.out.println("Read from cache: " + topicName);
            }
            return reviews.get(topicName);
        } else {
            if (isDisplayingProgress) {
                System.out.println("Reading Reviews for topic: " + topicName);
            }
            String lookupLink = topicLinks.get(topicName) + "&resultPerPage=200";
            boolean firstRun = true;
            while (!lookupLink.isEmpty()) {
                WebResult topicPage = webReader.read(lookupLink);
                if (firstRun) {
                    cochraneReviews.setExpectedLinkCount(topicPage);
                    firstRun = false;
                }
                cochraneReviews.addReviews(topicPage);
                lookupLink = cochraneReviews.getNextPageLink(topicPage);

                if (isDisplayingProgress) {
                    System.out.println("(" + topicName + ") " + "Progress:" + cochraneReviews.getLinkCount() + "/" + cochraneReviews.getExpectedLinkCount());
                }
            }
            if (isDisplayingProgress) {
                System.out.println("(" + topicName + ") " + "Finished:" + cochraneReviews.getLinkCount() + "/" + cochraneReviews.getExpectedLinkCount());
                if (!cochraneReviews.gotExpectedNumberOfReviews()) {
                    System.out.println("(" + topicName + ") " + "Problem!  Did not get expected number of reviews");
                }
            }
            reviews.put(topicName, cochraneReviews);
            return cochraneReviews;
        }
    }

    public CochraneReview getReview(String topicName, int reviewIndex) throws IOException {
        if(!topicLinks.containsKey(topicName))
            throw new IllegalArgumentException("TopicLinks is missing topic:" + topicName);
        if(!reviews.containsKey(topicName)) {
            getReviewsForTopic(topicName);
        }
        return reviews.get(topicName).getReview(reviewIndex);
    }

}
