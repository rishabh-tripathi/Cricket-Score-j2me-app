/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.killermobi.worldcup;

import java.io.InputStream;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.widget.Gauge;
import org.kalmeo.kuix.widget.List;
import org.kalmeo.kuix.widget.PopupBox;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.MathFP;
import org.kalmeo.util.frame.Frame;


/**
 *
 * @author rishabh
 */
public class HomeFrame implements Frame, Runnable {

   public Screen screen;
   private boolean m_getPage = true;
   private RssFeedParser m_curRssParser, n_curRssParser, bn_curRssParser, commentParser;
   public List l,newsList,bnewsList;
   public int liveScoreFeedSize=0,newsFeedSize=0,bnewsFeedSize=0;
   public Thread t;
   public RssFeed rssFeed,newsFeed,bnewsFeed;
   public List commentList;
   public int commentFeedSize;
   public RssFeed commentFeed;
   public Gauge gauge;
   public PopupBox progressBox;
   /** Fill RSS header list */

   
 	
    public void onAdded() {        
        // Load the content from the XML file with Kuix.loadScreen static method        
        rssFeed = new RssFeed("LiveScore","http://static.cricinfo.com/rss/livescores.xml");
        //newsFeed = new RssFeed("News","http://newsrss.bbc.co.uk/rss/sportonline_uk_edition/cricket/rss.xml");
        newsFeed = new RssFeed("News","http://www.espncricinfo.com/rss/content/story/feeds/0.xml");
        bnewsFeed = new RssFeed("Breaking News","http://in.news.yahoo.com/myyahoo/rss/sports-cricket.xml");
        commentFeed = new RssFeed("Comment","http://killermobi.com/worldcup2011/comments/feeds/rss");
        commentParser = new RssFeedParser(commentFeed);
        m_curRssParser = new RssFeedParser(rssFeed);
        n_curRssParser = new RssFeedParser(newsFeed);
        bn_curRssParser = new RssFeedParser(bnewsFeed);
        t = new Thread(this);
        gauge = new Gauge();
        progressBox = Kuix.showPopupBox(null, -1, gauge, Kuix.getMessage(KuixConstants.OK_I18N_KEY), null, null, null, null);
        t.start();
    }


