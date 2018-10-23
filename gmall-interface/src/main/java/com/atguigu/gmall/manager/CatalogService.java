package com.atguigu.gmall.manager;

import java.util.List;

public interface CatalogService {
    public List<BaseCatalog1> getCatalog1();

    public List<BaseCatalog2> getCatalog2(Integer catalog1Id);

    public List<BaseCatalog3> getCatalog3(Integer catalog2Id);

    public List<BaseAttrInfo> getAttrList(Integer catalog3Id);
}
