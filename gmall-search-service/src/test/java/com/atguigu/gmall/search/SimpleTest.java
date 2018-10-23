package com.atguigu.gmall.search;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVO;
import com.atguigu.gmall.search.service.impl.SkuEsServiceImpl;
import org.junit.Test;

public class SimpleTest {
    @Test
    public void testDSL(){
        SkuEsServiceImpl skuEsService = new SkuEsServiceImpl();
        SkuSearchParamEsVO paramEsVO = new SkuSearchParamEsVO();
        paramEsVO.setCatalog3Id(1);
        String s = skuEsService.buildSearchQueryDSL(paramEsVO);
        System.out.println(s);
    }
}
