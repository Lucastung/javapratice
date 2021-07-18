package Org.Alaysis.tw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MD5 {
	public static void main(String[] args) throws IOException {	
		//建立 stringLine
		String stringLine;
		//讀取檔案
		File file = new File("report/5769.R720.ER");
		//寫出檔案
   File outfile = new File("report/test.txt") ;
        FileWriter fw = new FileWriter (outfile);
        //寫入讀取內容
  BufferedWriter bw = new BufferedWriter(fw);
//  System.out.print(bw);

  InputStreamReader reader = new InputStreamReader(
    new FileInputStream(file));
  try (BufferedReader br = new BufferedReader(reader)) {
	
	  while ((stringLine=br.readLine())!=null) {
//	   System.out.println(stringLine);
		//進行抓取MD5
		int len = stringLine.length();
		int beginIndex = 0;
		beginIndex = stringLine.indexOf("MD5 =");
//		System.out.println(beginIndex);
		//抓取MD5的結果並列印出來
		if(beginIndex != -1) {
			
		String [] MD5data = stringLine.split(":");
		
//		System.out.println(MD5data[3]+MD5data[4]);
		fw.write(MD5data[3]+MD5data[4]);
        fw.flush();
        fw.close();

		
							}

		
//取的分析的row抓取如果有MD5 = 後進行取的後面的文字 並儲存在ARRAY中
//		System.out.println(stringLine.substring(beginIndex,endIndex));
		//抓取完的資料並放入ARRAYLIST中
//		ArrayList <String>array = new ArrayList<String>();
//		array.add(stringLine);
		//並輸出至檔案中
	
}  
	} 
			

	}

	}