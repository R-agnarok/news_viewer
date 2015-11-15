package ru.fate.newsviewer.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * Created by Фенрир on 13.11.2015.
 */
public class HtmlParser {

    public static String parse(String link) {
        Document doc;
        try {
            doc = Jsoup.connect(link).get();
            Elements container = doc.select("#article_full_text");

            container.select("div").remove();

            return container.html();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
