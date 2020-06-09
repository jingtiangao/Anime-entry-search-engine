/**
 * IK 中文分词  版本 5.0.1
 * IK Analyzer release 5.0.1
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * 源代码由林良益(linliangyi2005@gmail.com)提供
 * 版权声明 2012，乌龙茶工作室
 * provided by Linliangyi and copyright 2012 by Oolong studio
 * 
 * 
 */
package ucas;

import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 使用IKAnalyzer进行分词的演示
 * 2012-10-22
 *
 */
public class IKAnalzyerDemo {
	
	public static void main(String[] args){
		//System.out.println(Spilt2Words("　　认为林书豪仅仅是紫金军团在一号位的临时选择，并非是一名合格的首发控卫，而湖人需要在史蒂夫-纳什倒下之后找到一个新的组织者。　　沃纳洛夫斯基的原话是：“我认为林只是湖人的权宜之计，他是一名NBA替补级别的控卫。很明显是因为他们的阵容缺陷，以及史蒂夫-纳什因伤赛季报销，他才被强行顶上首发。你能看到林自信全无，在他和科比之间，显然没有产生很好的化学反应。”　　相比之下，隆多和科比虽然曾经作为总决赛的对手相互敌对，但彼此间却非常尊重。隆多目前的合同已进入最后一年，如果丹尼-安吉不打算在明年夏天与之续约，那么凯尔特人会在2月交易截止期前尽力将他送走。　　如果隆多在波士顿呆到明年6月，那么按照劳资协议规定，绿军可以提供比其他球队更高的合同，但问题的关键在于，志在重建的他们，会愿意为隆多送上顶薪合同么？而且对于隆多来说，是否愿意留下来陪着年轻人不断成长，亦或是去一支更有竞争力的球队追求胜利，也存在着巨大疑问。　　当然，仅仅一起吃了顿早餐，就说隆多要去湖人了，这未免为时过早。只是考虑到两支球队以及两名球员各自所处的境况，这实在让人忍不住想入非非。　　(熊猫) , "));
	System.out.println(Spilt2Words("stay night》（日语：フェイト/ステイナイト，中文：菲特，今晚留下来）是由TYPE-MOON于2004年1月30日发售的PC平台文字冒险游戏，并于2005年10月28日发售FAN DISC《Fate/hollow ataraxia》。2006年1月播放动画版。2010年1月23日剧场版 Fate/stay night - Unlimited Blade Works在日本本土上映，BD-DVD于2010年9月30日正式发行。同时已推出前传小说及动画《Fate/Zero》。拥有众多以Fate打头的相关作品。由ufotable制作的新TV动画分割为两季，第一季2014年10月开始放送，第二季2015年4月开始放送。Heaven's Feel剧场版制作决定！本作品是TYPE-MOON自同人社团转为商业公司后发表的首部作品，和TYPE-MOON发表的《月姬》、《空之境界》、《魔法使之夜》、《Girl's Work》、《冰之花》等多部作品有着共通世界观。作品中的攻略路线共3条，分别为Fate（命运）、Unlimited Blade Works（无限剑制）、Heaven's Feel（宛若天堂/天之杯），分别对应Saber、远坂凛、间桐樱三位女主角，5个线路结局（Fate线一个True End：梦的延续、Unlimited Blade Works一个Good End：sunny day和一个True End：brilliant years、Heavens'Feel一个Normal End：樱之梦和一个True End：春天归来），除此之外还有40个Bad End/Dead End嗯？你问我Bed End？），以及PS2版追加的LAST EPISODE。在开发初期，原本亦有伊莉雅线的制作计划，后因种种原因被取消，余下相关设定和剧情被整合至Heaven's Feel线路、Unlimited Blade Works线路TV版以及其他后续衍生作品中。应该还有很大一部分在蘑菇脑子里藏着，随时准备在各种作品里加一点骗钱。舞台，是被山与海所包围的城市·冬木市，在那里，进行着一个仪式。"));
	}

	public  static String Spilt2Words(String content) {
		String resString = "";
		//构建IK分词器，使用smart分词模式
		Analyzer analyzer = new IKAnalyzer(true);
		
		//获取Lucene的TokenStream对象
	    TokenStream ts=null;
		try {
			//myfield什么意思
			ts = analyzer.tokenStream("myfield", new StringReader(content));
		    //获取词元文本属性
		    CharTermAttribute term = ts.addAttribute(CharTermAttribute.class);
		    	    
		    //重置TokenStream（重置StringReader）
			ts.reset(); 
			//迭代获取分词结果
			while (ts.incrementToken()) {
				resString+=term.toString()+"|";
			}
			//关闭TokenStream（关闭StringReader）
			ts.end();   // Perform end-of-stream operations, e.g. set the final offset.

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//释放TokenStream的所有资源
			if(ts != null){
		      try {
				ts.close();
		      } catch (IOException e) {
				e.printStackTrace();
		      }
			}
	    }
		return resString;
	}
}
