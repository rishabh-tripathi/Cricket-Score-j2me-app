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
import org.kalmeo.kuix.widget.List;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Text;
import org.kalmeo.kuix.widget.TextArea;
import org.kalmeo.kuix.widget.Widget;
import org.kalmeo.util.frame.Frame;

/**
 *
 * @author rishabh
 */
public class LiveMatchFrame implements Frame, Runnable {

   public Screen screen;
   public RssFeed commentFeed;
   public RssItem rssItem;
   public String title = null;
   public DataProvider ld;
   public RssFeedParser scoreParser,commentParser;
   public boolean m_getPage = true;   
   public Thread t;
   public boolean running = true;
   public Text stat;
   public List commentList;
   public int commentFeedSize;
   public boolean firstRun = true;
   public int oldRun=0;
   public int oldWicket=0;

   public String country1=null, country2=null;
   public int run1=0,run2=0;
   public int wicket1=0,wicket2=0;
   public int battingTeam=0;
   public Text titleField;
   public boolean gameOver = false;
   public int winTeam = 0;

   public InputStream is;
   public Player player;

    public LiveMatchFrame(RssItem rssItem,DataProvider ld,RssFeedParser scoreParser) {
        this.rssItem = rssItem;
        this.ld = ld;
        this.scoreParser = scoreParser;
    }


    public void onAdded() {
        screen = Kuix.loadScreen("liveMatch.xml", null);
        commentFeed = new RssFeed("Comment","http://killermobi.com/worldcup2011/comments/feeds/rss");
        commentParser = new RssFeedParser(commentFeed);
        
        titleField = (Text)screen.getWidget("title");
        title = ld.getStringValue("match");
        titleField.setText(ld.getStringValue("match"));

        screen.setCurrent();
        t = new Thread(this);
        t.start();
    }


