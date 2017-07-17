package com.time.before.es.util;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.admin.indices.stats.CommonStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.MultiSearchRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.shard.DocsStats;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import com.time.before.seach.builder.SearchBuilder;

public class ESSelectUtil {

	private static final Logger logger = LogManager.getLogger(ESSelectUtil.class.getSimpleName());

	public static void selectDocByID(String indexName, String type, String id) {

		if (EsUtils.getClusterStatus() == 200) {

			GetResponse response = EsUtils.client.prepareGet(indexName, type, id).execute().actionGet();
			Map<String, Object> source = response.getSource();
			System.out.println(source);
		}
	}

	/**
	 * 根据indexName查询所有索引数据，数据量很大的时候没法用
	 * 
	 * @param indexName
	 * @return
	 */

	public static SearchHit[] selectDocByIndexName(String indexName) {

		SearchHit[] searchHits = null;

		if (EsUtils.getClusterStatus() == 200) {

			SearchRequest request = new SearchRequest(indexName);
			ActionFuture<SearchResponse> f = EsUtils.client.search(request);
			try {
				SearchResponse response = f.get();
				SearchHits hits = response.getHits();
				searchHits = hits.getHits();

			} catch (InterruptedException | ExecutionException e) {
				logger.error(e);
			}

		} else {
			throw new Error("es cluster not response");
		}
		return searchHits;
	}

	/**
	 * 根据index和type查询数据
	 * 
	 * @param builder
	 * @return
	 */
	public static SearchHit[] selectDocByType(SearchBuilder builder) {

		SearchHit[] searchHits = null;

		if (EsUtils.getClusterStatus() == 200) {
			long start = System.currentTimeMillis();
			SearchResponse scrollResp = EsUtils.client.prepareSearch().setIndices(builder.indices)
					.setTypes(builder.type).setSize(builder.size).setQuery(QueryBuilders.matchAllQuery()).execute()
					.actionGet();
			searchHits = scrollResp.getHits().getHits();
			System.out.println("total time =" + (System.currentTimeMillis() - start));
		} else {
			throw new Error("es cluster not response");
		}

		return searchHits;
	}
	
	/**
	 * 得到总记录数
	 * @throws Exception 1354  1382
	 */
	public static Long getTotalCount(String indexName, String docType) throws Exception{
		
		long start = System.currentTimeMillis();
		
		if(EsUtils.getClusterStatus() == 200){
			if(EsUtils.isExistedIndex(indexName)){
				IndicesStatsResponse response = EsUtils.client.admin().indices()
						   .prepareStats(indexName).setTypes(docType).execute().actionGet();

				CommonStats commonStatus = response.getIndex(indexName).getTotal();
				DocsStats docStats = commonStatus.getDocs();
				System.out.println(System.currentTimeMillis() - start);
				return docStats.getCount();
			}else{
				throw new Exception("Index not exist.");
			}

		}
		return null;
		
		 
//	     return response.getIndex(indexName).getDocs().getNumDocs();
		
	}
	
	public static void boolQuery(Map<String, Object> terms){
		
		BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
		
//		QueryBuilders.boolQuery().filter(QueryBuilders.termQuery(name, value));
		
	}
	
	/**
	 * 根据组合条件查询数据
	 * @return
	 */
	
	public static SearchHit[] selectDocByCondition() {

		if (EsUtils.getClusterStatus() == 200) {
			MultiSearchRequest multiSearchRequest = new MultiSearchRequest();
			SearchRequest request = new SearchRequest();
			multiSearchRequest.add(request);
			EsUtils.client.multiSearch(multiSearchRequest);
		}

		return null;
	}

	public static void main(String args[]) throws Exception {
		
//		System.out.println(getTotalCount("student", ""));

		SearchBuilder builder = new SearchBuilder();
		builder.setIndex("student").setType("person").setSize(10000);
		SearchHit[]  hits = selectDocByType(builder);
		System.out.println(hits.length);
		hits = null;
		
		Thread.sleep(1000 * 60L);
		SearchHit[]  hits_ = selectDocByType(builder);
		System.out.println(hits_.length);
		
//		System.out.println(.length);
	}

}
