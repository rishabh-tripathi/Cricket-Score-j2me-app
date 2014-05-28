/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.killermobi.worldcup;

import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;
import org.kalmeo.kuix.core.Kuix;
import org.kalmeo.kuix.core.KuixConstants;
import org.kalmeo.kuix.widget.Screen;
import org.kalmeo.kuix.widget.Text;
import org.kalmeo.kuix.widget.TextField;
import org.kalmeo.util.frame.Frame;

/**
 *
 * @author rishabh
 */
public class aboutFrame implements Frame{

   public Screen screen;
   public Thread t;
   public HttpConnection connection = null;
   public InputStream in = null;
   public String no=null,email=null,msg=null;
   public boolean mSending;
   public String mPort = "1234";


    public void onAdded() {
        screen = Kuix.loadScreen("showAbout.xml", null);
        screen.setCurrent();
    }


    public boolean onMessage(Object identifier, Object[] arguments) {
          if ("home".equals(identifier)) {
            try{ Thread.yield(); }catch(Exception ex){ex.printStackTrace();}
            Kuix.getFrameHandler().pushFrame(new HomeFrame());
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
