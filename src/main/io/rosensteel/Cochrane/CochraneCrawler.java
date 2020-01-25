package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebReader;
import io.rosensteel.Http.WebResult;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;

class OnTopicFinishedListener {
    private HashMap<String, HashMap<String, String>> topicReviewMap;

    public OnTopicFinishedListener(HashMap<String, HashMap<String, String>> topicReviewMap){
        this.topicReviewMap = topicReviewMap;
    };

    public void onTopicFinished(String topicName,  HashMap<String, String> reviewLinkMap) {
        this.topicReviewMap.put(topicName, reviewLinkMap);
    };
}


public class CochraneCrawler {
    private String cochraneBaseUri = "https://wwww.cochranelibrary.com";
    private WebReader webReader = new WebReader(HttpClients.createDefault(), cochraneBaseUri);

    public void crawl() {
        HashMap<String, HashMap<String, String>> topicReviewMap = new HashMap<>();
        OnTopicFinishedListener onTopicFinishedListener = new OnTopicFinishedListener(topicReviewMap);

        try {
            WebResult topicListPage = webReader.read("https://www.cochranelibrary.com/cdsr/reviews/topics");
            HashMap<String, String> topicLinkMap = topicListPage.extractData(CochraneExtractors.topicLinkExtractor);

            topicLinkMap.forEach((topicName, topicLink) -> {
                new Thread(() -> {
                    try {
                        HashMap<String, String> reviewLinkMap = new HashMap<>();
                        String modifiedLink = topicLink + "&resultPerPage=200";
                        while(!modifiedLink.isEmpty()) {
                            WebResult topicPage = webReader.read(modifiedLink);
                            reviewLinkMap.putAll(topicPage.extractData(CochraneExtractors.reviewLinkExtractor));
                            modifiedLink = topicPage.extractData(CochraneExtractors.nextPageLinkExtractor);
                            // Todo Delete
                            System.out.println(topicName + ", " + reviewLinkMap.size());
                        }
                        // Todo Delete
                        System.out.println("Finished --->" + topicName + ", " + reviewLinkMap.size());
                        onTopicFinishedListener.onTopicFinished(topicName, reviewLinkMap);
                    } catch (IOException ex) {
                        System.err.println("Failure while attempting to read topic page");
                        ex.printStackTrace();
                        System.exit(1);
                    }
                }).start();
            });

            topicReviewMap.forEach((topic, reviewLinkMap) -> {
                System.out.println(topic);
                reviewLinkMap.forEach((reviewName, reviewLink) -> {
                    System.out.println(reviewName + ", " + reviewLink);
                });
            });

            System.out.println(topicLinkMap);

        } catch (IOException ex){
            System.err.println("Failure while attempting to read Cochrane Library");
            ex.printStackTrace();
            System.exit(1);
        }

    }

}
