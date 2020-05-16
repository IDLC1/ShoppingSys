package com.taotao;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.mapper.TbItemMapper;
import com.taotao.service.ContentCategoryService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class testItem {

    public static final Logger Log = LoggerFactory.getLogger(testItem.class);

    @Test
    public void testItems() {
        // 创建一个spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        // 从spring容中获取mapper的代理对象
        ContentCategoryService contentCategoryService = applicationContext.getBean(ContentCategoryService.class);
        TaotaoResult res = contentCategoryService.removeContentCategory(Long.valueOf(96),Long.valueOf(129));
    }
}
