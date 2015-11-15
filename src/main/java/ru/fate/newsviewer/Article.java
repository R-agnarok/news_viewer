package ru.fate.newsviewer;

import java.util.Date;

/**
 * Created by Фенрир on 13.11.2015.
 */
public class Article {
    private String description;
    private String title;
    private Date pubDate;
    private String content;
    private String imageURL;
    private String articleURL;

    public Article(String description, String title, Date pubDate, String content, String imageURL, String articleURL) {
        this.description = description;
        this.title = title;
        this.pubDate = pubDate;
        this.content = content;
        this.articleURL = articleURL;
        this.imageURL = imageURL;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getArticleURL() {
        return articleURL;
    }

    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getPubDate() {
        return pubDate;
    }

    public void setPubDate(Date pubDate) {
        this.pubDate = pubDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
