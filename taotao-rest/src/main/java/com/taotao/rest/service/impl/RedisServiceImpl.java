package com.taotao.rest.service.impl;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.ExceptionUtil;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceImpl implements RedisService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_INDEX_CONTENT_KEY}")
    private String REDIS_INDEX_CONTENT_KEY;

    @Override
    public TaotaoResult syncContent(Long contentCid) {
        try {
            jedisClient.hdel(REDIS_INDEX_CONTENT_KEY, contentCid+"");
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
        return TaotaoResult.ok();
    }
}
