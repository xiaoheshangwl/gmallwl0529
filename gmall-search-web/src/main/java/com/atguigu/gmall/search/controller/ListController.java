package com.atguigu.gmall.search.controller;

import annotation.LoginRequire;
import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVO;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVO;
import com.atguigu.gmall.manager.sku.SkuEsService;
import com.atguigu.gmall.manager.sku.SkuInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ListController {
    @Reference
    SkuEsService skuEsService;
    /**
     * 将所有页面所有可能提交的查询数据封装入参
     * @param paramEsVO
     * @return
     */
    @RequestMapping("/list.html")
    public String listPage(SkuSearchParamEsVO paramEsVO, Model model){

        SkuSearchResultEsVO skuSearchResultEsVO  = skuEsService.searchSkuFromEs(paramEsVO);
        model.addAttribute("skuSearchResultEsVO",skuSearchResultEsVO);
        return "list";
    }
    @LoginRequire
    @RequestMapping("/hehe")
    public String hehe(){
        return "hehe";
    }


}
