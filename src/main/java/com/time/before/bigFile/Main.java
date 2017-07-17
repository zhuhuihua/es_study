package com.time.before.bigFile;
//package cn.dyz.tools.file;
//
//public class Main {
//
//	public static void main(String[] args) {
//		
//		BigFileReader.Builder builder = new BigFileReader.Builder("D:/testData/data.txt", new IHandle() {
//			
//			public void handle(String line) {
//				System.out.println(line);
//			}
//		});
//		
//		builder.withTreahdSize(2)
//			   .withCharset("utf-8")
//			   .withBufferSize(1024*1024);
//		BigFileReader bigFileReader = builder.build();
//		bigFileReader.start();
//	}
//	
//}
