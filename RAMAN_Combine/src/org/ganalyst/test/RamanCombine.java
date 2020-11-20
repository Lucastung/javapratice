package org.ganalyst.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

public class RamanCombine {

	//java -jar RamanFileCombine.jar -T test -C control/ output.txt
	public static void main(String[] args) {
		//JAVA Collection class
		Hashtable <String,String> atable = new Hashtable <String,String>();
		ArrayList <String> ftable = new ArrayList<String>();
		
		for(int i = 0; i<args.length;i++) {
			if(args[i].startsWith("-")) {
				if((i+1)>args.length) {
					System.out.println(args[i]+" is empty!");
					System.exit(-1);
				}
				atable.put(args[i], args[i+1]);
				i++;
			} else {
				ftable.add(args[i]);
			}
		}
		
		//Get file list
		//mFile[0] = tFiles[]
		//mFile[1] = cFiles[]
		File[][] mFiles = new File[2][];
		
		File t =new File(atable.get("-T"));
		File c =new File(atable.get("-C"));

		
		if(!t.isDirectory() || !c.isDirectory()  ) {
			System.out.println("Path is not a directory!");
			System.exit(-1);
		} 
		mFiles[0] = t.listFiles();
		mFiles[1] = c.listFiles();
		//Create samples container in array
		RamanSample[] samplelist = new RamanSample[mFiles[0].length];
		//Create sample one by one and load test and control sample
		for(int i = 0;i<mFiles[0].length;i++) {			
			try {
				samplelist[i] = new RamanSample(mFiles[0][i],mFiles[1][0]);
				
				//Get first value of new sample
				float x = samplelist[i].getTestIndex(10);
				float y = samplelist[i].getControlIndex(11);
				if( x==-1 || y == -1) {
					System.out.println(samplelist[i].getName() + " have null value");
					float z = samplelist[i].getWave(1999);
				} else {
					System.out.print(samplelist[i].getName() + ":");
					System.out.println(x/y);
				}
				

				
			} catch (IOException e) {
				System.out.println(mFiles[0][i].getName() +" fails "+ e.getMessage() );
			}
		}
		
		



	}
	



}
