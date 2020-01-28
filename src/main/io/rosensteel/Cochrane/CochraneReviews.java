package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebResult;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class CochraneReviews {
    private Integer expectedReviewCount;
    private ArrayList<CochraneReview> reviews = new ArrayList<>();

    public void addReviews(WebResult reviewListPage) {
        reviews.addAll(reviewListPage.extractData(CochraneExtractors.reviewExtractor));
    }

    public void setExpectedReviewCount(WebResult reviewListPage) {
        this.expectedReviewCount = reviewListPage.extractData(CochraneExtractors.expectedReviewCountExtractor);
    }

    public String getNextPageLink(WebResult reviewListPage) {
        return reviewListPage.extractData(CochraneExtractors.nextPageLinkExtractor);
    }

    public ArrayList<CochraneReview> getAllLinks() {
        return reviews;
    }

    public Integer reviewCount() {
        return reviews.size();
    }

    public Integer expectedReviewCount() {
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
