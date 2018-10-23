package com.atguigu.gmall.cart.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall.cart.CartItem;
import com.atguigu.gmall.cart.CartService;
import com.atguigu.gmall.cart.CartVO;
import com.atguigu.gmall.cart.SkuItem;
import com.atguigu.gmall.cart.constant.CartConstant;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuInfoService;
import jdk.nashorn.internal.ir.annotations.Reference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    JedisPool jedisPool;
    @Reference
    SkuInfoService skuInfoService;

    @Override
    public String addToCartUnLogin(String cookieCartKey, Integer skuId, Integer num) {
        Jedis jedis = jedisPool.getResource();
        if (!StringUtils.isEmpty(cookieCartKey)) {
            //之前创建过购物车
            Boolean exists = jedis.exists(cookieCartKey);
            if (exists) {
                String skuInfoJson = jedis.hget(cookieCartKey, skuId + "");
                if(!StringUtils.isEmpty(skuInfoJson)){
                    //有此商品,添加数量
                    CartItem cartItem = JSON.parseObject(skuInfoJson, CartItem.class);
                    cartItem.setNum(cartItem.getNum()+num);
                    cartItem.setTotalPrice(cartItem.getTotalPrice());
                    String cartItemJson  = JSON.toJSONString(cartItem);
                    jedis.hset(cookieCartKey,skuId+"",cartItemJson);

                }else{
                    //无此商品,新增商品
                    CartItem cartItem = new CartItem();
                    SkuItem skuItem =  new SkuItem();
                    BeanUtils.copyProperties(skuId,skuItem);
                    cartItem.setSkuItem(skuItem);
                    cartItem.setNum(num);
                    cartItem.setTotalPrice(cartItem.getTotalPrice());
                    String cartItemJson = JSON.toJSONString(cartItem);
                    jedis.hset(cookieCartKey,skuItem.getId()+"",cartItemJson);
                
                }
            } else {
                //非法购物车
                //创建新购物车
                cookieCartKey =  createCart( skuId,num);

            }
            //购物车中有此商品
            //购物车中无此商品
        } else {
            //无购物车
            // 新建购物车
            cookieCartKey = createCart( skuId,num);
        }
        jedis.close();
        return cookieCartKey;
    }

    @Override
    public void addToCartLogin(int userId, Integer skuId, Integer num) {

    }

    @Override
    public CartVO getYourCart(String cartKey) {
        return null;
    }

    @Override
    public void mergeCart(String cookieCartKey, int userId) {

    }

    /**
     * 私有方法
     * @param skuId
     * @param num
     * @return
     */
    private String createCart(Integer skuId,Integer num) {
        Jedis jedis = jedisPool.getResource();
        //新建购物车
        String cookieCartKey = CartConstant.TEMP_CART_PREFIX + UUID.randomUUID().toString().substring(0, 10).replaceAll("-", "");
        //保存购物车信息
        try {
            //查出商品的详细信息
            SkuInfo skuInfo = skuInfoService.getSkuInfoBySkuId(skuId);
            CartItem cartItem = new CartItem();
            SkuItem skuItem = new SkuItem();
            //拷贝商品信息，准备保存到redis
            BeanUtils.copyProperties(skuInfo, skuItem);
            cartItem.setSkuItem(skuItem);
            cartItem.setNum(num);
            cartItem.setTotalPrice(cartItem.getTotalPrice());

            String jsonString = JSON.toJSONString(cartItem);
            Long hset = jedis.hset(cookieCartKey, skuItem.getId() + "", jsonString);
            jedis.close();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        jedis.close();
        return cookieCartKey;
    }

}
