package com.atguigu.gmall.manager.constant;

public class RedisCacheKeyConst {
    public static final String SKU_INFO_PREFIX = "sku:";
    public static final String SKU_INFO_SUFFIX = ":info";
    public static final Integer SKU_INFO_TIMEOUT = 60*60*24; //以秒为单位
    public static final Integer SKU_INFO_NULL_TIMEOUT = 60*5;
    public static final Integer LOCK_TIMEOUT=30;  //默认锁的超时时间
    public static final String LOCK_SKU_INFO = "gmall:lock:sku:";
    public static final String SKU_HOT_SCORE = "gmall:sku:hotscore:";
}
