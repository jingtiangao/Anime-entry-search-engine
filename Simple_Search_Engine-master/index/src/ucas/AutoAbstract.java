package ucas;

import java.lang.Character.Subset;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

public class AutoAbstract {
	public static void main(String arg[])
	{
		String query="易建联广东";
		String resString =getAbstract("　　新浪体育讯　北京时间12月5日，2014-15赛季CBA第15轮，广东主场迎战东莞。易建联独砍40+，广东队压倒性优势以109比86赢下东莞德比战，取得四连胜。　　首节两队展开对攻战，比分交替上升。战至14平后，广东队内线连投带罚得到5分，逐渐占据了比赛主动权。首节结束，东莞队将比分追到25比28。　　次节广东队继续领跑比赛，利用阿联在内线的优势，一度49比34领先达15分。东莞队在本节后半段终于找回自己的节奏，孙桐林篮下命中，41比49，半场结束。　　易边再战，广东队3分钟内打出一个11比2的冲击波，将比分改写为60比43。东莞队暂停后依旧没有找到有效的进攻手段，分差被拉大到20+。尽管阿联下场休息，广东队仍以81比58领先23分进入末节。　　末节广东队全华班锻炼阵容。此前外线6投不中的朱芳雨终于找到手感，广东队一直领跑并不断扩大领先。最终广东队主场轻取东莞，取得4连胜。　　本周日，广东客战上海，东莞主场对阵八一。　　双方首发：　　广东：陈江华，易建联，周鹏，任骏飞，朱芳雨　　东莞：赵捷，顾全，孙桐林，迪奥古，布朗　　　　(小安) , ", query);
		System.out.println(resString);
	}
	public static String getAbstract(String content,String Query) {
		Set<Integer> posSet = new TreeSet();
		String ResultStr="";
		String result = IKAnalzyerDemo.Spilt2Words(Query);
		String[] wordsArray = result.split("\\|");
		for (int i = 0; i < wordsArray.length; i++) {
			int myIndex=0,prePos=0;
			String fullContent=content;
			myIndex=fullContent.indexOf(wordsArray[i]);
			while(myIndex>0)
			{	
				prePos += myIndex;
				posSet.add(prePos);
				fullContent = fullContent.substring(myIndex+1);
				myIndex = fullContent.indexOf(wordsArray[i]);
			}	
		}
		int preIndex,nowIndex;
		preIndex=0;
		for(Integer i: posSet){
			nowIndex=i;
			System.out.println(nowIndex);
			//将位置的前后五个字写入摘要
			if((nowIndex - preIndex)<5) ResultStr+=content.substring(nowIndex,nowIndex+5);
			else if((nowIndex - preIndex)<10) ResultStr+=content.substring(preIndex+5,nowIndex+5);
			else { 
				ResultStr+=".....";
				ResultStr+=content.substring(nowIndex-5,nowIndex+5);
			}
			preIndex = nowIndex;
			System.out.println(i);
		}
		return ResultStr;
	}
}
