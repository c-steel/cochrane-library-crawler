package io.rosensteel.cochrane.cli;

import org.apache.commons.cli.ParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrawlerSettings {
    private boolean displayHelp = false;
    private List<Integer> topicNumbers;
    private String fileOutput;
    private int topicsSize;

    public CrawlerSettings(boolean displayHelp) {
        this.displayHelp = displayHelp;
    }

    public CrawlerSettings(String rawTopicNumbers, String fileOutput, int topicsSize) throws Exception {
        this.fileOutput = fileOutput;
        this.topicsSize = topicsSize;
        this.topicNumbers = parseTopicNumbers(rawTopicNumbers);
    }

    public List<Integer> parseTopicNumbers(String rawTopicNumbers) throws Exception {
        List<Integer> parsedNumbers = new ArrayList<>();
        for (String s : rawTopicNumbers.split(",")) {
            String[] ranges = s.split("-");
            if (ranges.length == 1) {
                parsedNumbers.add(Integer.parseInt(ranges[0]));
            } else if (ranges.length == 2) {
                int start = Integer.min(Integer.parseInt(ranges[0]), Integer.parseInt(ranges[1]));
                int end = Integer.max(Integer.parseInt(ranges[0]), Integer.parseInt(ranges[1]));
                for (int numInRange = start; numInRange <= end; numInRange++) {
                    parsedNumbers.add(numInRange);
                }
            } else {
                throw new ParseException("Could not parse topic numbers: (" + s + ")");
            }
        }

        parsedNumbers.stream()
                .distinct()
                .forEach( topicNum -> {
                    if(!(topicNum < topicsSize)) {
                        System.out.println("Warning - found number outside of range. Num (" + topicNum + ") Max (" + (topicsSize - 1) + ")");
                    }
                });

        return parsedNumbers.stream()
                .distinct()
                .filter(topicNum -> topicNum < topicsSize)
                .collect(Collectors.toList());
    }

    public List<Integer> getTopicNumbers() {
        return topicNumbers;
    }

    public String getFileOutput() {
        return fileOutput;
    }

    public boolean isDisplayHelp() {
        return displayHelp;
    }
}
