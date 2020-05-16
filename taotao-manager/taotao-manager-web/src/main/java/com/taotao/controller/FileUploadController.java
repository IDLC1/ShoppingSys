package com.taotao.controller;

import com.taotao.common.pojo.PictureResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.service.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class FileUploadController {

    @Autowired
    private UploadService uploadService;

    @RequestMapping("/pic/upload")
    @ResponseBody
    public String uploadFielWithXFtp(MultipartFile uploadFile) {
        try {
            PictureResult res = uploadService.uploadFile(uploadFile);
            System.out.println(JsonUtils.objectToJson(res));
            return JsonUtils.objectToJson(res);
        } catch (Exception e) {
            e.printStackTrace();
            return "服务器异常！";
        }
    }
}
