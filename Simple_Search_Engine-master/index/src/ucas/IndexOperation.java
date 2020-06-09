package ucas;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class IndexOperation {
	
	
	//返回HashMap，健为单词，词为倒排索引
	public static HashMap<String, String> getHashMap(String IndexPath) throws IOException
	{
		HashMap<String, String> resMap = new HashMap<String,String>();
		File f = new File(IndexPath);
		try {
			String s = null;
			BufferedReader rin = new BufferedReader(new FileReader(f));
			while ((s = rin.readLine()) != null) {
				String wordKey; // 要索引的单词
				String valueString; // 值
				String[] arrayStrings = s.split("&");
				wordKey = arrayStrings[0];
				valueString =arrayStrings[1];
				
				resMap.put(wordKey, valueString);
			}
			rin.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return resMap;
	}

	//构造函数，传入需要倒排索引记录，构造tfidf_index.txt使用
	public static String getTfidfString(String content) {
		String wordKey; //要索引的单词
		int numbersOfDocs; //出现次数 这个值指的是一个词出现在了多少个文档里
		String tfidfStrs; //tfidf的值
		tfidfStrs="";
		String[] arrayStrings = content.split("&");
		wordKey = arrayStrings[0];
		numbersOfDocs = Integer.parseInt(arrayStrings[2]);//出现次数 
															//这个值指的是一个词出现在了多少个文档里
		
		String[] docsListArray=arrayStrings[1].split("@");//一个词在不同的文档里面出现的次数
		
		tfidfStrs+=wordKey;
		for (int i = 0; i < docsListArray.length; i++) {//对于这个词出现的每一个文档都要算权值
														//即所谓的T-D
			String[] valuesStrings = docsListArray[i].split("#");
			String filenameString = valuesStrings[0];//valuesStrings0是文档的名字
			int timesOfwords = Integer.parseInt(valuesStrings[1]);//valuesStrings1是在文档中出现的次数
		//	int numOfDoc = Integer.parseInt(valuesStrings[1]);
			
			DecimalFormat dec = new DecimalFormat("0.0000");
			double tfidf = Tf_idf( timesOfwords, numbersOfDocs);
			
			tfidfStrs+="&";
			tfidfStrs+=valuesStrings[0];
			tfidfStrs+="@";
			tfidfStrs+=dec.format(tfidf);
			if (i!=docsListArray.length) {//看出现这个词的文档是不是遍历完了，否则加#next#
				tfidfStrs+="#next#";     //所以每一个tfidf值的最后都是#next
			}
			//ttfidfstr结构：wordkey&文档名@tfidf值#next#&文档名@tfidf
		}
		return tfidfStrs;//把这个词对应的所有文档的名字加tfidf值
	}
	
	//返回某单词出现的文档列表
	public static void GetDocsList(String wordName){
		
	}
	
	//计算TF-IDF的公式
	public static double Tf_idf(int timeofWords,int timesOfDocs) {
		double d1 = ((double)Math.log10(timeofWords)+1.0) ;  //数值太小
		double d2= Math.log10(10000/timesOfDocs + 0.01);
		return d1*d2;
		/*其中，dft 是出现词项 t 的文档数目成为逆文档频率，tft,d 是指 t 在 d 中出现的次数，是与
		 * 文档相关的一个量，成为词项频率。随着词项频率的增大而增大，随着词项罕见度的增加而增大*/
	}	
	
	//将Json转换成docs文档
	public static void Json2Docs() {
		String fileName = "";
		//读取文件
		for (int fileIndex = 1; fileIndex <= 10000; fileIndex++){
			fileName = fileIndex+".json";
			File mFile = new File("data/jsons2/"+fileName);
			if (!mFile.exists()) {
				System.out.println(fileName+"不存在");
				continue;
			}
			try {
				JsonObject Myobject = JsonUtil.readFile2JsonObject("data/jsons2/"+fileName);
				Myobject.fileName = fileName;  
				ReadAndWrite.writeFileByChars("data/docs/"+fileIndex+".txt", Myobject.articalString.toString());
				ReadAndWrite.WriteAppend("data/AttrIndex.txt",fileName +"\t"+ Myobject.timeString.toString() 
						+"\t"+Myobject.totalString.toString()+"\r\n");
				System.out.println(fileName);
			} catch (Exception e) {
				//continue;
				e.printStackTrace();
				// TODO: handle exception
			}
			
		//System.out.println("导出完成！！！");
		}
	}
}
