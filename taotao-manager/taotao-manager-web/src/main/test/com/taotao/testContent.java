package com.taotao;

import com.taotao.common.pojo.EUDataGridResult;
import com.taotao.service.ContentCategoryService;
import com.taotao.service.ContentService;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class testContent {

    public static final Logger Log = LoggerFactory.getLogger(testContent.class);

    @Test
    public void testContentList() {
        // 创建一个spring容器
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-*.xml");
        // 从spring容中获取mapper的代理对象
        ContentService contentService = applicationContext.getBean(ContentService.class);

        EUDataGridResult result = contentService.getContentList(Long.valueOf(89), 0, 10);
    }
}
