package com.atguigu.gmall.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.BaseAttrInfoService;
import com.atguigu.gmall.manager.BaseAttrValue;
import com.atguigu.gmall.manager.vo.BaseAttInfoAndValueVO;
import com.atguigu.gmall.manager.vo.BaseAttrValueVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@RequestMapping("/attr")
@Controller
public class AttrManagerController {
    @Reference
    BaseAttrInfoService baseAttrInfoService;

    @ResponseBody
    @RequestMapping("/addAttrValue")
    public String addAttrValue(@RequestBody BaseAttInfoAndValueVO baseAttInfoAndValueVO){
        log.info("页面提交来的数据{}",baseAttInfoAndValueVO);
        if(baseAttInfoAndValueVO.getId()!=null){
            if(baseAttInfoAndValueVO.getAttrName()!=""){
                BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
                BeanUtils.copyProperties(baseAttInfoAndValueVO,baseAttrInfo);
                List<BaseAttrValue> baseAttrValues = new ArrayList<>();
                for (BaseAttrValueVO baseAttrValueVO : baseAttInfoAndValueVO.getAttrValues()) {
                    BaseAttrValue baseAttrValue = new BaseAttrValue();
                    BeanUtils.copyProperties(baseAttrValueVO,baseAttrValue);
                    baseAttrValues.add(baseAttrValue);
                }
                baseAttrInfo.setAttrValues(baseAttrValues);
                baseAttrInfoService.addAttrValues(baseAttrInfo);
            }else{
                log.info("属性名称不可为空");
            }
        }else{
            log.info("请提交合法的属性");
        }
        return "ok";
    }
    @ResponseBody
    @RequestMapping("/updates")
    public String saveOrUpdateOrDeleteAttrInfoAndValue(@RequestBody BaseAttInfoAndValueVO baseAttInfoAndValueVO){
        log.info("页面提交来的数据{}",baseAttInfoAndValueVO);
        if(baseAttInfoAndValueVO.getId()!=null){
            if(baseAttInfoAndValueVO.getAttrName()!=""){
                BaseAttrInfo baseAttrInfo = new BaseAttrInfo();
                BeanUtils.copyProperties(baseAttInfoAndValueVO,baseAttrInfo);
                List<BaseAttrValue> baseAttrValues = new ArrayList<>();
                for (BaseAttrValueVO baseAttrValueVO : baseAttInfoAndValueVO.getAttrValues()) {
                    BaseAttrValue baseAttrValue = new BaseAttrValue();
                    BeanUtils.copyProperties(baseAttrValueVO,baseAttrValue);
                    baseAttrValues.add(baseAttrValue);
                }
                baseAttrInfo.setAttrValues(baseAttrValues);
                baseAttrInfoService.saveOrUpdateAttrInfoAndValue(baseAttrInfo);
            }else{
               log.info("属性名称不可为空");
            }
        }else{
            log.info("请提交合法的属性");
        }
        return "ok";
    }
    @RequestMapping("/listPage.html")
    public String toAttrListPage(){
        return "attr/attrListPage";
    }
    @ResponseBody
    @RequestMapping("/value/{id}")
    public List<BaseAttrValue> listBaseAttrValue(@PathVariable("id") Integer id){
        return baseAttrInfoService.getBaseAttrValueByAttrId(id);
    }
}
