package com.taotao.portal.controller;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.portal.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @Autowired
    private ContentService contentService;

    public static final Logger Log = LoggerFactory.getLogger(IndexController.class);

//    默认会通过 /index 接口 找返回的那个文件位置
    @RequestMapping("/index")
    public String showIndex(Model model) {
        String adJson = contentService.getContentList();
        model.addAttribute("ad1", adJson);
        return "index";
    }

    @RequestMapping("/httpclient/post")
    @ResponseBody
    public TaotaoResult testPost() {
        return TaotaoResult.ok();
    }

    @RequestMapping(value = "/httpclient/postparm", produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String testPostParm(String name, String pwd) {
        Log.info("testPost   Parmname = " + name);
        Log.info("testPostParm    pwd = " + pwd);
        return "{username:" + name + ",pwd:" + pwd +"}";
    }
}
