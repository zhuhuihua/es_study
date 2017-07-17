package com.time.before.restful;

import java.io.IOException;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.indices.CreateIndex;

public class RestfulTest {
	
	public static void main(String args[]) throws Exception{
		
		 JestClientFactory factory = new JestClientFactory();
		 factory.setHttpClientConfig(new HttpClientConfig
		                        .Builder("http://localhost:9200")
		                        .multiThreaded(true)
					//Per default this implementation will create no more than 2 concurrent connections per given route
					.defaultMaxTotalConnectionPerRoute(1)
					// and no more 20 connections in total
					.maxTotalConnection(10)
		                        .build());
		 JestClient client = factory.getObject();
		 client.execute(new CreateIndex.Builder("articles").build());
		
	}

}
