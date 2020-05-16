package com.taotao.controller;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/content")
@Controller
public class ContentController {

    @Autowired
    ContentService contentService;

    @RequestMapping("/query/list")
    @ResponseBody
    public EUDataGridResult getItemList(@RequestParam("categoryId") Long id, int page, int rows) {
        EUDataGridResult result = contentService.getContentList(id, page, rows);
        return result;
    }

    @RequestMapping("/save")
    @ResponseBody
    public TaotaoResult saveItem(TbContent tbContent) {
        TaotaoResult result = contentService.saveContentItem(tbContent);
        return result;
    }
}
