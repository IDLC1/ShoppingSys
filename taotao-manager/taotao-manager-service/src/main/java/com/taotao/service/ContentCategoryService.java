package com.taotao.service;

import com.taotao.common.pojo.EUTreeNode;
import com.taotao.common.pojo.TaotaoResult;

import java.util.List;

public interface ContentCategoryService {

    /**
     * 获取展示内容列表
     *
     * @param parentId
     * @return
     */
    List<EUTreeNode> getCategoryList(long parentId);

    /**
     * 添加展示内容节点
     * @param parentId
     * @param name
     * @return
     */
    TaotaoResult insertContentCategory(Long parentId, String name);

    /**
     * 删除展示内容节点
     * @param parentId
     * @return
     */
    TaotaoResult removeContentCategory(Long parentId, Long nodeId);

    /**
     * 重命名节点
     * @param id
     * @param name
     * @return
     */
    TaotaoResult renameContentCategory(Long id, String name);


    // TODO: 测试
    List<Long> getNeedRemoveItemIdList(Long nodeId);
}
