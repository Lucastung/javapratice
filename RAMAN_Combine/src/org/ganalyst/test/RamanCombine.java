package org.ganalyst.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;

public class RamanCombine {

	//java -jar RamanFileCombine.jar -T test -C control/ -W1 1002 -W2 1013 output.txt
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
		
		double w1 = Double.valueOf(atable.get("-W1"));
		double w2 = Double.valueOf(atable.get("-W2"));

		
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
				samplelist[i] = new RamanSample(mFiles[0][i],mFiles[1][i]);
			} catch (IOException e) {
				System.out.println(mFiles[0][i].getName() +" fails "+ e.getMessage() );
			}
		}
		//generate report
		// Sample testW1 testW2 testRatio controlW1 controlW2 controlRatio
		StringBuilder sb = new StringBuilder("Sample\tTW1\tTW2\tTR\tCW1\tCW2\tCR");
		for (RamanSample r : samplelist) {
			 double tw1 = r.getWave(w1,0);
			 double cw1 = r.getWave(w1,1);
			 double ratio1 = tw1/cw1;
			 double tw2 = r.getWave(w2,0);
			 double cw2 = r.getWave(w2,1);
			 double ratio2 = tw2/cw2;
			 sb.append("\n"+r.getName()+"\t"+tw1+"\t"+tw2+"\t"+ratio1+"\t"+cw1+"\t"+cw2+"\t"+ratio2);
		}
		System.out.println(sb.toString());



	}
	



}
