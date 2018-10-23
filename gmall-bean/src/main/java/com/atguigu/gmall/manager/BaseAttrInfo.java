package com.atguigu.gmall.manager;

import com.atguigu.gmall.SuperBean;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.util.List;

@Data
public class BaseAttrInfo extends SuperBean{


    private String attrName;

    private Integer catalog3Id;
    @TableField(exist = false)
    private List<BaseAttrValue> attrValues;

}
