package com.time.before.hbase;
///**
// * this is a single Hbase client example based hbase 0.98.6 API,
// * Any questions,contact me 
// * xiaohui.zhang@transwarp.io 
// */
//package com.dk.hbase;
//
//import java.io.IOException;
//import java.util.NavigableMap;
//import java.util.NavigableSet;
//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.HColumnDescriptor;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.client.Get;
//import org.apache.hadoop.hbase.client.HBaseAdmin;
//import org.apache.hadoop.hbase.client.HConnection;
//import org.apache.hadoop.hbase.client.HConnectionManager;
//import org.apache.hadoop.hbase.client.HTableInterface;
//import org.apache.hadoop.hbase.client.Put;
//import org.apache.hadoop.hbase.client.Result;
//import org.apache.hadoop.hbase.client.ResultScanner;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.io.compress.Compression;
//import org.apache.hadoop.hbase.protobuf.generated.HBaseProtos.RegionSpecifier;
//import org.apache.hadoop.hbase.regionserver.BloomType;
//import org.apache.hadoop.hbase.util.Bytes;
////import org.apache.hadoop.hbase.util.RegionSplitter;
////import org.apache.hadoop.hbase.util.RegionSplitter.SplitAlgorithm;
//
//public class testHbaseConnect {
//	public static Configuration configuration;
//	public HBaseAdmin admin;
//	public HConnection connection;
//	
//	
//	static{
//		
//		configuration = HBaseConfiguration.create();
//		configuration.set("hbase.zookeeper.property.clientPort", "2181");
//		configuration.set("hbase.zookeeper.quorum", "10.212.142.55,10.212.142.56,10.212.142.57");
//		configuration.set("hbase.master", "10.212.142.55:60000");
//		configuration.set("zookeeper.znode.parent", "/hyperbase1");
//	}
//	
//	/**
//	 * init HBase connection
//	 * hbase.master的三个管理节点 一般跟zk对应
//	 */
//	private void init() {
//		
//		try {
//			connection = HConnectionManager.createConnection(configuration);
//			admin =new HBaseAdmin(configuration);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	/**
//	 * list hbase table
//	 */
//	private void listTable() {
//		try {
//			HTableDescriptor[] tableDescriptors = admin.listTables();
//			for(HTableDescriptor descriptor:tableDescriptors){
//				System.out.println(descriptor.getNameAsString());
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	/**
//	 * create new table
//	 * @param tableName
//	 * @param familys
//	 */
//	private void createTable(String tableName, String[] familys) {
//		try {
//			if(admin.tableExists(tableName)){
//				System.out.println("create table failed!!!,"+ tableName+" exists!!!");
//				return;
//			}
//			
//			HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
//			for(String family:familys){
//				tableDescriptor.addFamily(new HColumnDescriptor(family));
//			}
//			
//			admin.createTable(tableDescriptor);
//			System.out.println("create table "+ tableName +" successfully!!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * drop a table
//	 * @param tableName
//	 */
//	private void dropTable(String tableName){
//		try {
//			if(!admin.tableExists(tableName)){
//				System.out.println("drop table failed!!!,"+tableName +" does not exists");
//				return;
//			}
//			TableName table = TableName.valueOf(tableName);
//			admin.disableTable(table);
//			admin.deleteTable(table);
//			System.out.println("drop table "+ tableName +" successfully!!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * put data to a existed table
//	 * @param tableName
//	 * @param key
//	 * @param family
//	 * @param qualifier
//	 * @param value
//	 */
//	private void putData(String tableName, String key, String family, String qualifier, String value){
//		try {
//			if(!admin.tableExists(tableName)){
//				System.out.println("put data failed,"+tableName +" does not exists");
//				return;
//			}
//			TableName table = TableName.valueOf(tableName);
//			HTableInterface hTable = connection.getTable(table);
//			//construct put
//			Put put = new Put(key.getBytes());
//			put.add(family.getBytes(), qualifier.getBytes(), value.getBytes());
//			//put data
//			hTable.put(put);
//			System.out.println("put data successfully!");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//	}
//	/**
//	 * get a row based on rowkey
//	 * @param tableName
//	 * @param rowKey
//	 */
//	private void getData(String tableName, String rowKey){
//		try {
//			if(!admin.tableExists(tableName)){
//				System.out.println("get data failed!!"+ tableName +" does not exists");
//				return;
//			}
//			
//			TableName table = TableName.valueOf(tableName);
//			HTableInterface hTableInterface = connection.getTable(table);
//			HTableDescriptor hTableDescriptor = connection.getHTableDescriptor(table);
//			HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();
//			
//			Get get = new Get(Bytes.toBytes(rowKey));
//			Result result = hTableInterface.get(get);
//			
//			if(result.isEmpty()){
//				System.out.println("this table is empty!");
//				return;
//			}
//			
//			String family;
//			String qualifier;
//			String value;
//			for(HColumnDescriptor hColumnDescriptor:hColumnDescriptors){
//				family = hColumnDescriptor.getNameAsString();
//				NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(hColumnDescriptor.getName());
//				NavigableSet<byte[]> qualifierSet = familyMap.navigableKeySet();
//				for(byte[] q: qualifierSet){
//					qualifier = new String(q);
//					value = new String(familyMap.get(q));
//					System.out.println(rowKey+"    "+"column="+family+":"+qualifier+",value:" + value);
//				}
//				
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	/**
//	 * get all rows
//	 * @param tableName
//	 */
//	private void scanTable(String tableName){
//		try {
//			if(!admin.tableExists(tableName)){
//				System.out.println("scan table failed!!!,"+tableName +" does not exists");
//			}
//			
//			TableName table = TableName.valueOf(tableName);
//			HTableInterface hTableInterface = connection.getTable(table);
//			HTableDescriptor hTableDescriptor = connection.getHTableDescriptor(table);
//			
//			HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();
//			ResultScanner scanner = hTableInterface.getScanner(new Scan());
//			
//			for(Result result:scanner){
//				String rowKey = new String(result.getRow());
//				String family="";
//				String qualifier="";
//				String value="";
//				
//				if(result.isEmpty())continue;
//				
//				for(HColumnDescriptor hColumnDescriptor: hColumnDescriptors){
//					family = hColumnDescriptor.getNameAsString();
//					//System.out.println("debug"+rowKey+" "+family);
//					NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(hColumnDescriptor.getName());
//					NavigableSet<byte[]>qualifierSet = familyMap.navigableKeySet();
//					for(byte[] q:qualifierSet){
//						qualifier = new String(q);
//						value = new String(familyMap.get(q));
//						System.out.println(rowKey+"    "+"column="+family+":"+qualifier+",value:" + value);
//					}
//				}
//				
//			}			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	
//	/**
//	 * 创建
//	 * @param tableName
//	 * @param regions
//	 * */
//	public static void createCommonTable(String tableName, int regions) {
//		HBaseAdmin admin = null;
//		try {
//			admin = new HBaseAdmin();
//			HTableDescriptor hd = new HTableDescriptor();
//			hd.setName(tableName.getBytes("utf-8"));
//			hd.setReadOnly(false);
//			hd.setDeferredLogFlush(false);
//
//			HColumnDescriptor f = new HColumnDescriptor("F1");
//			f.setBlockCacheEnabled(true);
//			f.setBloomFilterType(BloomType.ROWCOL);
//			f.setCompressionType(Compression.Algorithm.SNAPPY);
//			f.setMaxVersions(1);
//			hd.addFamily(f);
//			//log.info("创建表" + tableName + "成功");
//		} catch (Exception e) {
//			//log.error("创建表" + tableName + "失败，" + e.getMessage(), e);
//		} finally {
//			if (admin != null)
//				try {
//					admin.close();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
//		}
//
//	}
//	
//	
//	
//	
//	
//	
//	public static void main(String[] args) {
//		testHbaseConnect test = new testHbaseConnect();
//		test.init();
//		
//		
//		
//		test.listTable();
//		
////		String[] families = {"f1","f2"};
////		test.dropTable("first");
////		test.createTable("first",families);
////		
////		test.putData("first", "1", "f1", "name", "zhang");
////		test.putData("first", "1", "f1", "sex", "male");
////		test.putData("first", "1", "f1", "test", "ddd");		
////		test.putData("first", "1", "f2", "birth", "2016");
////		test.putData("first", "1", "f2", "age", "1");
////		
//		test.scanTable("first");
////		System.out.println("*****************************");
////		test.putData("first", "1", "f2", "age", "2");
////		test.scanTable("first");
////		
////		test.getData("first","1");
//	}
//}
