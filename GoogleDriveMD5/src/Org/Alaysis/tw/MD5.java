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
		//�إ� stringLine
		String stringLine;
		//Ū���ɮ�
		File file = new File("report/5769.R720.ER");
		//�g�X�ɮ�
   File outfile = new File("report/test.txt") ;
        FileWriter fw = new FileWriter (outfile);
        //�g�JŪ�����e
  BufferedWriter bw = new BufferedWriter(fw);
//  System.out.print(bw);

  InputStreamReader reader = new InputStreamReader(
    new FileInputStream(file));
  try (BufferedReader br = new BufferedReader(reader)) {
	
	  while ((stringLine=br.readLine())!=null) {
//	   System.out.println(stringLine);
		//�i����MD5
		int len = stringLine.length();
		int beginIndex = 0;
		beginIndex = stringLine.indexOf("MD5 =");
//		System.out.println(beginIndex);
		//���MD5�����G�æC�L�X��
		if(beginIndex != -1) {
			
		String [] MD5data = stringLine.split(":");
		
//		System.out.println(MD5data[3]+MD5data[4]);
		fw.write(MD5data[3]+MD5data[4]);
        fw.flush();
        fw.close();

		
							}

		
//�������R��row����p�G��MD5 = ��i������᭱����r ���x�s�bARRAY��
//		System.out.println(stringLine.substring(beginIndex,endIndex));
		//���������ƨé�JARRAYLIST��
//		ArrayList <String>array = new ArrayList<String>();
//		array.add(stringLine);
		//�ÿ�X���ɮפ�
	
}  
	} 
			

	}

	}