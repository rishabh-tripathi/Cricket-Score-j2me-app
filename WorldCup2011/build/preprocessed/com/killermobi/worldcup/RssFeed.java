/**
 * RssFeed.java
 */

package com.killermobi.worldcup;

import java.util.*;

/**
 * RssFeed class contains one RSS feed's properties.
 * Properties include name and URL to RSS feed.
 *
 * @author Tommi Laukkanen
 */
public class RssFeed{
    
    private String m_url  = "";

    public void setM_url(String m_url) {
        this.m_url = m_url;
    }
    private String m_name = "";
    protected Vector m_items = new Vector();  // The RSS item vector
    
    /** Creates a new instance of RSSBookmark */
    public RssFeed(String name, String url){
        m_name = name;
        m_url = url;
    }
    
    /** Creates a new instance of RSSBookmark with record store string */
    public RssFeed(String storeString){
        int i = storeString.indexOf("|");
        if(i>0) {
            m_name = storeString.substring(0,i);
            m_url  = storeString.substring(i+1);
        }
    }
    
    /** Return bookmark's name */
    public String getName(){
        return m_name;
    }
    
    /** Return bookmark's URL */
    public String getUrl(){
        return m_url;
    }
    
    /** Return record store string */
    public String getStoreString(){
        return m_name + "|" + m_url;
    }
    
    /** Return RSS feed items */
    public Vector getItems() {
        return m_items;
    }
    
}
