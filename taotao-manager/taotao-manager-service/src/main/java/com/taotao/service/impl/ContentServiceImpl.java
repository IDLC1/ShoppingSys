package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    TbContentMapper tbContentMapper;

    @Override
    public EUDataGridResult getContentList(Long id, int page, int rows) {
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(id);
        // 分页处理
        PageHelper.startPage(page, rows);
        // 查询分类列表
        List<TbContent> lists = tbContentMapper.selectByExample(example);
        // 创建返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(lists);
        // 取记录总数
        PageInfo pageInfo = new PageInfo(lists);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());

        return result;
    }

    @Override
    public TaotaoResult saveContentItem(TbContent content) {
        content.setCreated(new Date());
        content.setUpdated(new Date());
        tbContentMapper.insert(content);
        return TaotaoResult.ok();
    }
}
