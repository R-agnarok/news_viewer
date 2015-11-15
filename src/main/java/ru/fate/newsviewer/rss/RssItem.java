package ru.fate.newsviewer.rss;

import java.util.Date;

/**
 * Created by Фенрир on 13.11.2015.
 */
public class RssItem {
    private String description;
    private String title;
    private Date publicationDate;
    private String articleUrl;
    private String imageUrl;


    public RssItem(String description, String title, Date publicationDate, String articleUrl, String imageUrl) {
        this.description = description;
        this.title = title;
        this.publicationDate = publicationDate;
        this.articleUrl = articleUrl;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public void setArticleUrl(String link) {
        this.articleUrl = articleUrl;
    }
}
