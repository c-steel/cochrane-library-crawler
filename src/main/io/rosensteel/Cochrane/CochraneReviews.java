package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebResult;

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

}
