package com.pku.edu;

import java.util.ArrayList;
import java.util.HashMap;

public class SpiderFirst implements Runnable {

    private ArrayList urls; //URL列表

private HashMap indexedURLs; //已经检索过的URL列表

private int threads ; //初始化线程数    

private ArrayList threadList;

private Object logger;



public void Spider(String strURL) {

       urls    = new ArrayList();

    threads = 10;

    urls.add(strURL);

    threadList = new ArrayList();

    indexedURLs = new HashMap();



    if (urls.size() == 0)

        throw new IllegalArgumentException("Missing required argument: -u [start url]");

    if (threads < 1)

        throw new IllegalArgumentException("Invalid number of threads: " +

            threads);

}

public static void main(String argv[]) throws Exception {

     if(argv[0] == null){

       System.out.println("Missing required argument: [Sit URL]");

       return ;

     }

            SpiderFirst Spider = new SpiderFirst();

            Spider.go();

}
public void go() throws Exception {


    long start = System.currentTimeMillis();

    for (int i = 0; i < threads; i++) {

        Thread t = new Thread(this, "Spide " + (i+1));

        t.start();

        threadList.add(t);

    }

    while (threadList.size() > 0) {

        Thread child = (Thread)threadList.remove(0);

        child.join();

    }

    long elapsed = System.currentTimeMillis() - start;

}

public void run() {

    String url;

    try {

        while ((url = dequeueURL()) != null) {

            indexURL(url);

        }

    }catch(Exception e) {


    }        

}

//检测URL列表容器中有没有URL没有被解析,如果有则返回URL由线程继续执行



public synchronized String dequeueURL() throws Exception {

    while (true) {

        if (urls.size() > 0) {

            return (String)urls.remove(0);

        }else {

            threads--;

            if (threads > 0) {

                wait();

                threads++;

            }else {

                notifyAll();

                return null;

            }

        }

    }

}

/*

 * 添加URL和当前URL的级数，并唤醒睡眠线程     

 */

public synchronized void enqueueURL(String[] urlGroups,int level) {

    if (indexedURLs.get(urlGroups) == null) {

        urls.add(urlGroups);

        indexedURLs.put(urlGroups, new Integer(level));

        notifyAll();

    }

}

/**

 * 通过URL解析出网页内容并解析出页面上的URL

 * @param url 页面链接

 * @throws java.lang.Exception

 */

private void indexURL(String url) throws Exception {

    boolean flag = true ;

   //判断网页链接的级别，系统默认为三级

    int level = 1 ;

    if (indexedURLs.get(url) == null) {

       indexedURLs.put(url, new Integer(level));

    }else{

       level = ((Integer)indexedURLs.get(url)).intValue();

       //只检测到页面的第二级

       if(level > 2 )

         return ;

       level++ ;

    }



    String strBody = null ;

    try{

            //解析页面内容

            strBody = loadURL(url);

    }catch(Exception e){

            return ;

    }

    if (strBody != null) {

      String urlGroups[] = null ;

      try{

              //解析出页面所以URL

              Object summary = null;
			urlGroups = parseURLs(summary);

      }catch(Exception e){

          
      }

      if(urlGroups == null)

              urlGroups = new String[0] ;

              

      strBody = null ;

      for (int i = 0; i < urlGroups.length; i++) {

            enqueueURL(urlGroups,level);

      }

    }

}

private String loadURL(String url) {
	
	return null;
}
private String[] parseURLs(Object summary) {
	
	return null;
}



}