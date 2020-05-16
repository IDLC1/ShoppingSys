package com.taotao.controller;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.service .ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/content/category")
public class ContentCategoryController {

    @Autowired
    private ContentCategoryService contentCategoryService;

    @RequestMapping("/list")
    @ResponseBody
    public List<EUTreeNode> getContentCategory(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        List<EUTreeNode> lists = contentCategoryService.getCategoryList(parentId);
        return lists;
    }

    @RequestMapping("/create")
    @ResponseBody
    public TaotaoResult addContentCategoryItem(Long parentId, String name) {
        TaotaoResult result = contentCategoryService.insertContentCategory(parentId, name);
        return result;
    }

    @RequestMapping("/delete")
    @ResponseBody
    public TaotaoResult delContentCategoryItem(Long parentId,@RequestParam(value = "id") Long nodeId) {
        TaotaoResult result = contentCategoryService.removeContentCategory(parentId, nodeId);
        return result;
    }

    @RequestMapping("/update")
    @ResponseBody
    public TaotaoResult updateContentCategoryItem(@RequestParam(value = "id") Long nodeId, String name) {
        TaotaoResult result = contentCategoryService.renameContentCategory(nodeId, name);
        return result;
    }
}
