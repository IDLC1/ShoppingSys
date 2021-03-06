package com.taotao;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.taotao.mapper.TbItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemExample;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class TestPageHelper {

    @Test
    public void testPageHelper() {
        // 创建一个spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        // 从spring容中获取mapper的代理对象
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        // 执行查询，并分页
        TbItemExample example = new TbItemExample();
        // 分页处理
        PageHelper.startPage(0, 10);
        List<TbItem> list = tbItemMapper.selectByExample(example);
        // 去商品列表
        for (TbItem tbItem : list) {
            System.out.println(tbItem.getTitle());
        }
        PageInfo<TbItem> pageInfo = new PageInfo<>(list);
        long total = pageInfo.getTotal();
        System.out.println("共有商品：" + total);
    }

    @Test
    public void testJDBC() {
        // 创建一个spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        // 从spring容中获取mapper的代理对象
        TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
        TbItem tbItem = tbItemMapper.selectByPrimaryKey((long) 536563);
        System.out.println(tbItem);
    }
}
