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
//�إ߼ҪO
public static void main(String[] args) throws IOException {
	//�M����o�ɮרӷ�
	String [][]basepair=new String [19984][134];	
	FileReader file =new FileReader("report/1-IToutgene.txt"); 	
	File outfile = new File("report/outall.txt") ;
	FileWriter fw = new FileWriter(outfile);
	//�H�C��覡Ū���ɮ׬�br.readLine()
	BufferedReader br = new BufferedReader(file);
	String stringLine = br.readLine();
	  while ((stringLine=br.readLine())!=null) {  
		   //�i��]�w����
		   if (!stringLine.startsWith("#")) {
		  System.out.println(stringLine);
		  fw.append(stringLine+"\n");
		            }
}
}
}
//    	//�ɮ׼g�J
//			File outfile = new File("report/outall.txt") ;
//			FileWriter fw = new FileWriter (outfile);
//			File files = null ; 
//			files = new File ("./report");
			
//
//
////Ū���h���ɮ�			
//		
//	
//		if (!files.isDirectory()) {
//			System.exit(0);
//		}else {
//			System.out.println("�o��TXT����Ƨ�");
//			String[] filelist=files.list(); //filelist���C�X
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



 //�H�C��覡Ū���ɮ�
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
//  //�إ�array�j�p�ü��g��
//	    int [][] a = new int [19984][134];
//	    int i , j;
//	    for (i = 1 ; ;i++){
//	     for (j = 1 ; ;j++){										
//	    	 a[i][j]= main;//�_�larray��  