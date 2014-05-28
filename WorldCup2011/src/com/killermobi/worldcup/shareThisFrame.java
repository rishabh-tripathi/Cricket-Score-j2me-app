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
public class shareThisFrame implements Frame, Runnable {

   public Screen screen;
   public Thread t;
   public HttpConnection connection = null;
   public InputStream in = null;
   public String no=null,email=null,msg=null;
   public boolean mSending;
   public String mPort = "1234";


    public void onAdded() {
        screen = Kuix.loadScreen("shareThis.xml", null);
        screen.setCurrent();
    }


        public void run() {
        if (no != null) {
            mSending = true;
            try {
                sendSMS();
            } catch (Exception ex) {
                Kuix.alert(Kuix.getMessage("Problem in sending Text... Please try later."), KuixConstants.ALERT_OK);
                ex.printStackTrace();
            }
            mSending = false;
        }

        if(email!=null) 
        {
            
        try {
        connection = (HttpConnection) Connector.open("http://killermobi.com/mobile_app/share_via_email/WorldCup2011/"+email+"/"+msg);
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
                Kuix.getFrameHandler().pushFrame(new HomeFrame());
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
         }


    public boolean onMessage(Object identifier, Object[] arguments) {
          if ("back".equals(identifier)) {
            try{ Thread.yield(); }catch(Exception ex){ex.printStackTrace();}
            Kuix.getFrameHandler().pushFrame(new HomeFrame());
        }

          if ("send".equals(identifier)) {
              try{
              Text mob = (Text)screen.getWidget("mnumber");
              Text mail = (Text)screen.getWidget("memail");
              Text mess = (Text)screen.getWidget("mmsg");
              no = mob.getText();
              email = mail.getText().replace('@','~');
              email = email.replace('.','-');
              msg = mess.getText().replace(' ','_');
              t = new Thread(this);
              t.start();
              }catch(Exception e){
                  Kuix.alert(Kuix.getMessage("Problem in sending Text... Please try later."), KuixConstants.ALERT_OK);
                  Kuix.getFrameHandler().pushFrame(new HomeFrame());
              }
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


    private void sendSMS() {
        String address = "sms://" + no + ":" + mPort;
        MessageConnection conn = null;
        try {
            /** Open the message connection. */
            conn = (MessageConnection) Connector.open(address);
            TextMessage txtmessage = (TextMessage) conn.newMessage(
                    MessageConnection.TEXT_MESSAGE);
            txtmessage.setAddress(address);
            msg = "Hi, Check this out! its an awesome app on WorldCup, Download WorldCup2011 from http://killermobi.com/m/downlaods/WorldCup2011 "+msg+" Download Killer Mobile Software for FREE @ KillerMobi (www.killermobi.com)";
            txtmessage.setPayloadText(msg);
            conn.send(txtmessage);
        } catch (Throwable t) {
            System.out.println("Send caught: ");
            t.printStackTrace();
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException ioe) {
                System.out.println("Closing connection caught: ");
                ioe.printStackTrace();
            }
        }
    }

}
