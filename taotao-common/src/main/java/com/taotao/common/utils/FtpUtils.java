package com.taotao.common.utils;

import com.jcraft.jsch.*;
import com.taotao.common.pojo.PictureResult;
import com.taotao.common.pojo.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.*;

public class FtpUtils {
    private static final Logger Log = LoggerFactory.getLogger(FtpUtils.class);

    /**
     * upload
     * @return
     */
    public static PictureResult upload(String host, int port, String username, String password, String remoteDir, String imgUrl, InputStream file, String TargetName) {
        // 建立连接
        Session session = getSession(host, port, username, password);
        Channel channel = getChannel(session);
        ChannelSftp sftp = (ChannelSftp) channel;

        // 上传图片
        PictureResult res = uploadFile(sftp, remoteDir, imgUrl,file, TargetName);

        // 关闭连接
        closeAll(session, channel, sftp);
        return res;
    }

    /**
     * @param
     * @param remoteDir
     *            上传目录
     * @param file
     *            上传文件
     * @return
     */
    public static PictureResult uploadFile(ChannelSftp sftp, String remoteDir, String imgUrl, InputStream file, String TargetName) {
        try {
            String[] folders = remoteDir.split( "/" );
            ArrayList<String> lists = new ArrayList<>();
            for (String folder : folders) {
                lists.add(folder);
            }
            try {
                sftp.cd( "/" );
                sftp.cd( remoteDir );
            }
            catch ( SftpException e ) {
                Log.error("error!!!!! e = " + e);
                // 若进入失败，则调用安全进入目录的方法，此时目录没有则会被创建
                safeCDRemoteDir(sftp,"", 0, lists);
            }
            sftp.put(file, TargetName);
            return PictureResult.ok(imgUrl+TargetName);
        } catch (Exception e) {
            Log.debug("上传失败！", e);
            return PictureResult.error("上传失败！");
        }
    }

    /**
     * 下载文件
     *
     * @param directory
     *            下载目录
     * @param downloadFile
     *            下载的文件
     * @param saveFile
     *            存在本地的路径
     */
    public R download(String directory, String downloadFile,
                      String saveFile) {
//        init();
//        try {
//            sftp.cd(directory);
//            sftp.get(downloadFile, saveFile);
            return R.success("下载成功！");
//        } catch (Exception e) {
//            System.out.println(e);
//            return R.success("下载失败！");
//        } finally {
//            close();
//        }
    }

    public static Channel getChannel(Session session) {
        Channel channel = null;
        try {
            channel = session.openChannel("sftp");
            channel.connect();
//            Log.info("get Channel success!");
        } catch (JSchException e) {
            Log.info("get Channel fail!", e);
        }
        return channel;
    }

    public static Session getSession(String host, int port, String username,
                              final String password) {
        Session session = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            session = jsch.getSession(username, host, port);
            session.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            session.setConfig(sshConfig);
            session.connect();
//            Log.info("Session connected!");
        } catch (JSchException e) {
            Log.info("get Channel failed!", e);
        }
        return session;
    }

    /**
     * 删除文件
     *
     * @param directory
     *            要删除文件所在目录
     * @param deleteFile
     *            要删除的文件
     * @param sftp
     */
    public String delete(String directory, String deleteFile, ChannelSftp sftp) {
        String result = "";
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
            result = "删除成功！";
        } catch (Exception e) {
            result = "删除失败！";
            Log.info("删除失败！", e);
        }
        return result;
    }

    private static void closeChannel(Channel channel) {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private static void closeSession(Session session) {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public static void closeAll(Session session, Channel channel, ChannelSftp sftp) {
        try {
            closeChannel(sftp);
            closeChannel(channel);
            closeSession(session);
        } catch (Exception e) {
            Log.info("closeAll", e);
        }
    }

    public static int safeCDRemoteDir(ChannelSftp sftp, String currPath, int index, ArrayList<String> pathList) throws SftpException {
        int size = pathList.size();
        for (int i=index; i < size; i++) {
            System.out.println(pathList.get(i));
            if (pathList.get(i).length() > 0) {
                try {
                    // 逐层进入
                    sftp.cd( pathList.get(i) );
                    // 若成功，则更改当前路径
                    currPath += "/" + pathList.get(i);
                }
                catch ( SftpException e ) {
                    // 创建对应的目录
                    sftp.mkdir( currPath + "/" + pathList.get(i));
                    System.out.println(pathList.toString());
                    return safeCDRemoteDir(sftp, currPath,i,pathList);
                }
            }
        }
        return 1;
    }
}
