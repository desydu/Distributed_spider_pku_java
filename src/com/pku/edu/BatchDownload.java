package com.pku.edu;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;

import org.jsoup.Jsoup; 
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class BatchDownload {

        /**
         * 程序说明：批量抓取网页图片下载到本地
         * 
         * @param args
         */
        public static void main(String[] args) {
                //百度图片
                String fromUrl = "http://www.douban.com/search?cat=1025&q=%E6%A2%85%E8%A5%BF";
                
                StringBuffer pageContents = new StringBuffer();
                //System.out.println("asdasdas");
                try{
    
                	URL startUrl = new URL(fromUrl);
	                pageContents = downloadPage(startUrl);
	                //System.out.println(pageContents);
	               // if(pageContents.length()==0) System.out.println("asdasdas");
	                List<String> imgUrls = getImageUrls(pageContents);
	                //System.out.println(imgUrls.size());
	                
	                
	                //downloadImages(pageContents);
	                //if(pageContents.length()==0) System.out.println("asdasdas");
	                //System.out.println(pageContents);
                }
                catch(Exception e)
                {
                	
                }
                
                
        }
        
        public static StringBuffer downloadPage(URL httpUrl)throws MalformedURLException,IOException 
		{
		    StringBuffer data = new StringBuffer();		
		    String currentLine;		
		    // 打开输入流
		    BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(httpUrl), "GBK"));
		    // 读取数据
		    while ((currentLine = reader.readLine()) != null) {
		        data.append(currentLine);
		    }
		    reader.close();
		
		    return data;
		}
				
        public static File downloadFile(String httpUrl, String fileSavePath)throws MalformedURLException, IOException
		{
		    File file = new File(fileSavePath);
		    if (!file.exists()) {
		        file.createNewFile();
		    }
		    URL url = new URL(httpUrl);
		    // 打开输入流
		    BufferedInputStream in = new BufferedInputStream(
		            getInputStream(url));
		
		    // 打开输出流
		    FileOutputStream out = new FileOutputStream(file);
		
		    byte[] buff = new byte[1];
		    // 读取数据
		    while (in.read(buff) > 0) {
		        out.write(buff);
		    }		
		    out.flush();
		    out.close();
		    in.close();
		    return file;
		}
        
        private static void downloadImages(StringBuffer pageContents)throws MalformedURLException, IOException 
		{	        
	        // 获取html页面
	        StringBuffer page = pageContents;
	        // 获取页面中的地址
	        List<String> imgUrls = getImageUrls(page);
	        // 保存图片，返回文件列表
	        List<File> fileList = new ArrayList<File>();
	        String imgSaveDir="E:";
	        int i = 1;
	        for (String url : imgUrls) 
	        {
	        	String fileName = url.substring(url.lastIndexOf("/") + 1);
	        	File file = downloadFile(url, imgSaveDir + "\\" + fileName);
	            System.out.println(file.getPath()+ " 下载完成！");
	            fileList.add(file);
	            i++;
	        }	        
	    }
        
		private static InputStream getInputStream(URL httpUrl) throws IOException 
		{
	        // 网页Url
	        URL url = httpUrl;
	        URLConnection uc = url.openConnection();
	        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
	        return uc.getInputStream();
	    }
        
        
		
        
        public static List<String> getImageUrls(StringBuffer html) 
		{
	        List<String> result = new ArrayList<String>();
	        // 将字符串解析为html文档
	        Document doc = Jsoup.parse(html.toString());
	        // 获取img标签
	        Elements es =doc.getElementsByTag("img");
	        //Element es = doc.getElementById("script");
	        //Elements ss = new Elements();
	        //ss.add(es);
	        //System.out.println(es.size());
	         //doc.getElementsByTag("img");
	        
	        // 获取每一个img标签src的内容，也就是图片地址
	        for (Iterator<Element> i = es.iterator(); i.hasNext();) 
	        {
	            Element e = i.next();
	            String r = e.attr("src");
	            Pattern p = Pattern.compile("http://.+\\.(jpg|jpeg)");
	            Matcher m = p.matcher(r);
	            if (m.matches()) 
	            {
	                result.add(r);
	            }
	        }
	        return result;
	    }
}
        
        
      