package io.rosensteel.cochrane.crawler;

import io.rosensteel.http.WebResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CochraneReviews {
    private Integer expectedReviewCount = 0;
    private ArrayList<CochraneReview> reviews = new ArrayList<>();


    public ArrayList<CochraneReview> getReviews() {
        return reviews;
    }


    public void addReviews(WebResult reviewListPage) {
        reviews.addAll(reviewListPage.extractData(CochraneExtractors.reviewExtractor));
    }

    public void addReviews(CochraneReviews newReviews) {
        reviews.addAll(newReviews.getReviews());
        expectedReviewCount += newReviews.getExpectedReviewCount();
    }

    public void setExpectedReviewCount(Integer expectedReviewCount) {
        this.expectedReviewCount = expectedReviewCount;
    }

    public String getNextPageLink(WebResult reviewListPage) {
        return reviewListPage.extractData(CochraneExtractors.nextPageLinkExtractor);
    }

    public Integer reviewCount() {
        return reviews.size();
    }

    public Integer getExpectedReviewCount() {
        return expectedReviewCount;
    }

    public boolean gotExpectedNumberOfReviews() {
        return expectedReviewCount.equals(reviewCount());
    }

    public CochraneReview getReview(int index) {
        return reviews.get(index);
    }

    public void saveFile(String filename) {
        try {
            FileWriter out = new FileWriter(filename);
            for (CochraneReview review : reviews) {
                out.write(review.toPipeDelimited());
                out.write("\n");
            }

            out.close();
        } catch (IOException e) {
            System.err.println("Couldn't open file (" + filename + ")");
        }
    }

}
