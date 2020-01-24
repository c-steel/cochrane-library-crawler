package io.rosensteel.Http;

import io.rosensteel.TestHelpers.BadRequestHttpClientStub;
import io.rosensteel.TestHelpers.OkHttpClient;
import io.rosensteel.TestHelpers.TestExtractors;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class WebReaderTest {

    String baseUri = "https://www.example.com";

    @Test
    public void WebResult_ShouldNotBeSuccess_WhenResponseHasBadRequestStatus(){
        WebReader reader = new WebReader(new BadRequestHttpClientStub(), baseUri);

        try {
            WebResult result = reader.read();
            assertFalse(result.isSuccess());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldContainBodyWithEmptyString_WhenResponseHasBadRequestStatus(){
        WebReader reader = new WebReader(new BadRequestHttpClientStub(), baseUri);

        try {
            WebResult result = reader.read();
            assertEquals(0, result.getBody().length());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldBeSuccess_WhenResponseHasOkStatus(){
        WebReader reader = new WebReader(new OkHttpClient(), baseUri);

        try {
            WebResult result = reader.read();
            assertTrue(result.isSuccess());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldContainBodyWithText_WhenResponseHasOkStatus(){
        WebReader reader = new WebReader(new OkHttpClient(), baseUri);

        try {
            WebResult result = reader.read();
            assertTrue(result.getBody().length() > 0, "Expected body text length greater than zero.");
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldContainPopulatedDom_WhenResponseHasOkStatus(){
        WebReader reader = new WebReader(new OkHttpClient(),baseUri);

        try {
            WebResult result = reader.read();
            Document dom = result.getDom();
            assertTrue(result.getDom().hasText());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldParseAbsoluteLinks_WhenResponseHasOkStatus(){
        WebReader reader = new WebReader(new OkHttpClient(),baseUri);

        try {
            WebResult result = reader.read();
            HashMap<String, String> links = result.extractData(TestExtractors.basicLinkExtractor);
            assertEquals("https://www.google.com", links.get("Google"));
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldParseRelativeLinks_WhenResponseHasOkStatus(){
        WebReader reader = new WebReader(new OkHttpClient(),baseUri);

        try {
            WebResult result = reader.read();
            HashMap<String, String> links = result.extractData(TestExtractors.basicLinkExtractor);
            assertEquals("https://www.example.com/testRelative", links.get("TestRelative"));
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }
}
