package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebReader;
import io.rosensteel.Http.WebResult;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

public class TopicLinks {
    private WebReader webReader;
    private HashMap<String, String> nameToLinkMap;
    private HashMap<String, ReviewLinks> nameToReviewMap = new HashMap<>();

    private boolean reportProgress = false;

    public TopicLinks(WebReader webReader, String topicListUrl) throws IOException {
        new TopicLinks(webReader, topicListUrl, false);
    }

    public TopicLinks(WebReader webReader, String topicListUrl, boolean reportProgress) throws IOException {
        this.webReader = webReader;
        this.reportProgress = reportProgress;
        WebResult topicListPage = webReader.read(topicListUrl);
        nameToLinkMap = topicListPage.extractData(CochraneExtractors.topicLinkExtractor);
    }

    public Set<String> getAllTopicNames() {
        return nameToLinkMap.keySet();
    }

    public void getReviewLinksForTopic(String topicName) throws IOException {
        ReviewLinks reviewLinks = new ReviewLinks();

        if(!nameToLinkMap.containsKey(topicName))
            throw new IllegalArgumentException("TopicLinks is missing topic:" + topicName);

        if(reportProgress){
            System.out.println("Review links for topic: " + topicName);
        }

        String lookupLink = nameToLinkMap.get(topicName) + "&resultPerPage=200";
        boolean firstRun = true;

        while(!lookupLink.isEmpty()) {
            WebResult reviewLinksPage = webReader.read(lookupLink);
            if(firstRun) {
                reviewLinks.setExpectedLinkCount(reviewLinksPage);
                firstRun = false;
            }
            reviewLinks.addReviewLinks(reviewLinksPage);
            lookupLink = reviewLinks.getNextPageLink(reviewLinksPage);

            if(reportProgress){
                System.out.println("(" + topicName + ") " + "Progress:" + reviewLinks.getLinkCount() + "/" + reviewLinks.getExpectedLinkCount());
            }
        }

        if(reportProgress){
            System.out.println("(" + topicName + ") "  + "Finished:" + reviewLinks.getLinkCount() + "/" + reviewLinks.getExpectedLinkCount());
            if(!reviewLinks.gotExpectedNumberOfReviews()) {
                System.out.println("(" + topicName + ") "  + "Problem!  Did not get expected number of reviews");
            }
        }

        nameToReviewMap.put(topicName, reviewLinks);
    }

    public CochraneReview getReview(String topicName, int reviewIndex) throws IOException {
        if(!nameToLinkMap.containsKey(topicName))
            throw new IllegalArgumentException("TopicLinks is missing topic:" + topicName);
        if(!nameToReviewMap.containsKey(topicName)) {
            getReviewLinksForTopic(topicName);
        }
        return nameToReviewMap.get(topicName).getReview(reviewIndex);
    }

}
