package io.rosensteel.http;

import io.rosensteel.TestHelpers.BadRequestHttpClientStub;
import io.rosensteel.TestHelpers.OkHttpClientStub;
import io.rosensteel.TestHelpers.SampleHtml;
import org.apache.http.client.HttpResponseException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WebReaderTest {

    String baseUri = "https://www.does-not-matter.com";

    @Test
    public void read_shouldThrowException_whenResponseHasBadRequestStatus() {
        WebReader webReader = new WebReader(new BadRequestHttpClientStub(), baseUri);
        int expectedStatusCode = 400;
        String expectedReasonPhrase = "Bad Request";

        HttpResponseException httpResponseException = assertThrows(HttpResponseException.class, () -> webReader.read("www.does-not-matter.com"));
        assertEquals(expectedStatusCode,httpResponseException.getStatusCode());
        assertEquals(expectedReasonPhrase,httpResponseException.getReasonPhrase());
    }

    @Test
    public void read_shouldReturnResultWithBody_whenResponseHasOkStatus(){
        WebReader webReader = new WebReader(new OkHttpClientStub(SampleHtml.cochraneTopicsList), baseUri);

        try {
            WebResult result = webReader.read("www.does-not-matter.com");
            assertTrue(result.getBody().length() > 0, "Expected body text length greater than zero.");
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void read_shouldReturnResultWithDom_whenResponseHasOkStatus() {
        WebReader webReader = new WebReader(new OkHttpClientStub(SampleHtml.cochraneTopicsList), baseUri);

        try {
            WebResult result = webReader.read("www.does-not-matter.com");
            assertTrue(result.getDom().hasText());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

}
