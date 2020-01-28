package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebResult;

import java.util.ArrayList;

public class ReviewLinks {
    private Integer expectedLinkCount;
    private ArrayList<CochraneReview> reviews = new ArrayList<>();

    public void addReviewLinks(WebResult reviewListPage) {
        reviews.addAll(reviewListPage.extractData(CochraneExtractors.reviewExtractor));
    }

    public void setExpectedLinkCount(WebResult reviewListPage) {
        this.expectedLinkCount = reviewListPage.extractData(CochraneExtractors.expectedReviewCountExtractor);
    }

    public String getNextPageLink(WebResult reviewListPage) {
        return reviewListPage.extractData(CochraneExtractors.nextPageLinkExtractor);
    }

    public ArrayList<CochraneReview> getAllLinks() {
        return reviews;
    }

    public Integer getLinkCount() {
        return reviews.size();
    }

    public Integer getExpectedLinkCount() {
        return expectedLinkCount;
    }

    public boolean gotExpectedNumberOfReviews() {
        return expectedLinkCount.equals(getLinkCount());
    }

    public CochraneReview getReview(int index) {
        return reviews.get(index);
    }

}
