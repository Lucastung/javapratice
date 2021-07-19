package Org.Alaysis.tw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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

        try (BufferedReader br = new BufferedReader( new FileReader(file))) {	
        	while ((stringLine=br.readLine())!=null) {
        	//	   System.out.println(stringLine);
        	//進行抓取MD5
        		int beginIndex = 0;
        		beginIndex = stringLine.indexOf("MD5 =");
        		//		System.out.println(beginIndex);
        		//抓取MD5的結果並列印出來
        		if(beginIndex != -1) {	
        				String [] MD5data = stringLine.split(":");
        				//		System.out.println(MD5data[3]+MD5data[4]);
        				fw.write(MD5data[3]+"\t"+MD5data[4]+"\n");

					}
        		}
			fw.flush();
			fw.close();
        }	 
			

	}

}