        public void run() {
        long lngStart;
        long lngTimeTaken;

        int PROGRESS_INCREMENT = 5;
	int MAX_PROGRESS = 100;
	int progress = 0;

        while (true) {
            gauge.setValue(MathFP.div(progress, MAX_PROGRESS));
            progress += PROGRESS_INCREMENT;
            if (progress > MAX_PROGRESS) {
		progressBox.remove();
	//	return;
            }

            try {
                screen = Kuix.loadScreen("home.xml", null);
                if (m_getPage) {
                    try {
                        /** Get RSS feed */
                        m_curRssParser.parseRssFeed();
                        
                        l = (List) screen.getWidget("mainlist");
                           
                        liveScoreFeedSize = m_curRssParser.getRssFeed().getItems().size();
                        LiveDataProvider[] ldd = new LiveDataProvider[liveScoreFeedSize+2];

                        RssFeed feed = m_curRssParser.getRssFeed();
                        for (int i = 0; i < liveScoreFeedSize; i++) {                                                        
                            RssItem r = (RssItem) feed.getItems().elementAt(i);
                            String title = r.getTitle();
                            title = " "+title;
                            String[] country = {"Australia","Pakistan","Sri Lanka","Zimbabwe","New Zealand","Kenya","Canada","India","England","South Africa","West Indies","Ireland","Bangladesh","Netherlands"};
                            int testForValidMatch=0;
                            for(int j=0;j<14;j++)
                            {
                                if(title.indexOf(country[j],0)>0)
                                {
                                    testForValidMatch++;
                                }                                
                            }

                            if(testForValidMatch>1)
                            {
                            ldd[i] = new LiveDataProvider();
                            title = removeCharacters(title, "abcdefghijklmnopqrstuvwxyz ABCDEFGHIJKLMNOPQRSTUVWXYZ");
                            ldd[i].setId(String.valueOf(i));
                            ldd[i].setMatch(title);
                            ldd[i].setScore(r.getDescription());
                            l.addItem(ldd[i]);
                            }
                        }
                        screen.setCurrent();
                    } catch (Exception e) {
                        /** Error while parsing RSS feed */
                        e.printStackTrace();
                        Kuix.alert(Kuix.getMessage("Error in fetching/parsing data"), KuixConstants.ALERT_OK);
                        //m_display.setCurrent( m_loadForm );
                    }


                        try {
                        /** Get breaking News feed */
                        bn_curRssParser.parseRssFeed();

                        bnewsList = (List)screen.getWidget("bnewslist");

                        bnewsFeedSize = bn_curRssParser.getRssFeed().getItems().size();
                        BreakingNewsDataProvider[] ndp = new BreakingNewsDataProvider[bnewsFeedSize+2];

                        RssFeed feed = bn_curRssParser.getRssFeed();
                        for (int i = 0; i < bnewsFeedSize; i++) {
                            RssItem r = (RssItem) feed.getItems().elementAt(i);
                            ndp[i] = new BreakingNewsDataProvider();
                            ndp[i].setNews(r.getTitle());
                            ndp[i].setNewsdetail(r.getDescription());
                            ndp[i].setNewspublish(r.getPublish());
                            bnewsList.addItem(ndp[i]);
                        }
                        screen.setCurrent();
                    } catch (Exception e) {
                        /** Error while parsing RSS feed */
                        e.printStackTrace();
                        Kuix.alert(Kuix.getMessage("Error in fetching/parsing data"), KuixConstants.ALERT_OK);
                        //m_display.setCurrent( m_loadForm );
                    }



                    try {
                        /** Get News feed */
                        n_curRssParser.parseRssFeed();

                        newsList = (List)screen.getWidget("newslist");

                        newsFeedSize = n_curRssParser.getRssFeed().getItems().size();
                        NewsDataProvider[] ndp = new NewsDataProvider[newsFeedSize+2];

                        RssFeed feed = n_curRssParser.getRssFeed();
                        for (int i = 0; i < newsFeedSize; i++) {                           
                            RssItem r = (RssItem) feed.getItems().elementAt(i);                                                                                    
                            ndp[i] = new NewsDataProvider();
                            ndp[i].setNews(r.getTitle());
                            ndp[i].setNewsdetail(r.getDescription());
                            ndp[i].setNewspublish(r.getPublish());
                            newsList.addItem(ndp[i]);
                        }
                        screen.setCurrent();
                    } catch (Exception e) {
                        /** Error while parsing RSS feed */
                        e.printStackTrace();
                        Kuix.alert(Kuix.getMessage("Error in fetching/parsing data"), KuixConstants.ALERT_OK);
                        //m_display.setCurrent( m_loadForm );
                    }


                        try {
                        /** Get News feed */
                        commentParser.parseRssFeed();

                        commentList = (List)screen.getWidget("commentlist");

                        commentFeedSize = commentParser.getRssFeed().getItems().size();
                        if(commentFeedSize > 0)
                        {
                            commentList.removeAllItems();
                        }
                        CommentDataProvider[] ndp = new CommentDataProvider[commentFeedSize+2];

                        RssFeed feed = commentParser.getRssFeed();
                        for (int i = 0; i < commentFeedSize; i++) {
                            RssItem r = (RssItem) feed.getItems().elementAt(i);
                            ndp[i] = new CommentDataProvider();
                            ndp[i].setName(r.getDescription());
                            ndp[i].setOn(r.getTitle());
                            ndp[i].setComment(r.getDescription());
                            commentList.addItem(ndp[i]);
                        }
                        screen.setCurrent();
                    } catch (Exception e) {
                        /** Error while parsing RSS feed */
                        e.printStackTrace();
                        Kuix.alert(Kuix.getMessage("Error in fetching/parsing data"), KuixConstants.ALERT_OK);
                        //m_display.setCurrent( m_loadForm );
                    }




                   m_getPage = false;
                }
                lngStart = System.currentTimeMillis();
                lngTimeTaken = System.currentTimeMillis() - lngStart;
                if (lngTimeTaken < 100) {
                    t.sleep(75 - lngTimeTaken);
                }
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public boolean onMessage(Object identifier, Object[] arguments) {
        // return "true" makes the FramHandler to forward the message to the next frame in the stack
      if ("show".equals(identifier)) {
      RssFeed feed = m_curRssParser.getRssFeed();
      Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
      DataProvider ld = widget.getDataProvider();
      RssItem item = (RssItem) feed.getItems().elementAt(Integer.parseInt(ld.getStringValue("id")));
      Kuix.getFrameHandler().pushFrame(new LiveMatchFrame(item,ld,m_curRssParser));
      //return false;
      //Kuix.alert(Kuix.getMessage(item.getDescription()), KuixConstants.ALERT_OK);
        }

      if ("showNews".equals(identifier)) {      
      Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();      
      DataProvider ld = widget.getDataProvider();      
      Kuix.showPopupBox("/xml/newsPopup1.xml", ld);
      //Kuix.alert(Kuix.getMessage(ld.getStringValue("news")+"|| Publish Date: "+ld.getStringValue("newspublish")+"||"+ld.getStringValue("newsdetail")), KuixConstants.ALERT_OK);
        }

      if ("showBNews".equals(identifier)) {      
      Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
      DataProvider ld = widget.getDataProvider();
      Kuix.showPopupBox("/xml/newsPopup.xml", ld);
      //Kuix.alert(Kuix.getMessage(ld.getStringValue("bnews")+"|| Publish Date: "+ld.getStringValue("bnewspublish")+"||"+ld.getStringValue("bnewsdetail")), KuixConstants.ALERT_OK);
        }

      if ("showComment".equals(identifier)) {
      Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
      DataProvider ld = widget.getDataProvider();
      Kuix.showPopupBox("/xml/showComment.xml", ld);
      //Kuix.alert(Kuix.getMessage(ld.getStringValue("news")+"|| Publish Date: "+ld.getStringValue("newspublish")+"||"+ld.getStringValue("newsdetail")), KuixConstants.ALERT_OK);
        }

      if ("refresh".equals(identifier)) {                  
              try{Thread.yield();}catch(Exception e){};
              t = null;
              t = new Thread(this);
              m_getPage = true;
              t.start();         
      }
          if ("addComment".equals(identifier)) {
            Kuix.getFrameHandler().pushFrame(new AddComment());
      }

      if ("share".equals(identifier)) {
          Kuix.getFrameHandler().pushFrame(new shareThisFrame());
      }

      if ("about".equals(identifier)) {          
          Kuix.getFrameHandler().pushFrame(new aboutFrame());
      }

      if ("feedback".equals(identifier)) {
          Kuix.getFrameHandler().pushFrame(new FeedbackFrame());
      }

      if ("exitConfirm".equals(identifier)) {
            // display a popup message
            Kuix.alert(Kuix.getMessage("Do you really want to Exit?"), KuixConstants.ALERT_YES | KuixConstants.ALERT_NO, "exit", null);
            return false;
        }

      if ("exit".equals(identifier)) {
            // get the midlet instance to invoke the Destroy() method
            Home.getDefault().destroyImpl();
            //if the event has been processed, we return 'false' to avoid event forwarding to other frames
            return false;
        }
        

        return true;
    }

    public void onRemoved() {
        // destroy the screen
        screen.cleanUp();
        // unreference the screen object to free the memory
        screen = null;
    }

        public static String removeCharacters(String text, String charsToKeep) {
         StringBuffer buffer = new StringBuffer();
         for(int i = 0; i < text.length(); i++) {
             char ch = text.charAt(i);
             if(charsToKeep.indexOf(ch) > -1) {
                 buffer.append(ch);
             }
         }
         return buffer.toString();
     }
 
}
