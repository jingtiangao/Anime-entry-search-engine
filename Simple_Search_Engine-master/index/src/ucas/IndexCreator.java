package ucas;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import jeasy.analysis.MMAnalyzer;

public class IndexCreator {

	public static void main(String[] args) throws IOException {

		 IndexOperation.Json2Docs();  //提取新闻内容
		 split2Words();  //切词
		 CreateIndex();  //创建倒排索引
		 try {   
		 CreateTfidf(); //计算tf-idf
		 } catch (IOException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 } 
		StaticsWords();  //统计词项出现次数,这个是在所有文档中出现的次数
	}

	public static Set<String> GetStopWordSet(String stopWordPath)
			throws IOException {
		Set<String> resSet = new HashSet<>();
		File f = new File(stopWordPath);
		try {
			String s = null;
			BufferedReader rin = new BufferedReader(new FileReader(f));
			while ((s = rin.readLine()) != null) {
				resSet.add(s);
			}
			rin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resSet;
	}

	private static void CreateIndex() {

		Set<String> stopSet = new HashSet<String>();
		try {
			stopSet = GetStopWordSet("data/stopwords.txt");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HashMap<String, String> hashResult = new HashMap<String, String>();
		String fileName = "";
		// 读取文件
		try {
			for (int fileIndex = 1; fileIndex <= 100000; fileIndex++) {
				fileName = fileIndex + ".txt";
				File mFile = new File("data/words/" + fileName);
				if (!mFile.exists()) {
					System.out.println(fileName + "不存在");
					continue;
				}
				HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
				String content = ReadAndWrite.readFileByChars("data/words/"
						+ fileName, "utf-8");
				String[] wordArray = content.split("\\|"); // “|”是转义字符 so need to addd \\
				/*split 方法:将一个字符串分割为子字符串，然后将结果作为字符串数组返回*/
				for (int i = 0; i < wordArray.length; i++) {

					if (!IsValued(wordArray[i])
							|| stopSet.contains(wordArray[i])) {
						continue;//如果扩展停止词典里有这个词就不搞索引了，直接跳转下一个
					}
					if (hashMap.keySet().contains(wordArray[i])) {
						Integer integer = (Integer) hashMap.get(wordArray[i]);//获取索引中对应词的value
						int value = integer.intValue() + 1;
						hashMap.put(wordArray[i], new Integer(value));
					} else
						hashMap.put(wordArray[i], new Integer(1));//Integer(1)为包装类，是存在堆里的
				}
				for (String str : hashMap.keySet()) {
					//遍历HashMap获得所有索引词的集合
					/*
					 * //获得标题 String strNum =
					 * fileName.replaceAll("news.cnblogs.com_n_", ""); strNum =
					 * strNum.replaceAll("_.txt", ""); String title =
					 * ReadAndWrite
					 * .readFileByChars("titleDoc/"+strNum+".txt","UTF-8");
					 */

					// 获得跟词相关的部分内容
					/*
					 * String fullContent =
					 * ReadAndWrite.readFileByChars("data/docs/"+fileName,
					 * "UTF-8"); String partContent = ""; int myIndex =
					 * fullContent.indexOf(str);//包含词的位置 int preIndex = 0;
					 * while(myIndex>0) { partContent+=(preIndex+myIndex+"#");
					 * preIndex += myIndex+1; fullContent =
					 * fullContent.substring(myIndex+1); myIndex =
					 * fullContent.indexOf(str);
					 * 
					 * } //形成倒排索引
					 */
					String tmp = fileName + "#" + hashMap.get(str);// 最后一个为出现次数
					if (hashResult.keySet().contains(str)) {// 包含该词
						String value = (String) hashResult.get(str);
						value += ("@" + tmp);//如果包含该词说明不同的文档里都有它所以加上@来区分不同文档的value
						hashResult.put(str, value);
					} else
						hashResult.put(str, tmp);
				}
				System.out.println(fileName);
			}//至此计算出了所有文档中所有分词出现的次数,并弄出了每个词在不同文档中出现的次数和在同一文档中出现的次数
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {//finally它只能用在try/catch语句中，并且附带着一个语句块，表示这段语句最终总是被执行。
			if (hashResult.size() > 0) {
				String value = "";

				for (String str : hashResult.keySet()) {
					String tmp = str + "&" + hashResult.get(str);// liang ge
																	// kong ge
					String[] timeWord = tmp.split("@"); // “|”是转义字符
					// 统计在文档中出现的总次数
					tmp += "&" + timeWord.length; //最后一个timeWord.length为这个词出现在了几个文档中
					System.out.println(tmp);//word&filename#times@filename1#times &length
											//用@分开以后就统计出了一个词出现在了几个文档中

					ReadAndWrite.WriteAppend("data/index.txt", tmp + "\r\n");
				}
			}
		}
	}

	public static boolean IsValued(String content) {
		// TODO Auto-generated method stub
		if (content.length() == 1) {
			return false;
		}
		try {
			int num = Integer.valueOf(content);// 把字符串强制转换为数字
			return false;// 如果是数字，返回True // maybe its wrong
		} catch (Exception e) {
			return true;// 如果抛出异常，返回False
		}
	}

	public static void writeFileByChars(String fileName, String value) {
		String path = fileName;
		try {
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(path, false), "UTF-8"));

			String[] arryStr = value.split("#LINE#");
			for (int i = 0; i < arryStr.length; i++) {
				System.out.println(arryStr[i]);
				// ByteBuffer bb = ByteBuffer.wrap(val.getBytes());
				bw.write(arryStr[i]);
				bw.write(13);
				bw.write(10);
			}

			bw.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void split2Words() throws IOException {
		String fileName = "";
		String valueTemp = "";
		for (int fileIndex = 1; fileIndex <= 10000; fileIndex++) {
			fileName = fileIndex + ".txt";
			valueTemp = "";
			File mFile = new File("data/docs/" + fileName);
			if (!mFile.exists() || fileIndex == 5168) {
				System.out.println(fileName + "不存在");
				continue;
			}
			String text = ReadAndWrite.readFileByChars("data/docs/" + fileName,
					"utf-8");
			// MMAnalyzer analyzer = new MMAnalyzer();
			// valueTemp += analyzer.segment(text, "|");
			valueTemp = IKAnalzyerDemo.Spilt2Words(text);
			System.out.println(fileName);
			ReadAndWrite.writeFileByChars("data/words/" + fileName, valueTemp);
		}
	}

	public static void CreateTfidf() throws IOException {
		File f = new File("data/index.txt");
		try {
			String s = null;
			BufferedReader rin = new BufferedReader(new FileReader(f),50000);
			//BufferedReader(Reader in, int sz)
	        // 创建一个使用指定大小输入缓冲区的缓冲字符输入流
			int i = 0;
			while ((s = rin.readLine()) != null) {
				try {
					// 对于索引文件的每个词都需要计算出TF,IDF值，然后按照wordkey&文档名@tfidf值#next#&文档名@tfidf
					//格式写入到下面的文件中
					ReadAndWrite.WriteAppend("data/tfidf_index.txt",
							IndexOperation.getTfidfString(s) + "\r\n");
					System.out.println(i++);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			rin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static void StaticsWords() throws IOException {
		File f = new File("data/index.txt");
		try {
		//	HashMap< Integer,String> wordsHashMap = new HashMap<>();
			String s = null;
			BufferedReader rin = new BufferedReader(new FileReader(f));
			int i = 0;
			while ((s = rin.readLine()) != null) {
				try {
					String[] arrayStrings = s.split("&");
					//word&filename#times@filename1#times &length
					String wordKey = arrayStrings[0];
					String numString = arrayStrings[2];//it is length
					// 写入
					ReadAndWrite.WriteAppend("data/statis_words.txt",
							wordKey+"&"+numString+"\r\n");
					System.out.println(i++);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			rin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}
