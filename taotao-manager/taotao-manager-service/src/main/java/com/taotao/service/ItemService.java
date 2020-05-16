package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbItem;

public interface ItemService {
    TbItem getItemById(long itemId);

    EUDataGridResult getItemList(int page, int rows);

    // 创建商品
    TaotaoResult createItem(TbItem item, String Desc, String itemParam) throws Exception;
}
