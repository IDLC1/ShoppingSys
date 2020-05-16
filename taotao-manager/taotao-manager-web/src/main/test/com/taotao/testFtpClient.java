package com.taotao;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.FtpUtils;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.UUID;

public class testFtpClient {

    private static final org.slf4j.Logger log = LoggerFactory.getLogger(testFtpClient.class);

    @Test
    public void testXFtpClient() throws JSchException, SftpException {
        String host = "www.greattom.xyz";
        int port = 22;
        String username = "root";
        String passwd = "Chat#*12";
        String remoteDir = "/home/ftpuser/img";
        String imageUrl = "http://http://49.233.178.108/images/";

        File file = new File("E:/study/img/1.jpg");
        // 生成新的文件名
        // 取文件的原始文件名
        String oldName = file.getName();
        // 生成新的文件名
        String newName = UUID.randomUUID().toString();
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        PictureResult res = null;
        try {
            res = FtpUtils.upload(host,port,username,passwd,remoteDir,imageUrl,new FileInputStream(file),newName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("上传失败！");
        }
        System.out.println(res);
    }

//    @Test
//    public void testXFtpDownLoad() {
//        FtpUtils ftpUtils = new FtpUtils();
//        R res = ftpUtils.download("/home/ftpuser/img","demo.zip","E:/study/img/download");
//        System.out.println(res);
//    }
}
