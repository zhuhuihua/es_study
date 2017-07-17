package com.time.before.hbase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.NavigableSet;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.io.compress.Compression;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HbaseDao {

	private static final Logger logger = LogManager.getLogger(HbaseDao.class.getSimpleName());

	private static Configuration conf = null;

	static {

		conf = HBaseConfiguration.create();
//		conf.set("hbase.zookeeper.property.clientPort", "2181");
		conf.set("hbase.zookeeper.quorum", "192.168.1.41:2181,192.168.1.42:2181,192.168.1.43:2181");
		conf.set("hbase.master", "192.168.1.41:2181");
		conf.set("hbase.client.retries.number", "3");
		conf.set("hbase.client.pause", "10000");
		conf.set("zookeeper.recovery.retry", "3");
		conf.set("ipc.socket.timeout", "40");
		conf.set("zookeeper.recovery.retry.intervalmill", "200");
		
//		conf.set("hbase.rpc.timeout", "60");

	}

	/**
	 * get list tables
	 * 
	 * @return
	 * @throws IOException
	 */
	public static List<String> listTables() throws IOException {

		List<String> tables_list = new ArrayList<String>();
		Connection connection = null;

		try {

			connection = ConnectionFactory.createConnection(conf);
			Admin admin = connection.getAdmin();

			HTableDescriptor[] tableDescriptors = admin.listTables();
			for (HTableDescriptor descriptor : tableDescriptors) {
				System.out.println(descriptor.getNameAsString());
				tables_list.add(descriptor.getNameAsString());
			}

		} finally {
			if(connection.isClosed())
				connection.close();
		}
		return tables_list;
	}

	/**
	 * create new table
	 * 
	 * @param tableName
	 * @param familys
	 * @throws IOException
	 */
	public static void createTable(final TableName tableName, final String[] familys) throws IOException {

		Connection connection = null;
		Admin admin = null;

		try {
			
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();

			if (admin.tableExists(tableName)) {
				logger.error("Create table failed, table name is:{}", tableName.getName().toString());
				return;
			}

			HTableDescriptor tableDescriptor = new HTableDescriptor(tableName);
			for (String family : familys) {
				tableDescriptor.addFamily(new HColumnDescriptor(family));
			}
			admin.createTable(tableDescriptor);
			logger.info("Create table success.");

		} finally {
			admin.close();
			if (!connection.isClosed())
				connection.close();
		}
	}

	/**
	 * drop a table
	 * 
	 * @param tableName
	 * @throws IOException
	 */
	public static void dropTable(final TableName tableName) throws IOException {

		Connection connection = null;
		Admin admin = null;

		try {

			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();

			if (!admin.tableExists(tableName)) {
				logger.error("Create table failed, table name is:{}", tableName.getName().toString());
				return;
			}
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			logger.info("Drop table success.");
		} finally {
			admin.close();
			if (!connection.isClosed())
					connection.close();
		}
	}

	/**
	 * put data to a existed table
	 * 
	 * @param tableName
	 * @param key
	 * @param family
	 * @param qualifier
	 * @param value
	 * @throws IOException
	 */
	public static void putData(final TableName tableName, final String key, final String family, final String qualifier,
			final String value) throws IOException {

		Connection connection = null;
		Admin admin = null;

		try {

			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();

			if (!admin.tableExists(tableName)) {
				logger.error("Put data failed, {} not existed!!", new String(tableName.getName()));
				return;
			}
			Table table = connection.getTable(tableName);
			// construct put
			Put put = new Put(key.getBytes());
			put.addColumn(family.getBytes(), qualifier.getBytes(), value.getBytes());
			// put data
			table.put(put);
			logger.info("Put data successfully!");
		} finally {
			
			admin.close();
			if(!connection.isClosed())
				connection.close();
		}
	}

	/**
	 * get a row based on rowkey
	 * 
	 * @param tableName
	 * @param rowKey
	 * @throws IOException
	 */
	public static void getData(final TableName tableName, final String rowKey) throws IOException {

		Connection connection = null;
		Admin admin = null;

		try {
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
			if (!admin.tableExists(tableName)) {
				logger.info("Get data failed, {} not existed", new String(tableName.getName()));
				return;
			}

			Table table = connection.getTable(tableName);
			HTableDescriptor hTableDescriptor = table.getTableDescriptor();
			HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();

			Get get = new Get(Bytes.toBytes(rowKey));
			Result result = table.get(get);

			if (result.isEmpty()) {
				logger.info("this table is empty!");
				return;
			}

			String family;
			String qualifier;
			String value;

			for (HColumnDescriptor hColumnDescriptor : hColumnDescriptors) {

				family = hColumnDescriptor.getNameAsString();
				NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(hColumnDescriptor.getName());
				NavigableSet<byte[]> qualifierSet = familyMap.navigableKeySet();
				for (byte[] q : qualifierSet) {
					qualifier = new String(q);
					value = new String(familyMap.get(q));
					logger.info(rowKey + "    " + "column=" + family + ":" + qualifier + ", value:" + value);
				}
			}
		} finally {
			admin.close();
			if(!connection.isClosed())
			connection.close();
		}
	}

	/**
	 * get all rows
	 * 
	 * @param tableName
	 * @throws IOException
	 */
	public static void scanTable(TableName tableName) throws IOException {

		Connection connection = null;
		Admin admin = null;
		
		try {
			
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();
			
			if (!admin.tableExists(tableName)) {
				logger.error("Scan table failed!!!, {} does not exists", new String(tableName.getName()));
			}

			Table table = connection.getTable(tableName);
			HTableDescriptor hTableDescriptor = table.getTableDescriptor();

			HColumnDescriptor[] hColumnDescriptors = hTableDescriptor.getColumnFamilies();
			ResultScanner scanner = table.getScanner(new Scan());

			for (Result result : scanner) {

				String rowKey = new String(result.getRow());
				String family = "";
				String qualifier = "";
				String value = "";

				if (result.isEmpty())
					continue;

				for (HColumnDescriptor hColumnDescriptor : hColumnDescriptors) {
					family = hColumnDescriptor.getNameAsString();
					// System.out.println("debug"+rowKey+" "+family);
					NavigableMap<byte[], byte[]> familyMap = result.getFamilyMap(hColumnDescriptor.getName());
					NavigableSet<byte[]> qualifierSet = familyMap.navigableKeySet();
					for (byte[] q : qualifierSet) {
						qualifier = new String(q);
						value = new String(familyMap.get(q));
						logger.info(rowKey + "    " + "column=" + family + ":" + qualifier + ",value:" + value);
					}
				}

			}
		} finally{
			admin.close();
			if(!connection.isClosed())
				connection.close();
		}
	}
	
	/**
	 * 创建
	 * @param tableName
	 * @param regions
	 * @throws IOException 
	 * */
	public static void createCommonTable(TableName tableName, int regions) throws IOException {
		
		Connection connection = null;
		Admin admin = null;
		
		try {
			
			connection = ConnectionFactory.createConnection(conf);
			admin = connection.getAdmin();

			if (!admin.tableExists(tableName)) {
				logger.error("Scan table failed!!!, {} does not exists", new String(tableName.getName()));
			}
			
			Table table = connection.getTable(tableName);
			HTableDescriptor hd = table.getTableDescriptor();
			hd.setReadOnly(false);

			HColumnDescriptor f = new HColumnDescriptor("F1");
			f.setBlockCacheEnabled(true);
			f.setBloomFilterType(BloomType.ROWCOL);
			f.setCompressionType(Compression.Algorithm.SNAPPY);
			f.setMaxVersions(1);
			hd.addFamily(f);
			
		}finally {
			admin.close();
			if (!connection.isClosed())
				connection.close();
		}

	}
	

	public static void main(String args[]) throws IOException {
		
		TableName tableName = TableName.valueOf("zhuhuihua_test");
		String[] familys = new String[]{"person_info, order"};
		
		createTable(tableName, familys);
		
		listTables();
	}

}
