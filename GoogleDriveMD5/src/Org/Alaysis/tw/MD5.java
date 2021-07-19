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
		//�إ� stringLine
		String stringLine;
		//Ū���ɮ�
		File file = new File("report/5769.R720.ER");
		//�g�X�ɮ�
		File outfile = new File("report/test.txt") ;
        FileWriter fw = new FileWriter (outfile);
        //�g�JŪ�����e

        try (BufferedReader br = new BufferedReader( new FileReader(file))) {	
        	while ((stringLine=br.readLine())!=null) {
        	//	   System.out.println(stringLine);
        	//�i����MD5
        		int beginIndex = 0;
        		beginIndex = stringLine.indexOf("MD5 =");
        		//		System.out.println(beginIndex);
        		//���MD5�����G�æC�L�X��
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