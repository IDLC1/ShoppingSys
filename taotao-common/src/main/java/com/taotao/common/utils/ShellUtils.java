package com.taotao.common.utils;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ShellUtils {
    private static JSch jsch;
    private static Session session;

    /**
     *      * 连接到指定的IP
     *      *
     *      * @throws JSchException
     *     
     */
    public static void connect(String user, String passwd, String host) throws JSchException, JSchException {
        jsch = new JSch();
        session = jsch.getSession(user, host, 22);
        session.setPassword(passwd);

        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        session.setConfig(config);

        session.connect();
    }

    /**
     *      * 执行相关的命令
     *      * @throws JSchException
     *     
     */
    public static void execCmd(String command, String user, String passwd, String host) throws JSchException {
        connect(user, passwd, host);

        BufferedReader reader = null;
        Channel channel = null;

        try {
            while (command != null) {
                channel = session.openChannel("exec");
                ((ChannelExec) channel).setCommand(command);

                channel.setInputStream(null);
                ((ChannelExec) channel).setErrStream(System.err);

                channel.connect();
                InputStream in = channel.getInputStream();
                reader = new BufferedReader(new InputStreamReader(in));
                String buf = null;
                while ((buf = reader.readLine()) != null) {
                    System.out.println(buf);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSchException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            channel.disconnect();
            session.disconnect();
        }
    }
}
