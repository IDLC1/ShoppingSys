package com.taotao.service.impl;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.service.ItemParamItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 展示商品规格残花
 */
@Service
public class ItemParamItemServiceImpl implements ItemParamItemService {

    @Autowired
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public String getItemParamByItemId(Long itemId) {
        // 根据商品id查询规格参数
        TbItemParamItemExample example = new TbItemParamItemExample();
        TbItemParamItemExample.Criteria criteria = example.createCriteria();
        criteria.andItemIdEqualTo(itemId);
        // 执行查询
        List<TbItemParamItem> lists = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (lists == null ||lists.size() == 0) {
            return "";
        }
        // 取规格参数
        TbItemParamItem itemParamItem = lists.get(0);
        String paramData = itemParamItem.getParamData();
        // 生成html
        //转换成java对象
        List<Map> mapList = JsonUtils.jsonToList(paramData, Map.class);
        //遍历list生成html
        StringBuffer sb = new StringBuffer();

        sb.append("<table cellpadding=\"1\" cellspacing=\"1\" width=\"100%\" border=\"1\" class=\"Ptable\">\n");
        sb.append("	<tbody>\n");
        for (Map map : mapList) {
            sb.append("		<tr>\n");
            sb.append("			<th class=\"tdTitle\" colspan=\"2\">"+map.get("group")+"</th>\n");
            sb.append("		</tr>\n");
            //取规格项
            List<Map>mapList2 = (List<Map>) map.get("params");
            for (Map map2 : mapList2) {
                sb.append("		<tr>\n");
                sb.append("			<td class=\"tdTitle\">"+map2.get("k")+"</td>\n");
                sb.append("			<td>"+map2.get("v")+"</td>\n");
                sb.append("		</tr>\n");
            }
        }
        sb.append("	</tbody>\n");
        sb.append("</table>");


        return sb.toString();
    }
}
