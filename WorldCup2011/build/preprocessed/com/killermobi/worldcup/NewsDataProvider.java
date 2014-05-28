/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.killermobi.worldcup;

import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.model.DataProvider;

/**
 *
 * @author rishabh
 */
public class NewsDataProvider extends DataProvider {

    // Declare static values to identify the provided data
    private static final String NEWS_PROPERTY = "news";
    private static final String DESC_PROPERTY = "newsdetail";
    private static final String PUB_PROPERTY = "newspublish";

    // create a value variable

    private String news = "unknown";
    private String newsdetail = "unknown";
    private String newspublish = "unknown";

    public String getNewspublish() {
        return newspublish;
    }

    public void setNewspublish(String newspublish) {
        this.newspublish = newspublish;
        dispatchUpdateEvent(PUB_PROPERTY);
    }

    public String getNews() {
        return news;
    }

    public void setNews(String news) {
        this.news = news;
        dispatchUpdateEvent(NEWS_PROPERTY);
    }

    public String getNewsdetail() {
        return newsdetail;
    }

    public void setNewsdetail(String newsdetail) {
        this.newsdetail = newsdetail;
        dispatchUpdateEvent(DESC_PROPERTY);
    }
   
       
    // override the parent method to handle user defined value
    protected Object getUserDefinedValue(String property) {

        // handle custom properties requests
        if (NEWS_PROPERTY.equals(property)) {
            //return Kuix.getCanvas().getPlatformName();        
            return this.news;
        }
        if (DESC_PROPERTY.equals(property)) {
            return this.newsdetail;
        }
        if (PUB_PROPERTY.equals(property)) {
            return this.newspublish;
        }
        // default behavior if the property has not been found
        return null;
    }

}
