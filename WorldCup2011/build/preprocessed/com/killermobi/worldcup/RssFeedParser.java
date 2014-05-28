/**
 * RssFeedParser.java
 */

package com.killermobi.worldcup;

import javax.microedition.io.*;
import java.util.*;
import java.io.*;
import org.xmlpull.v1.*;
import org.kxml2.io.*;

/**
 * RssFeedParser is an utility class for aquiring and parsing a RSS feed.
 * HttpConnection is used to fetch RSS feed and kXML is used on xml parsing.
 */
public class RssFeedParser {

    public HttpConnection hc = null;
    public DataInputStream dis = null;
    private RssFeed m_rssFeed;  // The RSS feed
    private XmlPullParser m_xmlParser = new KXmlParser(); // The Xml parser
    
    /** Create new instance of RssDocument */
    public RssFeedParser(RssFeed rssFeed) {
        m_rssFeed = rssFeed;
    }
    
    /** Return RSS feed */
    public RssFeed getRssFeed() {
        return m_rssFeed;
    }
    
    /** send a GET request to web server */
    public void parseRssFeed()
    throws IOException, Exception {
        
        String response = "";
        try {
            /**
             * Open an HttpConnection with the Web server
             * The default request method is GET
             */
            hc = (HttpConnection) Connector.open(m_rssFeed.getUrl());
            hc.setRequestMethod(HttpConnection.GET);
            /** Some web servers requires these properties */
            hc.setRequestProperty("User-Agent", 
                    "Profile/MIDP-2.0 Configuration/CLDC-1.0");
            hc.setRequestProperty("Content-Length", "0");
            hc.setRequestProperty("Connection", "close");
            
            /** 
             * Get a DataInputStream from the HttpConnection 
             * and forward it to kXML parser
             */
            parseRssFeedXml( hc.openInputStream() );
        } catch(Exception e) {
            throw new Exception("Error while parsing RSS data: " 
                    + e.toString());
        } finally {
            if (dis != null) dis.close();
            if (hc != null) hc.close();
        }
    }

   /** 
    * Nasty RSS feed XML parser.
    * Seams to work with both RSS 1.0 and 2.0.
    */
    public void parseRssFeedXml(InputStream is) 
            throws IOException, XmlPullParserException {
        /** Initialize item collection */
        m_rssFeed.getItems().removeAllElements();
        
        /** Initialize XML parser and parse RSS XML */
        KXmlParser  parser = new KXmlParser();
        parser.setInput( new InputStreamReader(is) );

        /** RSS item properties */
        String title = null;
        String description = null;
        String link = null;
        String pubDate = null;
        String summary = null;

        /** <?xml...*/
        parser.nextTag();

        /** Various tags... Wait for the <item> tag */
        parser.require(parser.START_TAG, null, null);
        while(!"item".equals(parser.getName()) ){
            /** Check if document doesn't include any item tags */
            if( parser.next() == parser.END_DOCUMENT )
                throw new IOException("No items in RSS feed!");
        }

        /** Parse <item> tags */
        do {
            parser.require(parser.START_TAG, null, null);

            /** Initialize properties */
            title = "";
            description = "";
            link = "";

            /** One <item> tag handling*/
            while (parser.nextTag() != parser.END_TAG) {
                parser.require(parser.START_TAG, null, null);
                String name = parser.getName();
                String text = parser.nextText();

                /** Save item property values */
                if (name.equals("title"))
                    title = text;
                else if (name.equals("description"))
                    description = text;
                else if (name.equals("link"))
                    link = text;
                else if (name.equals("pubDate"))
                    pubDate = text;
                else if (name.equals("summary"))
                    summary = text;
                parser.require(parser.END_TAG, null, name);
            }
            
            /** Debugging information */
         //   System.out.println ("Title:       " + title);
         //   System.out.println ("Link:        " + link);
         //   System.out.println ("Description: " + description);
         //   System.out.println ("pubDate: " + pubDate);
         //   System.out.println ("summary: " + summary);

            /** Create new RSS item and add it do RSS document's item
             *  collection
             */
            RssItem rssItem = new RssItem(title, link, description, pubDate, summary);
            m_rssFeed.getItems().addElement( rssItem );
            parser.nextTag();
            
        } while("item".equals(parser.getName()));
    }        
}
