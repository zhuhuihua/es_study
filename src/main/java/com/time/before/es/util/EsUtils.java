package com.time.before.es.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.ActionFuture;
import org.elasticsearch.action.ListenableActionFuture;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient.Builder;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.rest.RestStatus;

import com.time.before.config.Configs;
import com.time.before.es.exception.ESClusterResponseException;

/**
 * Es集群的状态，red表示指定的索引没有在集群中分配，yellow表示主分片分配了，但是主分片的副本没有，green表示所有分片都分配了
 * @author u
 *
 */


public class EsUtils {
	
	private static final Logger logger = LogManager.getLogger(EsUtils.class.getSimpleName());
	
	public static Client client;
	
	static{
		
		Settings settings = Settings.settingsBuilder()
				.put("cluster.name", Configs.dev.getConfig("es").getString("clusterName"))
				.put("refresh_interval", -1)
				.build();
		client = new Builder().settings(settings).build()
				.addTransportAddress(new InetSocketTransportAddress(
						new InetSocketAddress(
								Configs.dev.getConfig("es").getConfig("node1").getString("ip"), 
								Configs.dev.getConfig("es").getConfig("node1").getInt("port")))
						);

		
	}
	
	/**
	 * 创建索引，创建索引时一种是直接创建一种新的索引，一种是用已经存在的索引，用type属性区分不同的类型
	 * 
	 * ES索引和普通DB的对应关系
	 * 
	 * Index---->type----->document
	 * database---->table---->column
	 * 
	 * @param indexName 索引名
	 * @param type 文档类型
	 * @param id 用source对象的id作为区分同一个type不同内容的_id
	 * @throws ESClusterResponseException 
	 * @throws Exception
	 */
	
