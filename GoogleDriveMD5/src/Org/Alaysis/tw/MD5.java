package Org.Alaysis.tw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class MD5 {
	public static void main(String[] args) throws IOException {	
		String stringLine;
		File file = new File("report/5769.R720.ER");
        File outfile = new File("report/test.txt") ;
        FileWriter fw = new FileWriter (outfile);
  BufferedWriter bw = new BufferedWriter(fw);
  
  InputStreamReader reader = new InputStreamReader(
    new FileInputStream(file));
  BufferedReader br = new BufferedReader(reader);
  boolean firstLine = true ;
  while ((stringLine=br.readLine())!=null) {
   System.out.println(stringLine);
																}  
	} 
			

	}