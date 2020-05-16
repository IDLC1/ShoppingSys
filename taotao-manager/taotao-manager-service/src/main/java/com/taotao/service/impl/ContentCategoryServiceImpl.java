package com.taotao.service.impl;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.service.ContentCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

    public static final Logger Log = LoggerFactory.getLogger(ContentCategoryServiceImpl.class);

    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EUTreeNode> getCategoryList(long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria =  example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        // 执行查询语句
        List<TbContentCategory> list =  tbContentCategoryMapper.selectByExample(example);
        List<EUTreeNode> resultList = new ArrayList<>();
        for (TbContentCategory tbContentCategory: list) {
            EUTreeNode node = new EUTreeNode();
            node.setId(tbContentCategory.getId());
            node.setText(tbContentCategory.getName());
            node.setState(tbContentCategory.getIsParent()?"closed":"open");
            resultList.add(node);
        }
        return resultList;
    }

    /**
     * 添加展示内容节点
     * @param parentId
     * @param name
     * @return
     */
    @Override
    public TaotaoResult insertContentCategory(Long parentId, String name) {
        // 创建pojo
        TbContentCategory tbContentCategory = new TbContentCategory();
        tbContentCategory.setName(name);
        tbContentCategory.setIsParent(false);
        tbContentCategory.setStatus(1);
        tbContentCategory.setParentId(parentId);
        tbContentCategory.setSortOrder(1);
        tbContentCategory.setCreated(new Date());
        tbContentCategory.setUpdated(new Date());
        // 插入数据库
        tbContentCategoryMapper.insert(tbContentCategory);

        // 查看父节点的isParent列是否为true
        TbContentCategory tbParnet = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if(!tbParnet.getIsParent()) {
            tbParnet.setIsParent(true);
            tbContentCategoryMapper.updateByPrimaryKey(tbParnet);
        }

        return TaotaoResult.ok(tbContentCategory);
    }

    /**
     * 删除节点
     * @param parentId
     * @param nodeId
     * @return
     */
    @Override
    public TaotaoResult removeContentCategory(Long parentId, Long nodeId) {
        // 获取所有要删除的节点内容
        List<Long> lists = getNeedRemoveItemIdList(nodeId);

        // 删除当前节点
        TbContentCategoryExample tbRemoveExample = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria rmCriteria = tbRemoveExample.createCriteria();
        rmCriteria.andIdIn(lists);
        int count = tbContentCategoryMapper.deleteByExample(tbRemoveExample);

        // 若删除成功
        if(count > 0) {
            // 先查出父节点中子节点的个数
            TbContentCategoryExample parentExample = new TbContentCategoryExample();
            TbContentCategoryExample.Criteria parentCriteria = parentExample.createCriteria();
            parentCriteria.andParentIdEqualTo(parentId);
            Long parentNodeNumber = tbContentCategoryMapper.countByExample(parentExample);

            // 若父节点中没有子节点了，修改父节点内容
            if (parentNodeNumber == 0) {
                TbContentCategory tb = tbContentCategoryMapper.selectByPrimaryKey(parentId);
                tb.setIsParent(false);
                tb.setUpdated(new Date());
                count = tbContentCategoryMapper.updateByPrimaryKey(tb);
                if(count > 0) {
                    return TaotaoResult.ok("删除成功！");
                }
            }
        }
        return TaotaoResult.ok();
    }

    /**
     * 获取所有要删除的id
     * @param nodeId
     * @return
     */
    @Override
    public List<Long> getNeedRemoveItemIdList(Long nodeId) {
        List<Long> idList = new ArrayList<>();

        // 先添加自己
        idList.add(nodeId);
        // 判断当前节点是否是其他节点的父节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria =  example.createCriteria();
        criteria.andParentIdEqualTo(nodeId);
        List<TbContentCategory> list = tbContentCategoryMapper.selectByExample(example);
        // 若当前节点是其他节点的父节点
        if(list.size() > 0) {
            // 循环删除子节点
            for (TbContentCategory item : list) {
                // 添加自己
                idList.addAll(getNeedRemoveItemIdList(item.getId()));
            }
        }

        return idList;
    }

    /**
     * 更新列表
     * @param id
     * @param name
     * @return
     */
    @Override
    public TaotaoResult renameContentCategory(Long id, String name) {
        // 查找用户
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(id);
        tbContentCategory.setName(name);
        int count = tbContentCategoryMapper.updateByPrimaryKey(tbContentCategory);
        if (count > 0) {
            return TaotaoResult.ok("更新成功！");
        }
        return TaotaoResult.ok();
    }
}

