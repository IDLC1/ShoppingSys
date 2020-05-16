package com.taotao.service;

import com.taotao.common.pojo.TaotaoResult;

public interface ItemParamService {
    TaotaoResult getItemParamByCid(Long cid);

    TaotaoResult insertItemPram(Long cid, String paramData);
}
