/**
 * RssItem.java
 */

package com.killermobi.worldcup;

/**
 * RssItem class is a data store for a single item in RSS feed.
 * One item consist of title, link and description.
 */
public class RssItem {
    
    private String  m_title = "";   // The RSS item title
    private String  m_link  = "";   // The RSS item link
    private String  m_desc  = "";   // The RSS item description
    private String  m_pub  = "";
    private String m_summary = "";


    /** Creates a new instance of RssItem */
    public RssItem(String title, String link, String desc, String pub, String summary) {
        m_title = title;
        m_link  = link;
        m_desc  = desc;
        m_pub = pub;
        m_summary = summary;
    }

    public String getM_summary() {
        return m_summary;
    }

    /** Get RSS item title */
    public String getTitle(){
        return m_title;
    }
    
    /** Get RSS item link address */
    public String getLink(){
        return m_link;
    }
    
    /** Get RSS item description */
    public String getDescription(){
        return m_desc;
    }

      public String getPublish() {
        return m_pub;
    }
}
