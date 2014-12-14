package cluster.cn.edu.pku.ss;
import java.io.IOException;
import java.io.StringReader;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class WrodSplit {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String text = 
				"一个名字叫文件夹加密的小软件密码忘记了 " +
				"这个文件夹我想删除没法删除重装系统也没有用现在求助网友";
		
		//创建分词对象
		IKAnalyzer ana = new IKAnalyzer(true);
		StringReader reader = new StringReader(text);
		
		//分词
		TokenStream ts=ana.tokenStream("", reader); 
		CharTermAttribute term = ts.getAttribute(CharTermAttribute.class);
		
		//遍历分词数据
		while(ts.incrementToken()){
			System.out.print(term.toString()+"|");
		}
		
		reader.close();
		System.out.println();
	}
}
