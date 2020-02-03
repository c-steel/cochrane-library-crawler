package io.rosensteel.cochrane.crawler;

import java.util.Objects;

public class CochraneReview {

    private String url;
    private String topic;
    private String title;
    private String author;
    private String date;

    public CochraneReview(String url, String topic, String title, String author, String date) {
        this.url = url;
        this.topic = topic;
        this.title = title;
        this.author = author;
        this.date = date;
    }

    @Override
    public String toString() {
        return "CochraneReview{" +
                "url='" + url + '\'' +
                ", topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String toPipeDelimited() {
        return url + "|" +
                topic + "|" +
                title + "|" +
                author + "|" +
                date;
    }

    public String getUrl() {
        return url;
    }

    public String getTopic() {
        return topic;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CochraneReview that = (CochraneReview) o;
        return getUrl().equals(that.getUrl()) &&
                getTopic().equals(that.getTopic()) &&
                getTitle().equals(that.getTitle()) &&
                getAuthor().equals(that.getAuthor()) &&
                getDate().equals(that.getDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUrl(), getTopic(), getTitle(), getAuthor(), getDate());
    }
}
