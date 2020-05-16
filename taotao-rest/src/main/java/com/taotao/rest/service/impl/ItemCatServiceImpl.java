package com.taotao.rest.service.impl;

import com.taotao.mapper.TbItemCatMapper;
import com.taotao.pojo.TbItemCat;
import com.taotao.pojo.TbItemCatExample;
import com.taotao.rest.pojo.CatNode;
import com.taotao.rest.pojo.CatResult;
import com.taotao.rest.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示左侧商品分类列表
 */
@Service
public class ItemCatServiceImpl implements ItemCatService {

    @Autowired
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public CatResult getItemCatList() {
        CatResult catResult = new CatResult();
        catResult.setData(getCatList(0));
        return catResult;
    }

    /**
     * 递归查询分类
     * @param parentId
     * @return
     */
    private List<?> getCatList(long parentId) {
        // 创建查询条件
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        // 执行查询
        // 先取出父节点是 parentId 的节点
        List<TbItemCat> lists = tbItemCatMapper.selectByExample(example);
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
