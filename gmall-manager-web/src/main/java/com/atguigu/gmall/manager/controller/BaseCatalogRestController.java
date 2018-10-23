package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/baseCatalog")
@RestController
public class BaseCatalogRestController {
    @Reference
    CatalogService catalogService;
    @Reference
    BaseAttrInfoService baseAttrInfoService;
    @RequestMapping("/attrs.json")
    public List<BaseAttrInfo> listBaseAttrInfo(Integer id){
        return baseAttrInfoService.getBaseAttrInfoByCatalog3id(id);
    }


    @RequestMapping("/1/list.json")
    public List<BaseCatalog1> listBaseCatalog1(){
        return catalogService.getCatalog1();
    }
    @RequestMapping("/2/list.json")
    public List<BaseCatalog2> listBaseCatalog2(Integer id){
        return catalogService.getCatalog2(id);
    }
    @RequestMapping("/3/list.json")
    public List<BaseCatalog3> listBaseCatalog3(Integer id){
        return catalogService.getCatalog3(id);
    }
}
