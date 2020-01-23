package io.rosensteel.HTTP;

import io.rosensteel.Stubs.BadRequestHttpClientStub;
import io.rosensteel.Stubs.OkHttpClient;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class WebReaderTest {

    @Test
    public void WebResult_ShouldNotBeSuccess_WhenResponseHasBadRequestStatus(){
        WebReader reader = new WebReader(new BadRequestHttpClientStub());

        try {
            WebResult result = reader.read("");
            assertFalse(result.isSuccess());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldContainBodyWithEmptyString_WhenResponseHasBadRequestStatus(){
        WebReader reader = new WebReader(new BadRequestHttpClientStub());

        try {
            WebResult result = reader.read("");
            assertEquals(0, result.getBody().length());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldBeSuccess_WhenResponseHasOkStatus(){
        WebReader reader = new WebReader(new OkHttpClient());

        try {
            WebResult result = reader.read("");
            assertTrue(result.isSuccess());
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }

    @Test
    public void WebResult_ShouldContainBodyWithText_WhenResponseHasContent(){
        WebReader reader = new WebReader(new OkHttpClient());

        try {
            WebResult result = reader.read("");
            assertTrue(result.getBody().length() > 0, "Expected body text length greater than zero.");
        } catch (IOException e) {
            fail("Unexpected exception");
        }
    }
}
