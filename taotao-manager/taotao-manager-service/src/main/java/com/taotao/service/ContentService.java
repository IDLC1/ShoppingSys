package com.taotao.service;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {

    public EUDataGridResult getContentList(Long id, int page, int rows);

    public TaotaoResult saveContentItem(TbContent content);
}
