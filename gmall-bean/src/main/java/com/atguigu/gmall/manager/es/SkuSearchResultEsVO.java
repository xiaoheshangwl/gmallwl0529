package com.atguigu.gmall.manager.es;

import com.atguigu.gmall.manager.BaseAttrInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SkuSearchResultEsVO implements Serializable{
     //1、搜索的skuInfo结果
    private List<SkuInfoEsVO> skuInfoEsVOs;
     //分页条上该显示的内容
    private Integer total;//返回总记录数

    private Integer pageNo;//当前是第几页的数据
//返回页面显示的所有平台属性的名和每个平台属性对应的每一个值
    private List<BaseAttrInfo> baseAttrInfos;
}