        public void run() {
      
            while(running){
               try {
                if (m_getPage) {
                    try {
                        /** Get RSS feed */
                        stat = (Text)screen.getWidget("status");
                        stat.setText("Updating...");
                        scoreParser.parseRssFeed();
                        RssFeed feed = scoreParser.getRssFeed();
                        RssItem r = (RssItem) feed.getItems().elementAt(Integer.parseInt(ld.getStringValue("id")));
     









                        try
                        {                            
                        if(firstRun)
                        {
                        String s = title;
                        country1 = s.substring(0, s.indexOf(" v "));
                        country2 = s.substring(s.indexOf(" v ")+3, s.length());
                        String tt = r.getDescription();
                        String score1 = tt.substring(0, tt.indexOf(" v "));
                        String score2 = tt.substring(tt.indexOf(" v ")+3, tt.length());
                         TextArea inn1 = (TextArea)screen.getWidget("misScore");
                         inn1.setText(score1+" || "+score2);   
                        }

                        System.out.println(country1);
                        System.out.println(country2);
                        
                        titleField.setText(country1+" Vs "+country2);
                        
                        String tt = r.getDescription();//"india 222/2 * v canada";
                        String score1 = tt.substring(0, tt.indexOf(" v "));
                        String score2 = tt.substring(tt.indexOf(" v ")+3, tt.length());
                        

                        int inning=0;
                        
                        if(score1.indexOf("*")>0)
                        {
                            if((score2.indexOf("0")>0) || (score2.indexOf("1")>0) || (score2.indexOf("2")>0) || (score2.indexOf("3")>0) || (score2.indexOf("4")>0) || (score2.indexOf("5")>0) || (score2.indexOf("6")>0) || (score2.indexOf("7")>0) ||(score2.indexOf("8")>0) || (score2.indexOf("9")>0))
                            {
                                inning = 2;
                            }
                            else
                            {
                                inning = 1;
                            }
                            battingTeam = 1;
                        }
                        else if(score2.indexOf("*")>0)
                        {
                            if((score1.indexOf("0")>0) || (score1.indexOf("1")>0) || (score1.indexOf("2")>0) || (score1.indexOf("3")>0) || (score1.indexOf("4")>0) || (score1.indexOf("5")>0) || (score1.indexOf("6")>0) || (score1.indexOf("7")>0) ||(score1.indexOf("8")>0) || (score1.indexOf("9")>0))
                            {
                                inning = 2;
                            }
                            else
                            {
                                inning = 1;
                            }
                            battingTeam = 2;
                        }
                        else
                        {
                            inning = 0;
                        }

        
                        if(battingTeam == 1)
                        {                            
                            if(score1.indexOf("/")>0)
                            {
                            String run = score1.substring(score1.indexOf("/")-3,score1.indexOf("/"));
                            run = removeCharacters(run, "0123456789/");
                            run1 = Integer.parseInt(run);
                            String wic = score1.substring(score1.indexOf("/")+1,score1.indexOf("/")+3);
                            wic = removeCharacters(wic, "0123456789/");
                            wicket1 = Integer.parseInt(wic);
                            }
                            else
                            {
                                if((score1.indexOf("0")>0) || (score1.indexOf("1")>0) || (score1.indexOf("2")>0) || (score1.indexOf("3")>0) || (score1.indexOf("4")>0) || (score1.indexOf("5")>0) || (score1.indexOf("6")>0) || (score1.indexOf("7")>0) ||(score1.indexOf("8")>0) || (score1.indexOf("9")>0))
                                {
                                   String run = score1.substring(score1.indexOf("*")-4, score1.indexOf("*"));
                                   run = removeCharacters(run, "0123456789/");
                                   run1 = Integer.parseInt(run);
                                   wicket1 = 0;
                                }
                            }
                        }
                        else if(battingTeam == 2)
                        {
                            if(score2.indexOf("/")>0)
                            {
                            String run = score2.substring(score2.indexOf("/")-3,score2.indexOf("/"));
                            run = removeCharacters(run, "0123456789/");
                            run2 = Integer.parseInt(run);
                            String wic = score2.substring(score2.indexOf("/")+1,score2.indexOf("/")+3);
                            wic = removeCharacters(wic, "0123456789/");
                            wicket2 = Integer.parseInt(wic);
                            }
                            else
                            {
                                if((score2.indexOf("0")>0) || (score2.indexOf("1")>0) || (score2.indexOf("2")>0) || (score2.indexOf("3")>0) || (score2.indexOf("4")>0) || (score2.indexOf("5")>0) || (score2.indexOf("6")>0) || (score2.indexOf("7")>0) ||(score2.indexOf("8")>0) || (score2.indexOf("9")>0))
                                {
                                   String run = score2.substring(score2.indexOf("*")-4, score2.indexOf("*"));
                                   run = removeCharacters(run, "0123456789/");
                                   run2 = Integer.parseInt(run);
                                   wicket2 = 0;
                                }
                            }
                        }

                        else
                        {
                            
                        }


                        if((score1.indexOf("*")<0) && (score2.indexOf("*")<0))
                        {
                          gameOver = true;
                          if(run1>run2)
                          {
                              winTeam = 1;
                          }
                          else if(run1<run2)
                          {
                              winTeam = 2;
                          }
                          else if((run1 == run2) && (run1!=0) && (run2!=0))
                          {
                              winTeam = 3;
                          }
                          else
                          {
                              winTeam = 0;
                          }
                        }

                        // updateing screen
                        if(inning == 2)
                        {
                            if(battingTeam == 1)
                            {
                                TextArea mis = (TextArea)screen.getWidget("misScore");
                                mis.setText("");
                                TextArea inn1 = (TextArea)screen.getWidget("inning1");
                                inn1.setText("First Inning:");
                                TextArea sco1 = (TextArea)screen.getWidget("score1");
                                sco1.setText(score2);
                                TextArea inn2 = (TextArea)screen.getWidget("inning2");
                                inn2.setText("Second Inning:");
                                TextArea sco2 = (TextArea)screen.getWidget("score2");
                                sco2.setText(country1+" : "+String.valueOf(run1)+"/"+String.valueOf(wicket1));
                                if(firstRun == false)
                                {
                                    String sc="NO RUN";
                                    String wk="";
                                    if(run1>oldRun)
                                    {
                                        int diff = run1-oldRun;
                                        if(diff == 4) 
                                        {
                                            sc = "FOUR";
                                            playFourSound();
                                        }
                                        else if ((diff > 4) &&(diff < 6))
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                        else if(diff == 6)
                                        {
                                            sc = "SIX";
                                            playSixSound();
                                        }
                                        else
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                    }

                                    if(wicket1>oldWicket)
                                    {
                                        int diff = wicket1 - oldWicket;
                                        wk = "& OUT! "+String.valueOf(diff)+" WICKET DOWN";
                                        playWicketSound();
                                        oldWicket = wicket1;
                                    }

                                    TextArea upd = (TextArea)screen.getWidget("update");
                                    upd.setText("Last Update: "+sc+" "+wk);
                                }
                            }
                            else if(battingTeam == 2)
                            {
                                TextArea mis = (TextArea)screen.getWidget("misScore");
                                mis.setText("");
                                TextArea inn1 = (TextArea)screen.getWidget("inning1");
                                inn1.setText("First Inning:");
                                TextArea sco1 = (TextArea)screen.getWidget("score1");
                                sco1.setText(score1);
                                TextArea inn2 = (TextArea)screen.getWidget("inning2");
                                inn2.setText("Second Inning:");
                                TextArea sco2 = (TextArea)screen.getWidget("score2");
                                sco2.setText(country2+" : "+String.valueOf(run2)+"/"+String.valueOf(wicket2));
                                
                                if(firstRun == false)
                                {
                                    String sc="NO RUN";
                                    String wk="";
                                    if(run2>oldRun)
                                    {
                                        int diff = run2-oldRun;
                                        if(diff == 4) 
                                        {
                                            sc = "FOUR";
                                            playFourSound();
                                        }
                                        else if ((diff > 4) &&(diff < 6))
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                        else if(diff == 6)
                                        {
                                            sc = "SIX";
                                            playSixSound();
                                        }
                                        else
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                    }

                                    if(wicket2>oldWicket)
                                    {
                                        int diff = wicket2 - oldWicket;
                                        wk = " & OUT! "+String.valueOf(diff)+" WICKET DOWN";
                                        playWicketSound();
                                        oldWicket = wicket2;
                                    }

                                    TextArea upd = (TextArea)screen.getWidget("update");
                                    upd.setText("Last Update: "+sc+" "+wk);
                                }
                            }
                        }
                        else if(inning == 1)
                        {
                            if(battingTeam == 1)
                            {
                                TextArea mis = (TextArea)screen.getWidget("misScore");
                                mis.setText("");
                                TextArea inn1 = (TextArea)screen.getWidget("inning1");
                                inn1.setText("First Inning:");
                                TextArea sco1 = (TextArea)screen.getWidget("score1");
                                sco1.setText(country1+" : "+String.valueOf(run1)+"/"+String.valueOf(wicket1));
                                if(firstRun == false)
                                {
                                    String sc="NO RUN";
                                    String wk="";
                                    if(run1>oldRun)
                                    {
                                        int diff = run1-oldRun;
                                        if(diff == 4)
                                        {
                                            sc = "FOUR";
                                            playFourSound();
                                        }
                                        else if ((diff > 4) &&(diff < 6))
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                        else if(diff == 6)
                                        {
                                            sc = "SIX";
                                            playSixSound();
                                        }
                                        else
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                    }

                                    if(wicket1>oldWicket)
                                    {
                                        int diff = wicket1 - oldWicket;
                                        wk = "& OUT! "+String.valueOf(diff)+" WICKET DOWN";
                                        playWicketSound();
                                        oldWicket = wicket1;
                                    }

                                    TextArea upd = (TextArea)screen.getWidget("update");
                                    upd.setText("Last Update: "+sc+" "+wk);
                                }
                            }
                            else if(battingTeam == 2)
                            {
                                TextArea mis = (TextArea)screen.getWidget("misScore");
                                mis.setText("");
                                TextArea inn1 = (TextArea)screen.getWidget("inning1");
                                inn1.setText("First Inning:");
                                TextArea sco1 = (TextArea)screen.getWidget("score1");
                                sco1.setText(country2+" : "+String.valueOf(run2)+"/"+String.valueOf(wicket2));

                                if(firstRun == false)
                                {
                                    String sc="NO RUN";
                                    String wk="";
                                    if(run2>oldRun)
                                    {
                                        int diff = run2-oldRun;
                                        if(diff == 4) 
                                        {
                                            sc = "FOUR";
                                            playFourSound();
                                        }
                                        else if ((diff > 4) &&(diff < 6))
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                        else if(diff == 6)
                                        {
                                            sc = "SIX";
                                            playSixSound();
                                        }
                                        else
                                        {
                                            sc = String.valueOf(diff)+" RUNS";
                                        }
                                    }

                                    if(wicket2>oldWicket)
                                    {
                                        int diff = wicket2 - oldWicket;
                                        wk = "& OUT! "+String.valueOf(diff)+" WICKET DOWN";
                                        playWicketSound();
                                        oldWicket = wicket2;
                                    }

                                    TextArea upd = (TextArea)screen.getWidget("update");
                                    upd.setText("Last Update: "+sc+" "+wk);
                                }
                            }
                        }
                        else
                        {
                                TextArea inn1 = (TextArea)screen.getWidget("inning1");
                                inn1.setText("Not Started yet!");
                        }


                        if(firstRun)
                        {                         
                        firstRun = false;
                        }
                           if(battingTeam == 1){
                            oldRun = run1;
                            oldWicket = wicket1;
                            }
                            else if(battingTeam == 2){
                            oldRun = run2;
                            oldWicket = wicket2;
                            }
                        
                        }catch(Exception ec)
                        {
                        String tt = r.getDescription();
                        String score1 = tt.substring(0, tt.indexOf(" v "));
                        String score2 = tt.substring(tt.indexOf(" v ")+3, tt.length());
                         TextArea inn1 = (TextArea)screen.getWidget("misScore");
                         inn1.setText(score1+" Vs "+score2);
                        }


                        if(gameOver)
                        {
                            if(winTeam != 0)
                            {
                                String winner;
                                if(winTeam == 1)
                                {
                                    winner = country1+" won the match";
                                }
                                else if(winTeam == 2)
                                {
                                    winner = country2+" won the match";
                                }
                                else if(winTeam == 3)
                                {
                                    winner = "Match draw";
                                }
                                else
                                {
                                    winner = "";
                                }
                                TextArea update = (TextArea)screen.getWidget("update");
                                update.setText(winner);
                            }
                        }










                             
                        screen.setCurrent();                        
                    } catch (Exception e) {
                        /** Error while parsing RSS feed */
                        e.printStackTrace();
                        Kuix.alert(Kuix.getMessage("Error in getting scores"), KuixConstants.ALERT_OK);
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
                            if(r.getTitle().equals(title))
                            {
                            ndp[i] = new CommentDataProvider();
                            ndp[i].setName(r.getDescription());
                            ndp[i].setOn(r.getTitle());
                            ndp[i].setComment(r.getDescription());
                            commentList.addItem(ndp[i]);
                            }
                        }
                        screen.setCurrent();
                    } catch (Exception e) {
                        /** Error while parsing RSS feed */
                        e.printStackTrace();
                        Kuix.alert(Kuix.getMessage("Error in fetching/parsing data"), KuixConstants.ALERT_OK);
                        //m_display.setCurrent( m_loadForm );
                    }


                  //  m_getPage = false;
                }
                stat.setText("");
                    screen.setCurrent();
                    t.sleep(5000);
            } catch (InterruptedException e) {                
                break;
            }
            }

    }

    public boolean onMessage(Object identifier, Object[] arguments) {
        if ("back".equals(identifier)) {
            running = false;
            try{ Thread.yield(); }catch(Exception ex){ex.printStackTrace();}            
            Kuix.getFrameHandler().pushFrame(new HomeFrame());
        }
        if ("addCommentOnMatch".equals(identifier)) {
            running = false;
            try{ Thread.yield(); }catch(Exception ex){ex.printStackTrace();}
            Kuix.getFrameHandler().pushFrame(new AddCommentOnMatch(title,rssItem, ld, scoreParser));
        }

        if ("showMatchComment".equals(identifier)) {
      Widget widget = Kuix.getCanvas().getDesktop().getCurrentScreen().getFocusManager().getVirtualFocusedWidget();
      DataProvider lde = widget.getDataProvider();
      Kuix.showPopupBox("/xml/showComment.xml", lde);
      //Kuix.alert(Kuix.getMessage(ld.getStringValue("news")+"|| Publish Date: "+ld.getStringValue("newspublish")+"||"+ld.getStringValue("newsdetail")), KuixConstants.ALERT_OK);
        }

      if ("share".equals(identifier)) {          
          Kuix.getFrameHandler().pushFrame(new shareThisFrame());
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
      
        // return "true" makes the FramHandler to forward the message to the next frame in the stack
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

     public void playFourSound()
     {
          try{ new Home().vibrateMobile(1000); }catch(Exception ex){ex.printStackTrace();}
            try{
            is = getClass().getResourceAsStream("/four.wav");
            player = Manager.createPlayer(is, "audio/wav");
            player.prefetch();
            player.setLoopCount(2);
            player.start();
            }catch(Exception ed){ed.printStackTrace();}
     }

     public void playSixSound()
     {
          try{ new Home().vibrateMobile(1000); }catch(Exception ex){ex.printStackTrace();}
            try{
            is = getClass().getResourceAsStream("/six.wav");
            player = Manager.createPlayer(is, "audio/wav");
            player.prefetch();
            player.setLoopCount(2);
            player.start();
            }catch(Exception ed){ed.printStackTrace();}
     }

     public void playWicketSound()
     {
          try{ new Home().vibrateMobile(1000); }catch(Exception ex){ex.printStackTrace();}
            try{
            is = getClass().getResourceAsStream("/wicket.wav");
            player = Manager.createPlayer(is, "audio/wav");
            player.prefetch();
            player.setLoopCount(2);
            player.start();
            }catch(Exception ed){ed.printStackTrace();}
     }

}
