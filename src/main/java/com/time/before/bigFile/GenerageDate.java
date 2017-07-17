package com.time.before.bigFile;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.time.before.es.util.DateUtils;
import com.time.before.es.util.FormatUtils;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2015/11/18.
 */
public class GenerageDate {

	public static String suffix = "";
	public static BufferedWriter bw;
	public static String defaultFilePath = "";// File.separator + "tmp"+
												// File.separator + "d1" +
	public static String fileName = "";
	private static int rand;
	private static int Num;
	private static String fromDay = "2014-01-01";
	static volatile int j = 0;
	private static volatile int day = 1;
	public static URI uri;
	public static Configuration conf;
	public static FileSystem fs;
	static final String PATH = "hdfs://192.168.1.41:9000/";

	public GenerageDate() {
		
		try {
			conf = new Configuration();
			try {
				fs = FileSystem.get(new URI(PATH), conf);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getData(String afterDay) {
		
		StringBuffer sbf = new StringBuffer();
		for (Iterator<String> iter = getRadomCCARNUMBER().iterator(); iter.hasNext();) {
			String str = (String) iter.next();
			sbf.append(getRadomNID() + ",");
			sbf.append(getRadomCLICENSETYPE() + ",");
			sbf.append(str + ",");
			sbf.append(getRadomDCOLLECTIONDATE(afterDay) + ",");
			sbf.append(getRandomCADDRESSCODE() + ",");
			sbf.append(getRandomCCOLLECTIONADDRESS() + ",");
			sbf.append(getRandomCCOLLECTIONAGENCIES() + ",");
			sbf.append(getRandomCDATASOURCE() + ",");
			sbf.append(getRandomCSNAPTYPE() + ",");
			sbf.append(getRandomCDEVICECODE() + ",");
			sbf.append(getRandomCLANENUMBER() + ",");
			sbf.append(getRandomNVEHICLESPEED() + ",");
			sbf.append(getRandomCPIC1PATH() + ",");
			sbf.append(getRandomCPIC2PATH() + ",");
			sbf.append(getRandomCPIC3PATH() + ",");
			sbf.append(getRandomNDERICTRION() + ",");
			sbf.append(getRandomCARCOLOR() + ",");
			sbf.append(getRandomCARBRAND() + ",");
			sbf.append(getRandomCHISCARNUMBER() + ",");
			sbf.append(getRandomTARGETTYPE() + ",");
			sbf.append(getRandomXYWH());
			sbf.append("\n");
		}

		return sbf.toString();
	}

	public static String getRadomNID() {
		
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString().replaceAll("-", "");
		return id;
	}

	public static String getRadomCLICENSETYPE() {
		String clicensetype[] = { "01", "02", "41", "29" };
		int index = (int) (Math.random() * clicensetype.length);
		String licensetype = clicensetype[index];
		return licensetype;
	}

	public static ArrayList<String> getRadomCCARNUMBER() {

		ArrayList<String> carnumber = new ArrayList();

		String province[] = { "鲁" };
		String alphabet[] = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
				"S", "T", "U", "V", "W", "X", "Y", "Z" };
		String datanum[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };// ,"E",
																				// "F",
																				// "G",
																				// "H",
																				// "J",
																				// "K",
																				// "L",
																				// "Z"
																				// };
		int index1 = (int) (Math.random() * province.length);
		int index2 = (int) (Math.random() * alphabet.length);
		int index3 = (int) (Math.random() * datanum.length);
		int index4 = (int) (Math.random() * datanum.length);
		int index5 = (int) (Math.random() * datanum.length);
		int index6 = (int) (Math.random() * datanum.length);
		int index7 = (int) (Math.random() * datanum.length);
		Random randNum = new Random();
		int MaxRand = rand + 1;
		String randStr = Integer.toString(MaxRand);
		rand = MaxRand;
		randStr = randStr.substring(1, 6);

		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < alphabet.length; i++) {
				carnumber.add(province[index1] + alphabet[i] + randStr);
				// System.out.println("carnumber.get(i) = " + carnumber.get(i));
			}
		}
		return carnumber;
	}

