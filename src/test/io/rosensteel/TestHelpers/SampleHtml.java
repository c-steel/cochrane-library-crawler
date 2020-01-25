package io.rosensteel.TestHelpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SampleHtml {

    public static String cochraneTopicsList;
    public static String cochraneReviewList;
    public static String cochraneReview;

    static {
        try {
            cochraneTopicsList = readResource("cochrane-library-topic-list.html");
            cochraneReviewList = readResource("cochrane-library-review-list.html");
            cochraneReview = readResource("cochrane-library-review.html");
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


    private static String readResource(String resourceFile) throws IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(resourceFile);
        StringBuilder stringBuilder = new StringBuilder();

        if (inputStream != null) {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } else {
            throw new IOException("Could not read file (" + resourceFile + ")");
        }
    }

}
