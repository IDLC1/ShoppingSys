package com.taotao.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.IDUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemExample;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ItemServiceImpl implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public TbItem getItemById(long itemId) {
//        TbItem item = itemMapper.selectByPrimaryKey(itemId);
        TbItemExample example = new TbItemExample();
        // 添加查询条件
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> list = itemMapper.selectByExample(example);
        if (list != null &&list.size() > 0) {
            TbItem item = list.get(0);
            return item;
        }
        return null;
    }

    @Override
    public EUDataGridResult getItemList(int page, int rows) {
        // 查询商品列表
        TbItemExample example = new TbItemExample();
        // 分页处理
        PageHelper.startPage(page, rows);
        List<TbItem> list = itemMapper.selectByExample(example);
        // 创建返回值对象
        EUDataGridResult result = new EUDataGridResult();
        result.setRows(list);
        // 取记录总数
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setPages(pageInfo.getPages());
        return result;
    }

    @Override
    public TaotaoResult createItem(TbItem item, String itemDesc, String itemParam) throws Exception {
        // 生成商品id
        Long itemId = IDUtils.genItemId();
        logger.info("商品id = " + String.valueOf(itemId));
        item.setId(itemId);
        // 1 正常 2 下架  3 删除
        item.setStatus((byte) 1);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        // 插入到数据库
        itemMapper.insert(item);
        // 添加商品描述
        TaotaoResult taotaoResult = insertItemDesc(itemId, itemDesc);
        if (taotaoResult.getStatus() != 200) {
            throw new Exception();
        }
        // 添加规格参数
        taotaoResult = insertItemParamItem(itemId, itemParam);
        if (taotaoResult.getStatus() != 200) {
            throw new Exception();
        }
        return TaotaoResult.ok();
    }

    private TaotaoResult insertItemDesc(long itemId,String desc) {
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());
        tbItemDescMapper.insert(tbItemDesc);
        return TaotaoResult.ok();
    }

    private TaotaoResult insertItemParamItem(Long itemId, String itemParam) {
        TbItemParamItem itemParamItem = new TbItemParamItem();
        itemParamItem.setItemId(itemId);
        itemParamItem.setParamData(itemParam);
        itemParamItem.setCreated(new Date());
        itemParamItem.setUpdated(new Date());
        tbItemParamItemMapper.insert(itemParamItem);
        return TaotaoResult.ok();
    }
}
