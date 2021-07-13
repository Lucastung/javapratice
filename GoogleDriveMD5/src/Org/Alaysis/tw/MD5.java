package Org.Alaysis.tw;
import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class MD5 {
	public static void main(String[] args) throws IOException {	
		String stringLine;
		int num =0;
		List<String>array = new ArrayList<String>();
		File file = new File("report/5769.R720.ER");
		InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
		BufferedReader brfferedreader = new BufferedReader(reader);
		while(brfferedreader != null) {
			num = (brfferedreader.indexOf("MD5 =");
					
										}
		System.out.print(num);

																}  
			

	}
