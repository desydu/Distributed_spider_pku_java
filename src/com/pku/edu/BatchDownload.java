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

    public static void main(String[] args) {
       
        String fromUrl = "http://www.douban.com/search?cat=1025&q=%E6%A2%85%E8%A5%BF";

        StringBuffer pageContents = new StringBuffer();
      
        try {

            URL startUrl = new URL(fromUrl);
            pageContents = downloadPage(startUrl);
          
            List<String> imgUrls = getImageUrls(pageContents);
            
        } catch (Exception e) {

        }


    }

    public static File downloadFile(String httpUrl, String fileSavePath) throws MalformedURLException, IOException {
        File file = new File(fileSavePath);
        if (!file.exists()) {
            file.createNewFile();
        }
        URL url = new URL(httpUrl);
       
        BufferedInputStream in = new BufferedInputStream(
                getInputStream(url));

       
        FileOutputStream out = new FileOutputStream(file);

        byte[] buff = new byte[1];
  
        while (in.read(buff) > 0) {
            out.write(buff);
        }
        out.flush();
        out.close();
        in.close();
        return file;
    }

    public static StringBuffer downloadPage(URL httpUrl) throws MalformedURLException, IOException {
        StringBuffer data = new StringBuffer();
        String currentLine;
       
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream(httpUrl), "GBK"));
       
        while ((currentLine = reader.readLine()) != null) {
            data.append(currentLine);
        }
        reader.close();
        return data;
    }

    private static InputStream getInputStream(URL httpUrl) throws IOException {
  
        URL url = httpUrl;
        URLConnection uc = url.openConnection();
        uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        return uc.getInputStream();
    }

    private static void downloadImages(StringBuffer pageContents) throws MalformedURLException, IOException {

        StringBuffer page = pageContents;

        List<String> imgUrls = getImageUrls(page);

        List<File> fileList = new ArrayList<File>();
        String imgSaveDir = "E:";
        int i = 1;
        for (String url : imgUrls) {
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            File file = downloadFile(url, imgSaveDir + "\\" + fileName);
            System.out.println(file.getPath() + " ������ɣ�");
            fileList.add(file);
            i++;
        }
    }
    public static List<String> getImageUrls(StringBuffer html) {
        List<String> result = new ArrayList<String>();

        Document doc = Jsoup.parse(html.toString());
 
        Elements es = doc.getElementsByTag("img");

        for (Iterator<Element> i = es.iterator(); i.hasNext(); ) {
            Element e = i.next();
            String r = e.attr("src");
            Pattern p = Pattern.compile("http://.+\\.(jpg|jpeg)");
            Matcher m = p.matcher(r);
            if (m.matches()) {
                result.add(r);
            }
        }
        return result;
    }
}
        
        
      