	public static void createIndex(String indexName, String type, String id, Object source) throws ESClusterResponseException{
		
		Integer state = getClusterStatus();
		logger.info("Cluster health response is :{}", state);
		
		String src = null;
		
		if(state == 200){
			
			src = JsonUtil.Object2Json(source);
			IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, type, id);
			indexRequestBuilder.setSource(src);
			IndexResponse response = indexRequestBuilder.get();
			 
			 if (response.isCreated()) {
		            logger.info("{} index created success", indexName);
		     }
			
		}else{
			throw new ESClusterResponseException("ES Cluster Health Response failed.");
		}
	}
	
	/**
	 * 创建没有type的索引
	 * @param indexName
	 */
	public static void createIndex(String indexName){
		
		if(getClusterStatus() == 200){
			
			if(!isExistedIndex(indexName)){
				
				CreateIndexRequest request = new CreateIndexRequest(indexName);
				ActionFuture<CreateIndexResponse>  f = client.admin().indices().create(request);
				try {
					CreateIndexResponse response = f.get();
					if(response.isAcknowledged()){
						logger.info("create index success, index name is :{}", indexName);
					}
					
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				
			}
			
		}else{
			throw new Error("cluster is not arrivals");
		}
	}
	
	
	public static void bulkOperator(List<Person> persons){
		
		long start = System.currentTimeMillis();
		BulkRequestBuilder bulkRequest = client.prepareBulk();
		
		String src;
		for(Person p :persons){
			src = JsonUtil.Object2Json(p);
			bulkRequest.add(client.prepareIndex("student", "person", p.id).setSource(src));
		}
		
		BulkResponse bulkResponse = bulkRequest.execute().actionGet();
		logger.info("use total time:{}s", (System.currentTimeMillis() - start) / 1000);
		
		if(!bulkResponse.hasFailures()){
			logger.info("bulk insert success");
		}
		
		
	}
	
	/**
	 * 批量插入
	 * @param indexName
	 * @param type
	 * @param path
	 */
	
	public void insert(String indexName, String type, String path){
		
//		BulkRequestBuilder bulkRequestBuilder = client.prepareBulk();
//		BufferedReader br = new BufferedReader(new InputStreamReader(new DataInputStream(new FileInputStream(path))));
//		String readLine = "";
//		String id = "";
		
		/*
		 * todo
		 */
		
	}
	
	public static void getIndex(String indexName, String docType, String id){
		
		UpdateRequest updateRequest = new UpdateRequest(indexName, docType, id);
//		updateRequest.
		
//		client.update(request)
		
		if(getClusterStatus() == 200){
			
			GetRequestBuilder builder = client.prepareGet(indexName, docType, id);
			GetResponse r = builder.get();
			
			GetIndexRequest request = new GetIndexRequest();
			request.indices(indexName).types(docType);
			
			
			
			ActionFuture<GetIndexResponse> f = client.admin().indices().getIndex(request);
			GetIndexResponse response = f.actionGet();
//			if(response.){}
			
		}
		
		
		
	}
	
	/**
	 * 根据名字删除索引
	 * @param indexName
	 * @throws ESClusterResponseException
	 */
	
	public static void deleteIndex(String indexName) throws ESClusterResponseException{
		
		Integer state = getClusterStatus();
		logger.info("Cluster health response is :{}", state);
		 
		if(state == 200){
			
			 if (isExistedIndex(indexName)) {
				 DeleteIndexResponse deleteIndexResponse = client.admin().indices().
		        			delete(new DeleteIndexRequest(indexName)).actionGet();
		        	if(deleteIndexResponse.isAcknowledged()){
		        		 logger.info("Delete index success, index name is:{}", indexName);
		        	}
			 }
		}else{
			throw new ESClusterResponseException("ES Cluster Health Response failed.");
		}
	}
	
	/**
	 * 获取集群健康的状态，客户端可否连上集群，408表示不可以，200 表示ok
	 * @return
	 */
	
	public static Integer getClusterStatus(){
		
		 ClusterHealthResponse healthResponse = client.admin().cluster().
	        		health(new ClusterHealthRequest().waitForYellowStatus()).actionGet();
	        
	        RestStatus status = healthResponse.status();
	        return status.getStatus();
	}
	
	/**
	 * 集群中索引信息是否存在
	 * @param indexName
	 * @return
	 */
	public static boolean isExistedIndex(String indexName){
		
		IndicesExistsResponse res = client.admin().indices().prepareExists(indexName).execute().actionGet();
		return res.isExists();
	}
	
	
	public static boolean deleteDocumentByID(String index, String type, String id){
		
		boolean b = false;
		
		if(getClusterStatus() == 200){
			
			DeleteRequest request = new DeleteRequest(index, type, id);
			ActionFuture<DeleteResponse> f = client.delete(request);
			DeleteResponse response = f.actionGet();
			if(response.isFound()){
				b = true;
			}
		}
		
		return b;
		
	}
	
	/**
	 * 更新索引中特定的信息
	 * @param indexName
	 * @throws IOException 
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	
	public static void updateIndexDocByID(String indexName, String document, Map<String, String> source) throws IOException, InterruptedException, ExecutionException{
		
		if(getClusterStatus() == 200){
			
			if(isExistedIndex(indexName)){
				XContentBuilder jb = XContentFactory.jsonBuilder();
				UpdateRequest updateReqest = new UpdateRequest();
				updateReqest.index(indexName).type(document).id("");
				updateReqest.doc(jb.startObject().field("name", source).endObject());
				UpdateResponse response = client.update(updateReqest).get();
				if(response.isCreated()){
					logger.info("update index document SUCCESS");
				}else{
					logger.info("update index document failed");
				}
			}
			
		}
		
	}
	
	static class Person{
		
		public String name;
		public Integer age;
		public String id;
		
		public Person(){}
		
		public Person(String name, Integer age){
			
			this.name = name;
			this.age = age;
		}
		
	}
	
	public static String generateID(Object obj){
		
		int code = obj.hashCode();
//		int high = code & 0XFFFF0000;
		int low = code & 0X0000FFFF;
		
		BigDecimal bdCode = new BigDecimal(code);
		BigDecimal bdLow = new BigDecimal(low);
		
		BigDecimal result = bdCode.multiply(bdLow);
		
		return result.longValue() + "";
		
	}
	
	
	public static void main(String[] args) throws Exception{
		
		List<Person> persons = new ArrayList<Person>();

		for (int i = 0; i < 1000000; i++) {
			Person p = new Person("doc " + i + "", 25 + i);
			p.id = generateID(p);
			persons.add(p);
		}
		
		bulkOperator(persons);
		
//		for(int i = 0; i < 100; i ++ ){
//			Person p = new Person("doc "+ i +"", 25 + i);
//			p.id = generateID(p);
//			createIndex("person", "article", generateID(p), p);
//		}
		
		
//		
//		p = new Person("zhuhuihua2222", 25);
//		p._id = generateID(p);
		
//		createIndex("person");
		
//		deleteIndex("person");
		
		
//		deleteDocumentByID("person", "article", generateID(p));
		
//		updateIndexData("person", "article", "article");
	}
	
	
	

}
