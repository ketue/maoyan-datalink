package com.maoyan.bigdata.datalink.dao.eagle_rtdw;


import com.alibaba.fastjson.JSON;
import com.sankuai.meituan.eagle.restclient.EagleRestClient;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class FactRefundMovieOrderDao {

    private static final Logger logger = LoggerFactory.getLogger(FactRefundMovieOrderDao.class);

    @Autowired
    @Qualifier("eagleRestClientWH")
    private EagleRestClient eagleRestClient;


    private static final String INDEX_ALIAS_NAME = "fact2_refund_movie_order";
    private static final String TYPE_NAME = "fact2_refund_movie_order";

    /**
     * 根据 id 从索引中获取数据
     * @param sid document id
     * @return
     */
    public Map<String, Object> getFromIndexById(String sid) {
        // 根据单个id查询文档
        GetRequest request = new GetRequest(INDEX_ALIAS_NAME, TYPE_NAME, sid);
        Map<String, Object> sourceAsMap = null;
        try {
            GetResponse getResponse = eagleRestClient.getClient().get(request);
            String index = getResponse.getIndex();
            String type = getResponse.getType();
            String id = getResponse.getId();
            if (getResponse.isExists()) {
                long version = getResponse.getVersion();
                sourceAsMap = getResponse.getSourceAsMap(); //Retrieve the document as a Map<String, Object>
            }
        } catch (ElasticsearchException e) {
            if (e.status() == RestStatus.NOT_FOUND) {
                logger.error("处理因为索引不存在而抛出的异常", e);
            }
            if (e.status() == RestStatus.CONFLICT) {
                logger.error("表示是由于返回了版本冲突错误引发的异常", e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sourceAsMap;
    }


    /**
     * 从别名中根据 order_id获取数据
     * @param oid
     * @return
     */
    public Map<String, Object> searchFromAliasById(String oid) {
        SearchRequest searchRequest = new SearchRequest(INDEX_ALIAS_NAME); // 限制请求到某个索引上
        // searchRequest.types("doc");  // 限制请求的类别
        SearchSourceBuilder searchRequestBuilder = new SearchSourceBuilder();   // 大多数的搜索参数被添加到 SearchSourceBuilder
        searchRequestBuilder.query(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("orderId", oid)));
        searchRequestBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS)); //设置一个可选的超时时间，用于控制搜索允许的时间。
        searchRequest.source(searchRequestBuilder);
        Map<String, Object> sourceAsMap = null;
        try {
            SearchResponse searchResponse = eagleRestClient.getClient().search(searchRequest);
            // 获取返回的文档
            SearchHits hits = searchResponse.getHits();
            long totalHits = hits.getTotalHits();  // 命中总数
            float maxScore = hits.getMaxScore();   // 最大分数
            // 嵌套在 SearchHits 的各个搜索结果迭代访问
            SearchHit[] searchHits = hits.getHits();
            for (SearchHit hit : searchHits) {
                // do something with the SearchHit
                String index = hit.getIndex();
                String type = hit.getType();
                String id = hit.getId();
                float score = hit.getScore();
                // 键/值对的映射
                sourceAsMap = hit.getSourceAsMap();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sourceAsMap;
    }


    /**
     * 更新数据
     * @param id document id
     * @param indexName index name
     * @param typeName type name
     * @param data 更新的数据
     */
    public void update(String id, String indexName,String typeName,Map<String, Object> data) {
        // 插入使用的数据
        if (data != null && !data.isEmpty()) {
            // 执行更新
            UpdateRequest updateRequest = new UpdateRequest(indexName, typeName, id);
            updateRequest.timeout(TimeValue.timeValueSeconds(5));
            updateRequest.doc(data);
            try {
                UpdateResponse response = eagleRestClient.getClient().update(updateRequest);
            } catch (Exception e) {
                logger.error("update data : {} error ", JSON.toJSONString(data), e);
            }

        }
    }
}
