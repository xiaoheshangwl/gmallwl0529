package com.atguigu.gmall.manager.mapper.spu;

import java.util.List;

public interface SpuInfoService {
    List<SpuInfo> getSpuInfoByC3Id(Integer c3Id);

    List<BaseSaleAttr> getBaseSaleAttr();

    void saveAllSpuInfos(SpuInfo spuInfo);

    List<SpuImage> getSpuImages(Integer spuId);
}
