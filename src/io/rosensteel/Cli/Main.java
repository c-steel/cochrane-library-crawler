package io.rosensteel.Cli;

import io.rosensteel.Http.CochraneExtractors;
import io.rosensteel.Http.WebReader;
import io.rosensteel.Http.WebResult;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            WebReader webReader = new WebReader(HttpClients.createDefault(), "https://wwww.cochranelibrary.com");
            WebResult mainPage = webReader.read("https://www.cochranelibrary.com/cdsr/reviews/topics");
            if(mainPage.isSuccess()){
                HashMap<String, String> topicLinks = mainPage.extractData(CochraneExtractors.topicLinkExtractor);
                topicLinks.forEach((topic, topicPageLink) -> {
                    System.out.println(topic + " -> " + topicPageLink);
                    try {
                        WebResult topicPage = webReader.read(topicPageLink);
                        String nextPageLink = topicPage.extractData(CochraneExtractors.nextPageExtractor);
                        HashMap<String, String> articleLinks = topicPage.extractData(CochraneExtractors.articleLinkExtractor);
                        System.out.println(articleLinks.size());
                        while(!nextPageLink.isEmpty()) {
                            topicPage = webReader.read(nextPageLink);
                            articleLinks.putAll(topicPage.extractData(CochraneExtractors.articleLinkExtractor));
                            nextPageLink = topicPage.extractData(CochraneExtractors.nextPageExtractor);
                            System.out.println(articleLinks.size());
                        }
                        System.out.println("Finished.");
                    } catch (IOException e) {
                        System.err.println("Error reading topic page in Cochrane Library.");
                        System.err.println(e.getMessage());
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            System.err.println("Error reading main Cochrane Library page.");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