	public static String getRadomDCOLLECTIONDATE(String afterDay) {
		// String year[] = { "2014"};//, "2014" };
		// String month[] = { "01", "02", "03", "04", "05", "06", "07",
		// "08","09", "10", "11", "12" };
		// String day[] = { "01", "02", "03", "04", "05", "06", "07", "08",
		// "09",
		// "10", "11", "12", "13", "14", "15", "16", "17", "18", "19",
		// "20", "21", "22", "23", "24", "25", "26", "27", "28" };
		String hour[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23" };
		String minute[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
				"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
				"49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
		String second[] = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
				"15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
				"32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48",
				"49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" };
		// int index1 = (int) (Math.random() * year.length);
		// int index2 = (int) (Math.random() * month.length);
		// int index3 = (int) (Math.random() * day.length);
		int index4 = (int) (Math.random() * hour.length);
		int index5 = (int) (Math.random() * minute.length);
		int index6 = (int) (Math.random() * second.length);
		String coliectiondate = afterDay + " " + hour[index4] + ":" + minute[index5] + ":" + second[index6];
		return coliectiondate;
	}

	public static String getRandomCADDRESSCODE() {
		String caddresscode[] = { "202180111260", "605168506050", "805998504050", "705648508050", "605158504050",
				"616011023000", "616011015000", "611191003000", "511011004001", "611211007001", "611171008001",
				"616011003000", "611011003000", "611041006000", "611041005000", "203942233011", "202190000085",
				"102040000224", "617051001100", "711121006001", "611081006001", "203955703103", "203955704104",
				"203955705105", "203975802102", "203975803103", "203975804104", "203975805105", "203975806106",
				"102045319109", "705648512050", "601068205050", "102040000228", "102040000221", "002010010000",
				"602268306050", "711121007001", "605098403050", "605028518050", "705528404050", "603238306050",
				"603018315050", "507021005050", "100000000014", "616011016000", "611261002001", "611151002001",
				"711751003001", "611221003001", "611151005001", "611191006001", "000220023800", "611061002001",
				"602301001050", "705648014050", "605168534050", "605148513050", "605198509050", "705648515050",
				"611161005001", "602258203050", "701598206050", "603078312050", "701600001001", "701600001002",
				"701600001003", "701600001004", "701600001005", "701760001001", "701760001002", "701760001003",
				"701760001004", "701760001005", "701760001006", "701610001001", "601140013001", "702070011018",
				"702060001001", "601160001001", "601160001002", "605068501050", "605068502050", "611041008000",
				"611051007001", "611161008001", "611041009001", "507011006050", "711131009001" };
		int index = (int) (Math.random() * caddresscode.length);
		String addresscode = caddresscode[index];
		return addresscode;
	}

	public static String getRandomCCOLLECTIONADDRESS() {
		String ccollectionaddress1[] = { "山东路", "江西路", "合肥路", "哈尔滨路", "劲松三路", "辽阳西路", "长沙路", "昌乐路", "敦化路", "镇江路",
				"镇江南路" };
		String ccollectionaddress2[] = { "延吉路", "宁夏路", "银川西路", "重庆南路", "福州北路", "辽阳东路", "香港中路", "东海西路", "郑州路", "株洲路",
				"九水路" };
		int index1 = (int) (Math.random() * ccollectionaddress1.length);
		int index2 = (int) (Math.random() * ccollectionaddress2.length);
		String collectionaddress = ccollectionaddress1[index1] + ccollectionaddress2[index2] + "卡口";
		return collectionaddress;
	}

	public static String getRandomCCOLLECTIONAGENCIES() {
		String ccollectionagencies[] = { "370202000000", "370203000000", "370213000000", "370214000000" };
		int index = (int) (Math.random() * ccollectionagencies.length);
		String collectionagencies = ccollectionagencies[index];
		return collectionagencies;
	}

	public static String getRandomCDATASOURCE() {
		String cdatasource[] = { "1", "2" };
		int index = (int) (Math.random() * cdatasource.length);
		String datasource = cdatasource[index];
		return datasource;
	}

	public static String getRandomCSNAPTYPE() {
		String csnaptype[] = { "1", "2" };
		int index = (int) (Math.random() * csnaptype.length);
		String snaptype = csnaptype[index];
		return snaptype;
	}

	public static String getRandomCDEVICECODE() {
		String cdevicecode[] = { "370213000000012004", "370213000000171665", "370213000000171767", "370213000000012008",
				"370213000000112029", "370213000000012200", };
		int index = (int) (Math.random() * cdevicecode.length);
		String devicecode = cdevicecode[index];
		return devicecode;
	}

	public static String getRandomCLANENUMBER() {
		String clanenumber[] = { "01", "02" };
		int index = (int) (Math.random() * clanenumber.length);
		String lanenumber = clanenumber[index];
		return lanenumber;
	}

	public static String getRandomNVEHICLESPEED() {
		String nvehiclespeed[] = { "0", "1" };
		int index = (int) (Math.random() * nvehiclespeed.length);
		String vehiclespeed = nvehiclespeed[index];
		return vehiclespeed;
	}

	public static String getRandomCPIC1PATH() {
		String cpic1path1[] = { "202180111260", "705648014050", "605168506050", "805998504050", "705648508050" };
		String cpic1path2[] = { "20", "21", "19" };
		String cpic1path3[] = { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14",
				"15" };
		UUID uuid1 = UUID.randomUUID();
		String cid = uuid1.toString().replaceAll("-", "");
		int index1 = (int) (Math.random() * cpic1path1.length);
		int index2 = (int) (Math.random() * cpic1path2.length);
		int index3 = (int) (Math.random() * cpic1path3.length);
		String pic1path = "ftp://zhdd:zhdd123@10.16.1.109:21/guoche/" + cpic1path1[index1] + "/" + cpic1path2[index2]
				+ "/" + cpic1path3[index3] + ".jpg";
		return pic1path;
	}

	public static String getRandomCPIC2PATH() {
		return null;
	}

	public static String getRandomCPIC3PATH() {
		return null;
	}

	public static String getRandomNDERICTRION() {
		String nderictrion[] = { "1", "2", "3", "4" };
		int index = (int) (Math.random() * nderictrion.length);
		String derictrion = nderictrion[index];
		return derictrion;
	}

	public static String getRandomCARCOLOR() {
		String ncarcolor[] = { "A", "B", "J", "G" };
		int index = (int) (Math.random() * ncarcolor.length);
		String carcolor = ncarcolor[index];
		return carcolor;
	}

	public static String getRandomCARBRAND() {
		String ncarbrand[] = { "5", "15", "54", "78", "17", "8", "30", "40", "19", "22", "34", "2", "13" };
		int index = (int) (Math.random() * ncarbrand.length);
		String carbrand = ncarbrand[index];
		return carbrand;
	}

	public static String getRandomCHISCARNUMBER() {
		String chiscarnumber[] = { "0", "1", "2" };
		int index = (int) (Math.random() * chiscarnumber.length);
		String hiscarnumber = chiscarnumber[index];
		return hiscarnumber;
	}

	public static String getRandomTARGETTYPE() {
		String targettypes[] = { "01", "02" };
		int index = (int) (Math.random() * targettypes.length);
		String targettype = targettypes[index];
		return targettype;
	}

	public static String getRandomXYWH() {
		String XYWH1[] = { "0", "627", "1585", "2261", "1004", "1402", "424", "875", "931", "929", "941", "891" };
		String XYWH2[] = { "1772", "1643", "1389", "1786", "1550", "1640", "0", "1774", "1443", "1577", "1703",
				"1426" };
		String XYWH3[] = { "113", "126", "135", "137", "138", "139", "148", "125", "145", "154", "132", "133" };
		String XYWH4[] = { "29", "35", "36", "38", "39", "47", "55", "58" };
		String XYWH5[] = { "1", "2", "3", "4" };
		int index1 = (int) (Math.random() * XYWH1.length);
		int index2 = (int) (Math.random() * XYWH2.length);
		int index3 = (int) (Math.random() * XYWH3.length);
		int index4 = (int) (Math.random() * XYWH4.length);
		int index5 = (int) (Math.random() * XYWH5.length);
		String xywh = XYWH1[index1] + "/" + XYWH2[index2] + "/" + XYWH3[index3] + "/" + XYWH4[index4] + "/"
				+ XYWH5[index5];
		return xywh;
	}

	private static void writeHdfs(long c, String file, int flag) {

		FSDataOutputStream fsDataOutputStream = null;
		ByteArrayInputStream byteArrayInputStream = null;

		try {

				while(flag < day){
					
					String dkdata = "dkdata2";
					fs.mkdirs(new Path(dkdata));
					String afterNDayStr = FormatUtils.parse(fromDay, flag);
//					System.out.println(FormatUtils.parse(fromDay, i));
					fsDataOutputStream = fs
							.create(new Path(dkdata + "/" + afterNDayStr + "-" + System.currentTimeMillis()));
					// System.out.println(dkdata+"/"+DateUtils.getAfterNDayStr(i)+"/"+System.currentTimeMillis());

					long currentCount = 0;
					
					while (currentCount < c) {
						byteArrayInputStream = new ByteArrayInputStream(getData(afterNDayStr).getBytes());
						org.apache.hadoop.io.IOUtils.copyBytes(byteArrayInputStream, fsDataOutputStream, 1024);
						currentCount++;
					}
				}

			fsDataOutputStream.close();
			byteArrayInputStream.close();
			System.out.println("finish！" + file);
			System.out.println();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("error！");
		}
	}

	private static void writeToHdfs() {
		
		long count = 100000L;
		long step = 100000000L;

		System.out.println("prepare generage：" + count + "count，toFile：" + defaultFilePath + fileName + suffix);
		
		for (long i = 0; i < count; i += step) {
			String _file = defaultFilePath + fileName;
			if (count <= step) {
				_file = _file + suffix;
				writeHdfs(count, _file, j);
			} else {
				_file = _file + "_" + i / step + suffix;
				if (i + step >= count) {
					writeHdfs(count - i, _file, j);
				} else
					writeHdfs(step, _file, j);
			}
		}
		
		j ++;
		System.out.println("finish！");
	}


	public static void main(String[] args) throws Exception {

		GenerageDate generageDate = new GenerageDate();
		
//		count = 100000;
		fromDay = "2015-01-11 23:22:45";
		day = 10;

		rand = 99999;
		Num = 0;
		
		LinkedBlockingDeque<Runnable> workQueue = new LinkedBlockingDeque<Runnable>(100000);
		
		ThreadPoolExecutor excuter = new ThreadPoolExecutor(5, 5, 1000 * 60L, TimeUnit.SECONDS, workQueue);
		for(int i = 0; i < 5; i ++){
			excuter.execute(generageDate.new Task());
		}
	}
	
	 class Task implements Runnable{

		@Override
		public void run() {
			writeToHdfs();
		}
		
	}
	
}
