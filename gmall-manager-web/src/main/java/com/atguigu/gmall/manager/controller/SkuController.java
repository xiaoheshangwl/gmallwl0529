package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.mapper.spu.SpuImage;
import com.atguigu.gmall.manager.mapper.spu.SpuInfoService;
import com.atguigu.gmall.manager.mapper.spu.SpuSaleAttr;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequestMapping("/sku")
@RestController
public class SkuController {
    @Reference
    SkuInfoService SkuInfoService;
    @Reference
    SpuInfoService spuInfoService;
@RequestMapping("/getSkuInfoBySkuId")
    public List<SkuInfo> getSkuInfosBySpuId(@RequestParam("spuId") Integer spuId){
    return  SkuInfoService.getSkuInfosBySpuId(spuId);
    }

    @RequestMapping("/bigsave")
    public  String skuBigSave(@RequestBody SkuInfo skuInfo){
        log.debug("页面提交过来的skuInfo信息{}",skuInfo);
        SkuInfoService.saveBigSkuinfo(skuInfo);
        return "ok";
    }


   @RequestMapping("/base_attr_info.json")
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3Id(@RequestParam("id") Integer catalog3Id){
           return SkuInfoService.getBaseAttrInfoByCatalog3Id(catalog3Id);
    }
    @RequestMapping("/sale_attr_info.json")
    public List<SpuSaleAttr> getSpuSaleAttrBySpuId(@RequestParam("id") Integer spuId){
        return SkuInfoService.getSpuSaleAttrBySpuId(spuId);
    }
    @RequestMapping("/getSpuImages")
    public List<SpuImage> getSpuImages(@RequestParam("spuId") Integer spuId){
       return  spuInfoService.getSpuImages(spuId);
    }
}
