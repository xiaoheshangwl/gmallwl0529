package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.manager.es.SkuSearchParamEsVO;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVO;

public interface SkuEsService {
    void onSale(Integer skuId);

    SkuSearchResultEsVO searchSkuFromEs(SkuSearchParamEsVO paramEsVO);

    void updateHotScore(Integer skuId, Long hincrBy);
}
