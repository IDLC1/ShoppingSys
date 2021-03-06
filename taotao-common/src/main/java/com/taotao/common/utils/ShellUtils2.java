package com.taotao.common.utils;

import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;

public class ShellUtils2{
    public static void main(String[] arg){

        try{
            JSch jsch=new JSch();

            //jsch.setKnownHosts("/home/foo/.ssh/known_hosts");
            Boolean autoLogin = true;
            String host=null;
            String user = "";
            String passwd = "";
            if (!autoLogin) {
                if(arg.length>0){
                    host=arg[0];
                }
                else{
                    host=JOptionPane.showInputDialog("Enter username@hostname",
                            System.getProperty("user.name")+
                                    "@localhost");
                }
                user=host.substring(0, host.indexOf('@'));
                host=host.substring(host.indexOf('@')+1);
                passwd = JOptionPane.showInputDialog("Enter password");
            } else {
                host = "www.greattom.xyz";
                user = "root";
                passwd = "Chat#*12";
            }

            Session session=jsch.getSession(user, host, 22);
            session.setPassword(passwd);


            UserInfo ui = new MyUserInfo(){
                @Override
                public void showMessage(String message){
                    JOptionPane.showMessageDialog(null, message);
                }
                @Override
                public boolean promptYesNo(String message){
                    Object[] options={ "yes", "no" };
                    int foo=JOptionPane.showOptionDialog(null,
                            message,
                            "Warning",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.WARNING_MESSAGE,
                            null, options, options[0]);
                    return foo==0;
                }

                // If password is not given before the invocation of Session#connect(),
                // implement also following methods,
                //   * UserInfo#getPassword(),
                //   * UserInfo#promptPassword(String message) and
                //   * UIKeyboardInteractive#promptKeyboardInteractive()

            };

            session.setUserInfo(ui);

            // It must not be recommended, but if you want to skip host-key check,
            // invoke following,
            // session.setConfig("StrictHostKeyChecking", "no");

            //session.connect();
            session.connect(30000);   // making a connection with timeout.

            Channel channel=session.openChannel("shell");

            // Enable agent-forwarding.
            //((ChannelShell)channel).setAgentForwarding(true);

            channel.setInputStream(System.in);
      /*
      // a hack for MS-DOS prompt on Windows.
      channel.setInputStream(new FilterInputStream(System.in){
          public int read(byte[] b, int off, int len)throws IOException{
            return in.read(b, off, (len>1024?1024:len));
          }
        });
       */

            channel.setOutputStream(System.out);

      /*
      // Choose the pty-type "vt102".
      ((ChannelShell)channel).setPtyType("vt102");
      */

      /*
      // Set environment variable "LANG" as "ja_JP.eucJP".
      ((ChannelShell)channel).setEnv("LANG", "ja_JP.eucJP");
      */

            //channel.connect();
            channel.connect(3*1000);
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static abstract class MyUserInfo
            implements UserInfo, UIKeyboardInteractive{
        @Override
        public String getPassword(){ return null; }
        @Override
        public boolean promptYesNo(String str){ return false; }
        @Override
        public String getPassphrase(){ return null; }
        @Override
        public boolean promptPassphrase(String message){ return false; }
        @Override
        public boolean promptPassword(String message){ return false; }
        @Override
        public void showMessage(String message){ }
        @Override
        public String[] promptKeyboardInteractive(String destination,
                                                  String name,
                                                  String instruction,
                                                  String[] prompt,
                                                  boolean[] echo){
            return null;
        }
    }
}
