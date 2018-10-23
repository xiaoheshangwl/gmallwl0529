package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.es.SkuBaseAttrValueEsVO;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVO;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVO;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttr;

import java.util.List;

public interface SkuInfoService {
    List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(Integer catalog3Id);

    List<SpuSaleAttr> getSpuSaleAttrBySpuId(Integer spuId);

   SkuInfo getSkuInfoBySkuId(Integer skuId) throws InterruptedException;

    void saveBigSkuinfo(SkuInfo skuInfo);

    List<SkuInfo> getSkuInfosBySpuId(Integer spuId);

    List<SkuAttrValueMappingTO> getSkuAttrValueMapping(Integer spuId);


    List<SkuBaseAttrValueEsVO> getSkuBaseAttrValueIds(Integer skuId);


    List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(List<Integer> valueIds);

    /**
     * 增加某个商品的热度
     * @param skuId
     */
    void incrSkuHotScore(Integer skuId);
}
