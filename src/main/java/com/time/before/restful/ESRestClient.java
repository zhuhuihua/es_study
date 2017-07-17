package com.time.before.restful;

import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;

public class ESRestClient {
	
	static RestClient restClient;
	
	static {
		
		restClient = RestClient.builder(new HttpHost("localhost", 9200))
		        .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {
		            @Override
		            public HttpAsyncClientBuilder customizeHttpClient(HttpAsyncClientBuilder httpClientBuilder) {
		                return httpClientBuilder.setDefaultIOReactorConfig(
		                        IOReactorConfig.custom().setIoThreadCount(5).build());
		            }
		        })
		        .build();
		
	}
	

}
