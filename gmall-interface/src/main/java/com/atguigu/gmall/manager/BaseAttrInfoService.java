package com.atguigu.gmall.manager;

import java.util.List;

public interface BaseAttrInfoService {
    public List<BaseAttrInfo> getBaseAttrInfoByCatalog3id(Integer catalog3id);

    public List<BaseAttrValue> getBaseAttrValueByAttrId(Integer BaseInfoAttrId);

    void saveOrUpdateAttrInfoAndValue(BaseAttrInfo baseAttrInfo);

    void addAttrValues(BaseAttrInfo baseAttrInfo);


}
