package io.rosensteel.Cochrane;

import io.rosensteel.Http.WebReader;
import io.rosensteel.Http.WebResult;
import io.rosensteel.TestHelpers.OkHttpClientStub;
import io.rosensteel.TestHelpers.SampleHtml;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;

import static org.jsoup.helper.Validate.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CochraneExtractorsTest {
    private String cochraneBaseUri = "https://www.cochranelibrary.com";

    @Test
    public void topicLinkExtractor_shouldReturnTopicsMappedToLinks() {
        WebReader webReader = new WebReader(new OkHttpClientStub(SampleHtml.cochraneTopicsList), cochraneBaseUri);
        String expectedTopicLink = "https://www.cochranelibrary.com/en/search?p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&_scolarissearchresultsportlet_WAR_scolarissearchresults_displayText=Lungs+%26+airways&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchText=Lungs+%26+airways&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchType=basic&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryField=topic_id&_scolarissearchresultsportlet_WAR_scolarissearchresults_searchBy=13&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetDisplayName=Lungs+%26+airways&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetQueryTerm=z1209270536574680511905632880005&_scolarissearchresultsportlet_WAR_scolarissearchresults_facetCategory=Topics";

        try {
            WebResult webResult = webReader.read("www.does-not-matter.com");
            CochraneTopicLinks topicLinks = webResult.extractData(CochraneExtractors.topicLinkExtractor);
            assertEquals(36, topicLinks.numTopics());
            assertEquals(expectedTopicLink, topicLinks.get("Lungs & airways"));
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }


    @Test
    public void nextPageExtractor_shouldReturnLinkToNextPage() {
        WebReader webReader = new WebReader(new OkHttpClientStub(SampleHtml.cochraneReviewList), cochraneBaseUri);
        String expectedNextPageLink = "https://www.cochranelibrary.com/en/search?min_year=&max_year=&custom_min_year=&custom_max_year=&searchBy=13&searchText=Lungs+%26+airways&selectedType=review&isWordVariations=&resultPerPage=200&searchType=basic&orderBy=relevancy&publishDateTo=&publishDateFrom=&publishYearTo=&publishYearFrom=&displayText=Lungs+%26+airways&forceTypeSelection=true&facetDisplayName=Lungs+%26+airways&facetQueryTerm=z1209270536574680511905632880005&facetQueryField=topic_id&facetCategory=Topics&p_p_id=scolarissearchresultsportlet_WAR_scolarissearchresults&p_p_lifecycle=0&p_p_state=normal&p_p_mode=view&p_p_col_id=column-1&p_p_col_count=1&cur=2";

        try {
            WebResult webResult = webReader.read("www.does-not-matter.com");
            String actualNextPageLink = webResult.extractData(CochraneExtractors.nextPageLinkExtractor);
            assertEquals(expectedNextPageLink, actualNextPageLink);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void expectedReviewCountExtractor_shouldReturnTotalNumberOfReviews() {
        WebReader webReader = new WebReader(new OkHttpClientStub(SampleHtml.cochraneReviewList), cochraneBaseUri);
        Integer expectedNumberOfReviews = 825;

        try {
            WebResult webResult = webReader.read("www.does-not-matter.com");
            Integer reviewCount = webResult.extractData(CochraneExtractors.expectedReviewCountExtractor);
            assertEquals(expectedNumberOfReviews, reviewCount);
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void reviewExtractor_shouldReturnListOfReviews() {
        WebReader webReader = new WebReader(new OkHttpClientStub(SampleHtml.cochraneReviewList), cochraneBaseUri);

        try {
            WebResult webResult = webReader.read("www.does-not-matter.com");
            ArrayList<CochraneReview> reviews = webResult.extractData(CochraneExtractors.reviewExtractor);

            assertEquals(200, reviews.size());

            assertEquals(expectedReviews.get(0), reviews.get(0));
            assertEquals(expectedReviews.get(100), reviews.get(100));
            assertEquals(expectedReviews.get(199), reviews.get(199));

        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    private static HashMap<Integer, CochraneReview> expectedReviews;
    static {
        expectedReviews = new HashMap<>();
        expectedReviews.put(0,
                new CochraneReview("https://www.cochranelibrary.com/cdsr/doi/10.1002/14651858.CD001146.pub5/full",
                "Lungs & airways",
                "Early (< 8 days) systemic postnatal corticosteroids for prevention of bronchopulmonary dysplasia in preterm infants",
                "Lex W Doyle, Jeanie L Cheong, Richard A Ehrenkranz, Henry L Halliday",
                "24 October 2017"));
        expectedReviews.put(100,
                new CochraneReview("https://www.cochranelibrary.com/cdsr/doi/10.1002/14651858.CD008578.pub3/full",
                        "Lungs & airways",
                        "Chinese medicinal herbs for mumps",
                        "Min Shu, Yi Qiong Zhang, Zhiyao Li, Guan J Liu, Chaomin Wan, Yang Wen",
                        "18 April 2015"));
        expectedReviews.put(199,
                new CochraneReview("https://www.cochranelibrary.com/cdsr/doi/10.1002/14651858.CD003086.pub3/full",
                        "Lungs & airways",
                        "Opioid antagonists for smoking cessation",
                        "Sean P David, Tim Lancaster, Lindsay F Stead, A. Eden Evins, Judith J Prochaska",
                        "6 June 2013"));
    }


}
