package io.rosensteel.cochrane.cli;

import io.rosensteel.cochrane.crawler.CochraneCrawler;
import io.rosensteel.cochrane.crawler.CochraneReviews;
import org.apache.commons.cli.*;

import java.util.ArrayList;

public class Main {
    static CochraneCrawler crawler = new CochraneCrawler();
    static ArrayList<String> topics = new ArrayList<>(crawler.listAvailableTopics());

    public static void main(String[] args) {
        try {
            CrawlerSettings settings = parseArgs(args);

            if(settings.isDisplayHelp()) {
                displayHelp();
            } else {
                for(Integer topicNumber: settings.getTopicNumbers()) {
                    crawler.crawlTopic(topics.get(topicNumber));
                }
                CochraneReviews reviews = crawler.getCrawledReviews();
                reviews.saveFile(settings.getFileOutput());
            }
        } catch (Exception e) {
            System.err.println("Problem parsing command line arguments.");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static CrawlerSettings parseArgs(String[] args) throws Exception {

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(getOptions(), args);

        if(!cmd.hasOption("t") || !cmd.hasOption("o") || cmd.hasOption("h")){
            return new CrawlerSettings(true);
        } else {
            return new CrawlerSettings(cmd.getOptionValue("t"), cmd.getOptionValue("o"), topics.size());
        }
    }

    private static Options getOptions(){
        Options options = new Options();
        options.addOption("t", true, "[Required] - Input topic numbers to crawl.  Comma delimited.  Use dash for ranges.  [-t 0-5,10,24]");
        options.addOption("o", true, "[Required] - Output filename. [-o ./cochrane_reviews.txt]");
        options.addOption("h", false, "Display this help information");
        return options;
    }

    private static void displayHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar cochrane-library-crawler", getOptions());
        displayTopicMenu();
    }

    private static void displayTopicMenu () {
        System.out.println("Available topics....");
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

    private static void writeReviewsFile (int topicNumber, String filename) {
        String topicName = topics.get(topicNumber);
        CochraneReviews reviews = crawler.getReviewsForTopic(topicName);
        reviews.saveFile(filename);
    }

}
