package com.atguigu.gmall.manager.sku;

import com.atguigu.gmall.SuperBean;
import lombok.Data;

import java.util.List;
/*
传输数据,需要实现序列化接口
 */

@Data
public class SkuAllSaleAttrAndValueTO extends SuperBean{
 /*   id  spu_id  sale_attr_id  sale_attr_name  ssav_id  sale_attr_id
    sale_attr_value_name  sku_id  is_check*/

 private  Integer spuId;
 private Integer saleAttrid;
 private String saleAttrName;
 private List<SkuAllSaleAttrValueContent> skuAllSaleAttrValueContents;

}
