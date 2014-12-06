package pku.eu;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.LinkedList;
import java.util.logging.Level;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.logging.LogFactory;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

import com.gargoylesoftware.htmlunit.html.HtmlPage;



public class spider {
				public LinkedList<String> isVisitedUrl =new LinkedList<String>();
				public LinkedList<String> unVisitedUrl =new LinkedList<String>();
				public int countNum=1;
				public int filename=2000;
				fileDel del=new fileDel();
	public static void main(String [] args) throws FailingHttpStatusCodeException, IOException, InterruptedException{
			
				spider apSpider=new spider();
				apSpider.start();
	}
	/*
	 public  void FileInputStreamDemo(String path) throws IOException{
	        File file=new File(path);
	        if(!file.exists()||file.isDirectory())
	            throw new FileNotFoundException();
	        FileInputStream fis=new FileInputStream(file);
	        byte[] buf = new byte[1024];
	        StringBuffer sb=new StringBuffer();
	        while((fis.read(buf))!=-1){
	            sb.append(new String(buf));    
	            buf=new byte[1024];/
	        }
	        System.out.println(sb.toString());
	    }
	 */

	private void serachUrl(String path) throws FailingHttpStatusCodeException, MalformedURLException, IOException {
		// TODO Auto-generated method stub
		 LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log","org.apache.commons.logging.impl.NoOpLog");
		  java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
		  
		String str=path;
		WebClient webClient=new WebClient(BrowserVersion.FIREFOX_24);
		webClient.getOptions().setJavaScriptEnabled(true);
		webClient.getOptions().setCssEnabled(false);
		webClient.getOptions().setActiveXNative(false);
		webClient.getOptions().setDoNotTrackEnabled(false);
		webClient.getOptions().setRedirectEnabled(true);
		webClient.getOptions().setThrowExceptionOnScriptError(false);
		 webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
		try {
			HtmlPage page=webClient.getPage(str);
			//webClient.waitForBackgroundJavaScript(100);
			if(page.asText()!=null){
				//System.out.println(page.asText());
				if (path.startsWith("http://sports.sina.com.cn/g/laliga")) {
					downHtmlFile(page);
					//combineFile newCombineFile =new combineFile();
					//newCombineFile.delDir("data");
				}
				
				
			
			isVisitedUrl.addLast(path);
			java.util.List<HtmlAnchor> achList=page.getAnchors();
			for(HtmlAnchor ach:achList){
				String con=ach.getHrefAttribute().toString();
				if ((isVisitedUrl.contains(con)==false)&&(unVisitedUrl.contains(con)==false)) {
					if (con.startsWith("http://sports.sina.com.cn/g/")) {
						unVisitedUrl.addLast(con);
						System.out.println(con);
					}

				}

			 }
			}
		webClient.closeAllWindows();
	
		} catch (Exception e) {
			// TODO: handle exception
			return ;
		}
			
	}

	private void downHtmlFile(HtmlPage page) throws Exception {
		// TODO Auto-generated method stub


		String pathString=page.getTitleText().replaceAll("[\\-\\?/:*|<>\"]", "_");
		
		//File file=new File(pathString+".html");
		//file.createNewFile();
		
		File file2=new File("data/"+filename+".txt");
		file2.createNewFile();

		//FileWriter write=new FileWriter(file,true);
		FileWriterWithEncoding write2=new FileWriterWithEncoding(file2, "UTF-8");
		//write.write(page.asXml());
		write2.write(page.asText());
		filename++;
		System.out.println("store-success----"+page.getTitleText());
		
		File dir=new File("data");
        if(dir.isDirectory()){
            File[] tmp=dir.listFiles();
            for (int i = 0; i < tmp.length; i++) {
            	if (del.getFileSizes(tmp[i])==0) {
					tmp[i].delete();
				}
			}
}
		
	}
	
	private void start() throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException{
		String pathString="http://sports.sina.com.cn/g/laliga/";
		unVisitedUrl.addFirst(pathString);
		serachUrl(pathString);
		while (true) {
			pathString=unVisitedUrl.removeFirst();
			if (pathString.startsWith("http://sports.sina.com.cn/g/")) {
				serachUrl(pathString);
			}


			System.out.println("=========================>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+unVisitedUrl.size());
		}
		
		
	}

}
