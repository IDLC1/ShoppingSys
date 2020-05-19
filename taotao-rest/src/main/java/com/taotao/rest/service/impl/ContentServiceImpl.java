package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.dao.impl.JedisClientCluster;
import com.taotao.rest.service.ContentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Value("${REDIS_INDEX_CONTENT_KEY}")
    private String REDIS_INDEX_CONTENT_KEY;

    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    JedisClientCluster jedisClientCluster;

    @Autowired
    private JedisClient jedisClient;

    @Override
    public List<TbContent> getContentList(Long contentId) {
        // 从缓存中取出内容
        try {
            String result = jedisClientCluster.hget(REDIS_INDEX_CONTENT_KEY, contentId + "");
//            String result = jedisClient.hget(REDIS_INDEX_CONTENT_KEY, contentId+"");
            if (!StringUtils.isBlank(result)) {
                // 将字符串转换成list
                List<TbContent> resultList = JsonUtils.jsonToList(result, TbContent.class);
                return resultList;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据内容分类id查询内容列表
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(contentId);
        // 执行查询
        List<TbContent> list = tbContentMapper.selectByExample(example);

        // 向缓存中添加内容
        try {
            // 将list转换成字符串
            String cacheString = JsonUtils.objectToJson(list);
            // 单机版
//            jedisClient.hset(REDIS_INDEX_CONTENT_KEY, contentId + "", cacheString);

            // 集群版
            jedisClientCluster.hset(REDIS_INDEX_CONTENT_KEY, contentId + "", cacheString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
