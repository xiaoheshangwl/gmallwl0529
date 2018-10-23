package com.atguigu.gmall.item.controller;

import annotation.LoginRequire;
import com.alibaba.dubbo.config.annotation.Reference;

import com.atguigu.gmall.manager.sku.SkuAttrValueMappingTO;
import com.atguigu.gmall.manager.sku.SkuEsService;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuInfoService;

import constant.CookieConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@Controller
public class ItemController {
    @Reference
    SkuInfoService skuInfoService;
    @Reference
    SkuEsService skuEsService;

    /**
     * 商品上架（缓存到es中）
     * @param skuId
     * @return
     */

@ResponseBody
@RequestMapping("/onSale")
    public String onSale(Integer skuId){
    skuEsService.onSale(skuId);
        return  "ok";
    }

    @RequestMapping("/{skuId}.html")
    public String getSkuInfoBySkuId(@PathVariable("skuId") Integer skuId,
                                    Model model){
      log.info("您要查的商品的skuId是{}",skuId);
        SkuInfo skuInfo = null;
        try {
            skuInfo = skuInfoService.getSkuInfoBySkuId(skuId);
            if(skuInfo==null){
                return "null";
            }else{
                log.info("{}",skuInfo);
                model.addAttribute("skuInfo",skuInfo);
                Integer spuId = skuInfo.getSpuId();
                //查出当前sku对应的spu下面所有销售属性值的组合
                List<SkuAttrValueMappingTO> skuAttrValueMappingTOs = skuInfoService.getSkuAttrValueMapping(spuId);
                model.addAttribute("skuValueMapping",skuAttrValueMappingTOs);

            }

        } catch (InterruptedException e) {
            log.error("查询sku信息失败{}",e);
        }
        //增加点击率，更新es的hotScore值
        skuInfoService.incrSkuHotScore(skuId);
        return  "item";



    }

    /**
     * 写一个拦截器，在请求到达目标方法的时候，看方法是否需要登陆才能访问
     * 如果需要就进行操作
     * @return
     */
    @LoginRequire
    @RequestMapping("/haha")
    public String haha(HttpServletRequest request){
      Map<String,Object> map = (Map<String, Object>) request.getAttribute(CookieConstant.USERINFO_IN_COOKIEVALUE);
        log.info("获得到的用户信息{}",map);
      return  "haha";
    }


}
