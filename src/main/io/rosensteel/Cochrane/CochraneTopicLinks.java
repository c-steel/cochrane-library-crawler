package io.rosensteel.Cochrane;

import java.util.HashMap;
import java.util.Set;

public class CochraneTopicLinks {
    private HashMap<String, String> topicLinks = new HashMap<>();

    public void put (String topicName, String link) {
        topicLinks.put(topicName, link);
    }

    public String get (String topicName) {
        String link = topicLinks.get(topicName);
        if(link == null) {
            System.err.println("Topic not available (" + topicName + ")");
            System.err.println("Known topics (" + topicLinks.keySet().toString() + ")");
            new Exception().printStackTrace();
            System.exit(1);
        }
        return link;
    }

    public int numTopics() {
        return topicLinks.size();
    }

    public Set<String> availableTopics () {
        return topicLinks.keySet();
    }
}
