package io.rosensteel.CLI;

import io.rosensteel.HTTP.WebReader;
import io.rosensteel.HTTP.WebResult;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            WebReader webReader = new WebReader(HttpClients.createDefault());
            WebResult webResult = webReader.read("https://www.cochranelibrary.com/cdsr/reviews/topics");
            if(webResult.isSuccess()){
                System.out.println(webResult.getBody());
            }
        } catch (IOException e) {
            System.err.println("Error reading web page.");
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}