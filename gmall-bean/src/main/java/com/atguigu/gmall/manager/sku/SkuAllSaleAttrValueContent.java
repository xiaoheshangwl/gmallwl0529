package com.atguigu.gmall.manager.sku;

import lombok.Data;

import java.io.Serializable;

@Data
public class SkuAllSaleAttrValueContent implements Serializable{
    private Integer saleAttrValueId;
    private String saleAttrValueName;
    private Integer skuId;
    private Integer isCheck;
}
