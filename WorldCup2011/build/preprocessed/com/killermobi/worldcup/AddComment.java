/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.killermobi.worldcup;

import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.media.Manager;
import javax.microedition.media.Player;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.core.model.DataProvider;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.TextField;
import org.kalmeo.util.frame.Frame;

/**
 *
 * @author rishabh
 */
public class AddComment implements Frame, Runnable {

   public Screen screen;
   public String title;
   public RssItem rssItem;
   public DataProvider ld;
   public RssFeedParser scoreParser;
   public Thread t;
   public HttpConnection connection = null;
   public InputStream in = null;
   public TextField name;
   public TextField comment;
   public TextField on;
   public String name1;
   public String comment1;
   public String on1;



    public void onAdded() {
        screen = Kuix.loadScreen("addCommentHome.xml", null);
        screen.setCurrent();
    }


        public void run() {
       try {
        connection = (HttpConnection) Connector.open("http://killermobi.com/worldcup2011/comment/mobile/"+name1+"/"+on1+"/"+comment1);
        /** Some web servers requires these properties */
            connection.setRequestProperty("User-Agent",
                    "Profile/MIDP-2.0 Configuration/CLDC-1.0");
            connection.setRequestProperty("Content-Length", "0");
            connection.setRequestProperty("Connection", "close");

        in = connection.openInputStream();
        int length = (int) connection.getLength();
            byte[] data = new byte[length];
            in.read(data);
            String response = new String(data);
            if (response != null)
            {
                Kuix.alert(Kuix.getMessage(response), KuixConstants.ALERT_OK);
                name.setText("");
                comment.setText("");
            }
            else
            {
                Kuix.alert(Kuix.getMessage("No response from server. May be due to internet/gprs connectivity. Please try later."), KuixConstants.ALERT_OK);
            }
      }catch (Exception error) {
         error.printStackTrace();
      }
      finally
      {
           if (in != null)
           {
               try{in.close();}catch(Exception ex){}}
           if (connection != null)
           {try{connection.close();}catch(Exception ee){}}
      }
         }


    public boolean onMessage(Object identifier, Object[] arguments) {

          if ("addHome".equals(identifier)) {
             try{
          name = (TextField)screen.getWidget("name");
          comment = (TextField)screen.getWidget("comment");
          on = (TextField)screen.getWidget("on");
          name1 = name.getText().replace(' ','_');
          comment1 = comment.getText().replace(' ', '_');
          on1 = on.getText().replace(' ', '_');
          t = new Thread(this);
          t.start();
          }catch(Exception e){e.printStackTrace();}     
        }

        if ("back".equals(identifier)) {           
            Kuix.getFrameHandler().pushFrame(new HomeFrame());
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
    
        return true;
    }

    public void onRemoved() {
        // destroy the screen
        screen.cleanUp();
        // unreference the screen object to free the memory
        screen = null;
    }
}
