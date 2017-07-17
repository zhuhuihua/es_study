package com.time.before.restful;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.common.settings.Settings;

import com.time.before.es.util.JsonUtil;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;

public class ESRestFulClient {

	public static JestClient client;

	private static String serverUri = "http://localhost:";
	private static int serverPort = 9200;
	private static Logger logger = LogManager.getLogger(ESRestFulClient.class.getSimpleName());

	static {

		JestClientFactory factory = new JestClientFactory();
		factory.setHttpClientConfig(new HttpClientConfig.Builder(serverUri + serverPort)
														.readTimeout(60000).connTimeout(20000)
														.multiThreaded(true).build());
		client = factory.getObject();
	}

	public void createIndex(String indexName, int number_of_shards, int number_of_replicas) {

		Settings.Builder settingsBuilder = Settings.builder();
		settingsBuilder.put("number_of_shards", number_of_shards);
		settingsBuilder.put("number_of_replicas", number_of_replicas);

		try {
			client.execute(new CreateIndex.Builder(indexName).settings(settingsBuilder.build().getAsMap()).build());
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public static void createIndex(Map<String, Object> source, String indexName, String type) {

		Index index = new Index.Builder(source).index(indexName).type(type).build();
		try {
			client.execute(index);
		} catch (IOException e) {
			logger.error(e);
		}

	}

	/**
	 * bulk operation
	 * 
	 * @param source
	 */

	public static void bulkOperate(String indexName, String type, List<Person> sources) {
		
		Index index = null;
		Bulk.Builder bulkBuilder = new Bulk.Builder();
		Long start = System.currentTimeMillis();
		String src = null;
		
		for(Person p : sources){
			src = JsonUtil.Object2Json(p);
			index = new Index.Builder(src).index(indexName)
                     .type(type).id(p.id).build();
			bulkBuilder.addAction(index);
		}
		
		try {
			
			BulkResult result = client.execute(bulkBuilder.build());
			logger.info("use total time:{}s", (System.currentTimeMillis() - start) / 1000);
			if (result.getResponseCode() == 200) {
				logger.info("bulk operation success");
			}
		} catch (IOException e) {
			System.out.println(e);
			logger.error("bulk operation failed", e);
		}
	}
	
	public static void deleteIndex(String indexName){
		
		if(is_index_exist(indexName)){
			
			try {
				JestResult jr = client.execute(new DeleteIndex.Builder(indexName).build());
				logger.info(jr.getResponseCode());
			} catch (IOException e) {
				logger.error("delete index failed", e.toString());
			}  
		}
	}

	public static boolean is_index_exist(String indexName) {

		boolean exist = false;

		IndicesExists indicesExists = new IndicesExists.Builder(indexName).build();
		
		try {
			JestResult result = client.execute(indicesExists);
			if(result.isSucceeded()){
				exist = true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return exist;
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

	public static void main(String args[]) {

		List<Person> persons = new ArrayList<Person>();

		for (int i = 0; i < 400000; i++) {
			Person p = new Person("doc " + i + "", 25 + i);
			p.id = generateID(p);
			persons.add(p);
		}

		bulkOperate("student", "person", persons);
		
		deleteIndex("student");

//		 Map<String, Object> source = new HashMap<String, Object>();
//		 source.put("name", "zhuhuihua");
//		 source.put("age", 26);
//		 source.put("sex", "male");
//		
//		 createIndex(source, "person2", "famous");

	}

	static class Person {

		public String name;
		public Integer age;
		public String id;

		public Person() {
		}

		public Person(String name, Integer age) {

			this.name = name;
			this.age = age;
		}
	}

}
