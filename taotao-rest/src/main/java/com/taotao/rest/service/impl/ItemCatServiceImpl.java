package com.taotao.rest.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.dao.JedisClient;
import com.taotao.rest.dao.impl.JedisClientCluster;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 显示左侧商品分类列表
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    public static final Logger Log = LoggerFactory.getLogger(ItemCatServiceImpl.class);

    @Value("${REDIS_ITEM_CAT_LIST_KEY}")
    private String REDIS_ITEM_CAT_LIST_KEY;

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Autowired
    private JedisClient jedisClient;

    @Autowired
    private JedisClientCluster jedisClientCluster;

    @Override
    public CatResult getItemCatList() {
        CatResult catResult = new CatResult();
        Date date = new Date();

        long startTime=System.currentTimeMillis();
        Log.info("startTime = " + startTime);

        List<TbItem> lists = new ArrayList<>();
        lists = (List<TbItem>) getCatList(0);

//        try {
//            String itemList = jedisClient.hget("itemList","iteml");
//            if (!StringUtils.isBlank(itemList)) {
//                lists = JsonUtils.jsonToList(itemList, TbItem.class);
//            } else {
//                lists = (List<TbItem>) getCatList(0);
                // 将商品类目列表记录进redis中
//                String strList = JsonUtils.objectToJson(lists);
//                jedisClient.hset("itemList","iteml",strList);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        long endTime=System.currentTimeMillis();
        Log.info("endTime = " + endTime);
        Log.info("程序运行时间 = " + (endTime-startTime)+"ms");

        catResult.setData(lists);
        return catResult;
    }

    /**
     * 递归查询分类
     * @param parentId
     * @return
     */
    private List<?> getCatList(long parentId) {
        List<TbItemCat> lists = new ArrayList<>();
        // 先尝试从redis中取出
        try {
            // 单机版
            String itemList = jedisClient.hget(REDIS_ITEM_CAT_LIST_KEY, parentId+"");
            // 集群版
//            String itemList = jedisClientCluster.hget(REDIS_ITEM_CAT_LIST_KEY, parentId+"");

            if (!StringUtils.isBlank(itemList)) {
                lists = JsonUtils.jsonToList(itemList, TbItemCat.class);
                Log.info("redis redis 取出！ size = " + lists.size());
            } else {
                // 若不存在于缓存中，则从数据库中取出
                // 创建查询条件
                TbItemCatExample example = new TbItemCatExample();
                TbItemCatExample.Criteria criteria = example.createCriteria();
                criteria.andParentIdEqualTo(parentId);
                // 执行查询
                // 先取出父节点是 parentId 的节点
                lists = tbItemCatMapper.selectByExample(example);

                // 将商品类目列表记录进redis中
                String strList = JsonUtils.objectToJson(lists);
                // 单机版
                jedisClient.hset(REDIS_ITEM_CAT_LIST_KEY, parentId+"",strList);
                // 集群版
//                jedisClientCluster.hset(REDIS_ITEM_CAT_LIST_KEY, parentId+"",strList);
                Log.info("redis redis 存入！ size = " + lists.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List resultList = new ArrayList<>();
        // 由于界面显示，只显示14个parentId == 0
        int count = 0;
        // 循环构造成需要的格式
        for (TbItemCat tbItemCat : lists) {
            CatNode catNode = new CatNode();
            // 判断是否为父节点
            if(tbItemCat.getIsParent()) {
                if(parentId == 0) {
                    catNode.setName("<a href='/products/"+tbItemCat.getId()+".html'>"+tbItemCat.getName()+"</a>");
                } else {
                    catNode.setName(tbItemCat.getName());
                }
                catNode.setUrl("/products/" + tbItemCat.getId() + ".html");
                catNode.setItem(getCatList(tbItemCat.getId()));
                resultList.add(catNode);

                // 由于界面显示，只显示14个parentId == 0
                count++;
                if(parentId == 0 && count >= 14) {
                    break;
                }
            } else {
                resultList.add("/products/" + tbItemCat.getId() + ".html|" + tbItemCat.getName());
            }
        }
        return resultList;
    }
}
