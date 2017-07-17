package com.time.before.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @author zhuhuihua
 *
 */
public class Configs {
	
	public static final Config base = ConfigFactory.load();
	
	public static final Config main;
	
	static {
		
		File file = new File("config.json");
		
		if(file.exists()){
			main = ConfigFactory.parseFile(file).withFallback(base);
			
		} else {
			InputStream stream = Configs.class.getClassLoader().getResourceAsStream("config.json");
			main = ConfigFactory.parseReader(new InputStreamReader(stream)).withFallback(base);
		}
	}

	public static final Config dev = main.getConfig("dev").withFallback(main);
	
	public static void main(String args[]){
		
		System.out.println(Configs.dev.getConfig("es").getConfig("node1").getString("ip"));
	}
	
}
