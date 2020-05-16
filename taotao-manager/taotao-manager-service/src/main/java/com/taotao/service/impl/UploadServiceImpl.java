package com.taotao.service.impl;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.FtpUtils;
import com.taotao.service.UploadService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
public class UploadServiceImpl implements UploadService {

    @Value("${ftp.host}")
    private String host;

    @Value("${ftp.port}")
    private int port;

    @Value("${ftp.username}")
    private String username;

    @Value("${ftp.passwd}")
    private String password;

    @Value("${ftp.remoteDir}")
    private String remoteDir;

    @Value("${images.url}")
    private String imgUrl;

    @Override
    public PictureResult uploadFile(MultipartFile uploadFile) {
        // 生成新的文件名
        // 取文件的原始文件名
        String oldName = uploadFile.getOriginalFilename();
        // 生成新的文件名
        String newName = UUID.randomUUID().toString();
        newName = newName + oldName.substring(oldName.lastIndexOf("."));
        // 图片上传
        PictureResult res = null;
        try {
            res = FtpUtils.upload(host,port,username,password,remoteDir, imgUrl,uploadFile.getInputStream(), newName);
        } catch (IOException e) {
            e.printStackTrace();
            return res;
        }
        return res;
    }
}
