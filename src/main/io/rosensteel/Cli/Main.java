package io.rosensteel.Cli;

import io.rosensteel.Cochrane.CochraneCrawler;
import io.rosensteel.Cochrane.CochraneReviews;

import java.util.ArrayList;

public class Main {
    static CochraneCrawler crawler = new CochraneCrawler();
    static ArrayList<String> topics = new ArrayList<>(crawler.listAvailableTopics());

    public static void main(String[] args) {
        displayReview(10, 0);
        displayReview(10, 20);
        displayReview(10, 124);
    }

    private static void displayTopicMenu () {
        for(int i = 0; i < topics.size(); i++) {
            System.out.println(i + ". " + topics.get(i));
        }
    }

    private static void displayReviews (int topicNumber) {
        String topicName = topics.get(topicNumber);
        CochraneReviews reviews = crawler.getReviewsForTopic(topicName);
        for(int i = 0; i < reviews.reviewCount(); i++) {
            System.out.println(i + ". " + reviews.getReview(i).getTitle());
        }
    }

    private static void displayReview(int topicNumber, int reviewNumber) {
        String topicName = topics.get(topicNumber);
        CochraneReviews reviews = crawler.getReviewsForTopic(topicName);
        System.out.println(reviews.getReview(reviewNumber));
    }

}
