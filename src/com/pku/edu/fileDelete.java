package pku.eu;

import java.io.File;
import java.io.FileInputStream;

public class fileDel {

	/**
	 * @param args
	 * @throws Exception 
	 */
	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		fileDel f=new fileDel();
	       File dir=new File("data");
	        if(dir.isDirectory()){
	            File[] tmp=dir.listFiles();
	            for (int i = 0; i < tmp.length; i++) {
	            	if (f.getFileSizes(tmp[i])==0) {
						tmp[i].delete();
					}
				}
	}
	}
	*/
	
	
	 public long getFileSizes(File f) throws Exception{//取得文件大小
	        long s=0;
	        if (f.exists()) {
	            FileInputStream fis = null;
	            fis = new FileInputStream(f);
	           s= fis.available();
	        } else {
	            f.createNewFile();
	            System.out.println("文件不存在");
	        }
	        return s;
	    }
}



