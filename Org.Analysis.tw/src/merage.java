import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class merage {
//java -jar readATAT.jar -T read/ -C report/ Outerfile.txt
//建立模板
public static void main(String[] args) throws IOException {
	//尋找取得檔案來源
	String [][]basepair=new String [19984][134];	
	FileReader file =new FileReader("report/1-IToutgene.txt"); 	
	File outfile = new File("report/outall.txt") ;
	FileWriter fw = new FileWriter(outfile);
	//以每行方式讀取檔案為br.readLine()
	BufferedReader br = new BufferedReader(file);
	String stringLine = br.readLine();
	  while ((stringLine=br.readLine())!=null) {  
		   //進行設定條件
		   if (!stringLine.startsWith("#")) {
		  System.out.println(stringLine);
		  fw.append(stringLine+"\n");
		            }
}
}
}
//    	//檔案寫入
//			File outfile = new File("report/outall.txt") ;
//			FileWriter fw = new FileWriter (outfile);
//			File files = null ; 
//			files = new File ("./report");
			
//
//
////讀取多組檔案			
//		
//	
//		if (!files.isDirectory()) {
//			System.exit(0);
//		}else {
//			System.out.println("這為TXT的資料夾");
//			String[] filelist=files.list(); //filelist為列出
//	      for (String path:filelist) {
//	    	  System.out.println(files+"/"+path);
//	    	  FileReader infile =new FileReader(files+"/"+path);
//	    	  BufferedReader brs = new BufferedReader(infile);
//	    	  String stringLines = null;
//	    	  while ((stringLines=brs.readLine())!=null) {
//	    		  if (!stringLine.startsWith("#")) {
//	    			  {String s = new String (stringLine);
//	    			  String basepair[]= s.split("\t"); 
//	    			  
//	    			  System.out.println(basepair[3]);
//	    		}
//	    	}
//	    	  }
//	      }
//		}
//}
//}
//}												



 //以每行方式讀取檔案
//	BufferedReader br = new BufferedReader(file);    
//    String stringLine = null;
//    while ((stringLine=br.readLine())!=null) {    	
//  	  if (!stringLine.startsWith("#")) {
//          {String s = new String (stringLine);
//          String basepair[]= s.split("\t"); 
//  	      System.out.println(basepair[4]);
//          }							  
  
   


//          }
//    		     						}
//    										 }
//    														}
//							}

//    	
//public static void array(String files)  { 	
//  //建立array大小並撰寫值
//	    int [][] a = new int [19984][134];
//	    int i , j;
//	    for (i = 1 ; ;i++){
//	     for (j = 1 ; ;j++){										
//	    	 a[i][j]= main;//起始array值  