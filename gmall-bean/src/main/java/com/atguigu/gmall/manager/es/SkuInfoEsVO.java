package com.atguigu.gmall.manager.es;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data//es中保存的skuInfo 信息
public class SkuInfoEsVO extends SuperBean{


    BigDecimal price;

    String skuName;  //全文检索 分词

    String skuDesc;   //全文检索  分词

    Integer catalog3Id;

    String skuDefaultImg;

    Long hotScore=0L;

    List<SkuBaseAttrValueEsVO> skuAttrValueEsVOs; //热度评分

}
