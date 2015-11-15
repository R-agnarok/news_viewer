package ru.fate.newsviewer.rss;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by Фенрир on 13.11.2015.
 */
public class RssFetcher {
    public static ArrayList<RssItem> fetch(String link) {

        ArrayList<RssItem> rssItems = new ArrayList<>();

        if (link == null || link.equals(""))
            return rssItems;

        try {

            URL url = new URL(link);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream is = conn.getInputStream();
                DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

                Document document = db.parse(is);
                Element element = document.getDocumentElement();

                NodeList nodeList = element.getElementsByTagName("item");
                if (nodeList.getLength() > 0) {
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        Element entry = (Element) nodeList.item(i);

                        Element _titleE = (Element) entry.getElementsByTagName("title").item(0);
                        Element _descriptionE = (Element) entry.getElementsByTagName("description").item(0);
                        Element _pubDateE = (Element) entry.getElementsByTagName("pubDate").item(0);
                        Element _articleUrlE = (Element) entry.getElementsByTagName("link").item(0);
                        Element _imageUrlE = (Element) entry.getElementsByTagName("enclosure").item(0);

                        Node _titleN = _titleE.getFirstChild();
                        Node _descriptionN = _descriptionE.getFirstChild();
                        Node _pubDateN = _pubDateE.getFirstChild();
                        Node _articleUrlN = _articleUrlE.getFirstChild();

                        String imageUrl = _imageUrlE != null ? _imageUrlE.getAttribute("url") : null;

                        String title = _titleN != null ? _titleN.getNodeValue() : null;
                        String description = _descriptionN != null ? _descriptionN.getNodeValue() : null;
                        String articleUrl = _articleUrlN != null ? _articleUrlN.getNodeValue() : null;

                        Date pubDate = _pubDateN != null ? new Date(_pubDateN.getNodeValue()) : null;

                        RssItem rssItem = new RssItem(description, title,
                                pubDate, articleUrl, imageUrl);

                        rssItems.add(rssItem);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rssItems;
    }
}
