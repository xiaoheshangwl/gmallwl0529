package com.atguigu.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.atguigu.gmall.manager.BaseAttrInfo;
import com.atguigu.gmall.manager.es.SkuBaseAttrValueEsVO;
import com.atguigu.gmall.manager.es.SkuInfoEsVO;
import com.atguigu.gmall.manager.es.SkuSearchParamEsVO;
import com.atguigu.gmall.manager.es.SkuSearchResultEsVO;
import com.atguigu.gmall.manager.sku.SkuEsService;
import com.atguigu.gmall.manager.sku.SkuInfo;
import com.atguigu.gmall.manager.sku.SkuInfoService;
import com.atguigu.gmall.search.constant.EsConstant;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.Update;
import io.searchbox.core.search.aggregation.MetricAggregation;
import io.searchbox.core.search.aggregation.TermsAggregation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.*;

import org.elasticsearch.search.aggregations.bucket.terms.TermsBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class SkuEsServiceImpl implements SkuEsService{
    @Autowired
    JestClient jestClient;
    @Reference
    SkuInfoService skuInfoService;


    @Override
    public void onSale(Integer skuId) {
        try {
            SkuInfo skuInfo = skuInfoService.getSkuInfoBySkuId(skuId);
            log.info("查询到的skuInfo的信息{}",skuInfo);
            SkuInfoEsVO skuInfoEsVO = new SkuInfoEsVO();
            BeanUtils.copyProperties(skuInfo,skuInfoEsVO);
           //查出当前skuInfo的所有平台属性的值
            List<SkuBaseAttrValueEsVO> valueIdVOs = skuInfoService.getSkuBaseAttrValueIds(skuId);
            skuInfoEsVO.setSkuAttrValueEsVOs(valueIdVOs);
            Index index = new Index.Builder(skuInfoEsVO).index(EsConstant.GMALL_INDEX).type(EsConstant.GMALL_SKU_TYPE).id(skuInfoEsVO.getId() + "").build();
            try {
                jestClient.execute(index);
            } catch (IOException e) {
               log.error("es保存数据出问题了{}",e);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SkuSearchResultEsVO searchSkuFromEs(SkuSearchParamEsVO paramEsVO) {
        SkuSearchResultEsVO skuSearchResultEsVO = null;
        //DSL大拼串
        String queryDsl = buildSearchQueryDSL(paramEsVO);
       log.info("拼串queryDsl{}",queryDsl);
        //传入DSL语句
        Search search = new Search.Builder(queryDsl).addIndex(EsConstant.GMALL_INDEX)
                                              .addType(EsConstant.GMALL_SKU_TYPE)
                                              .build();

        //执行查询
        try {
            SearchResult result = jestClient.execute(search);
            log.info("result{}",result);

            //把查询出来的结果处理成能给页面返回的SkuSearchResultEsVO对象
             skuSearchResultEsVO = buildSkuSearchResult(result);
             log.info("将要返回页面的skuSearchResultEsVO{}",skuSearchResultEsVO);
        } catch (IOException e) {
           log.error("Es查询出故障了{}",e);
        }
        Integer pageNo = paramEsVO.getPageNo();
        log.info("PageNo{}", pageNo);
        skuSearchResultEsVO.setPageNo(pageNo);

        log.info("将要返回页面的skuSearchResultEsVO{}",skuSearchResultEsVO);
        return skuSearchResultEsVO ;
    }
    @Async
    @Override
    public void updateHotScore(Integer skuId, Long hincrBy) {
        String updateHotScoreDSL = "{\"doc\": {\"hotScore\":"+hincrBy+"}}";
        Update updateHotScoreDsl = new Update.Builder(updateHotScoreDSL).index(EsConstant.GMALL_INDEX)
                .type(EsConstant.GMALL_SKU_TYPE).id(skuId+"")
                .build();
        try {
            jestClient.execute(updateHotScoreDsl);
        } catch (IOException e) {
           log.error("更新热度评分出问题了{}",e);
        }

    }

    //构造ueryDSL字符串
    public String buildSearchQueryDSL(SkuSearchParamEsVO paramEsVO){
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //过滤三级分类信息
        if(paramEsVO.getCatalog3Id() != null){

            TermQueryBuilder termCatalog3Id = new TermQueryBuilder("catalog3Id", paramEsVO.getCatalog3Id());

            boolQueryBuilder.filter(termCatalog3Id);
        }
        //过滤ValueId信息
        if(paramEsVO.getValueId()!=null && paramEsVO.getValueId().length>0){
            for (Integer valueId : paramEsVO.getValueId()) {


                TermQueryBuilder termValueId =
                        new TermQueryBuilder("skuAttrValueEsVOs.ValueId",
                                valueId);

                boolQueryBuilder.filter(termValueId);
            }

        }
        //搜索
        if(!StringUtils.isEmpty(paramEsVO.getKeyword())){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", paramEsVO.getKeyword());
            boolQueryBuilder.must(matchQueryBuilder);
            //高亮
            HighlightBuilder highlightBuilder = new HighlightBuilder();
            highlightBuilder.field("skuName");
            highlightBuilder.preTags("<span style='color:red'>");
            highlightBuilder.postTags("</span>");
            searchSourceBuilder.highlight(highlightBuilder);

        };
        //以上查询与过滤完成
        searchSourceBuilder.query(boolQueryBuilder);
         //排序
       if(!StringUtils.isEmpty(paramEsVO.getSortField())) {
           SortOrder sortOrder = null;
           switch (paramEsVO.getSortOrder()){
               case  "desc": sortOrder=SortOrder.DESC;break;
               case "asc": sortOrder=SortOrder.ASC;break;
               default:sortOrder = SortOrder.DESC;
           }
           searchSourceBuilder.sort(paramEsVO.getSortField(),sortOrder);

       }
       //分页
        //页面传入的是页码，我们计算一下从第几个开始查;  (pageNo - 1)*pageSize   0  12
        searchSourceBuilder.from((paramEsVO.getPageNo()-1)*paramEsVO.getPageSize());
        searchSourceBuilder.size(paramEsVO.getPageSize());

        //聚合
        TermsBuilder termsBuilder = new TermsBuilder("valueIdAggs");
        termsBuilder.field("skuAttrValueEsVOs.ValueId");
        searchSourceBuilder.aggregation(termsBuilder);
        String dsl = searchSourceBuilder.toString();

        return dsl;
   
   
    }
//将查出的结果构建为页面能用的vo对象数据
    public SkuSearchResultEsVO buildSkuSearchResult(SearchResult result){
        log.info("传入的result{}",result);

        SkuSearchResultEsVO resultEsVO = new SkuSearchResultEsVO();
        List<SkuInfoEsVO> skuInfoEsVOs = null;

        //拿到命中的所有记录
        List<SearchResult.Hit<SkuInfoEsVO, Void>> hits = result.getHits(SkuInfoEsVO.class);
        if(hits==null || hits.size()==0){
              return null;
        }else{
            skuInfoEsVOs=new ArrayList<>(hits.size());
            for (SearchResult.Hit<SkuInfoEsVO, Void> hit : hits) {
                SkuInfoEsVO source = hit.source;
                //可能有高亮内容
                Map<String, List<String>> highlight = hit.highlight;
                //普通非全文模糊匹配是没有高亮的
                if(highlight!=null){
                    String highText = highlight.get("skuName").get(0);
                    //替换高亮
                    source.setSkuName(highText);
                }
                skuInfoEsVOs.add(source);
        }
            log.info("resultEsVO000{}",resultEsVO);
            resultEsVO.setSkuInfoEsVOs(skuInfoEsVOs);
            log.info("resultEsVO111{}",resultEsVO);
            //总记录数
            resultEsVO.setTotal(result.getTotal().intValue());
            log.info("resultEsVO222{}",resultEsVO);
//从聚合的数据中取出所有的平台属性和它的值
            List<BaseAttrInfo> baseAttrInfos = getBaseAttrInfoGroupByValueId(result);
            log.info("resultEsVO333{}",resultEsVO);
            resultEsVO.setBaseAttrInfos(baseAttrInfos);
            log.info("resultEsVO444{}",resultEsVO);
            return resultEsVO;
        }


    }
    public List<BaseAttrInfo> getBaseAttrInfoGroupByValueId(SearchResult result) {
        MetricAggregation aggregations = result.getAggregations();
        TermsAggregation valueIdAggs = aggregations.getTermsAggregation("valueIdAggs");
        List<TermsAggregation.Entry> buckets = valueIdAggs.getBuckets();
        List<Integer> valueIds = new ArrayList<>();
        for (TermsAggregation.Entry bucket : buckets) {
            String key = bucket.getKey();
            int valueId = Integer.parseInt(key);
            valueIds.add(valueId);
       }
          return skuInfoService.getBaseAttrInfoGroupByValueId(valueIds);
    }


}
