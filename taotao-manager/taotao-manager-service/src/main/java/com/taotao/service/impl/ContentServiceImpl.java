package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.HttpClientUtil;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.service.ContentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    public static final Logger Log = LoggerFactory.getLogger(ContentServiceImpl.class);

    @Value("${REST_BASE_URL}")
    private String REST_BASE_URL;

    @Value("${REST_CONTENT_SYNC_URL}")
    private String REST_CONTENT_SYNC_URL;

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

        // 添加缓存同步逻辑
        try {
            Log.info(content.getCategoryId() + "");
            HttpClientUtil.doGet(REST_BASE_URL + REST_CONTENT_SYNC_URL + content.getCategoryId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TaotaoResult.ok();
    }
}
