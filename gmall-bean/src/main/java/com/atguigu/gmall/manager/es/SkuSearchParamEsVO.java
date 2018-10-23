package com.atguigu.gmall.manager.es;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuSearchParamEsVO implements Serializable{
    String keyword; //关键字搜索
    Integer catalog3Id; //三级目录id
    Integer[] valueId; //这个值会有多个
    Integer pageNo = 1;  //查询的页码
    Integer pageSize = 12; //每页展示的条目数
    String sortField = "hotScore";//排序字段
    String sortOrder = "desc";//降序

}
