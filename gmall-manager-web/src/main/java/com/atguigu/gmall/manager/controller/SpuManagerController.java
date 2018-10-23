package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.mapper.spu.BaseSaleAttr;
import com.atguigu.gmall.manager.mapper.spu.SpuInfo;
import com.atguigu.gmall.manager.mapper.spu.SpuInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
@Slf4j
@Controller
@RequestMapping("/spu")
public class SpuManagerController {
    @Reference
    SpuInfoService spuInfoService;
    @ResponseBody
    @RequestMapping("/bigsave")
    public String saveAllSpuInfos(@RequestBody SpuInfo spuInfo){
       log.info("提交的spuInfo{}",spuInfo);
       spuInfoService.saveAllSpuInfos(spuInfo);
       return  "ok";
    }

    @ResponseBody
    @RequestMapping("/base_sale_attr")
    public List<BaseSaleAttr> getBaseSaleAttr(){
        return spuInfoService.getBaseSaleAttr();
    }

    @ResponseBody
    @RequestMapping("/info.json")
    public List<SpuInfo> getSpuInfoByC3Id(@RequestParam("c3Id") Integer c3Id){
       return spuInfoService.getSpuInfoByC3Id(c3Id);
    }
    @RequestMapping("/listPage.html")
    public String spuListPage(){
        return "spu/spuListPage";
    }

}
