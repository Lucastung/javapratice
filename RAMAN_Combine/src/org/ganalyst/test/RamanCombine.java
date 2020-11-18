package org.ganalyst.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Hashtable;

public class RamanCombine {

	public static void main(String[] args) {
		//Create a dictionary for arg lookup parameter (lead with "-" )
		Hashtable <String,String> atable = new Hashtable<String,String>();
		//Create a list array for filename (the rest string)
		ArrayList<String> flist = new ArrayList<String>();
		
		//****** Parsing args
		for(int i=0;i<args.length;i++) { //<====for loop start
			//if arg start with - , add the next arg into  atable 
			if(args[i].startsWith("-")) {
				//Prevent out of array boundary
				if(i+1 >= args.length) {
					System.out.println(args[i]+" is empty!");
					System.exit(-1);
				}
				// add arg[i+1] and tag arg[i] into atable
				atable.put(args[i],args[i+1]);
				// add i to skip args[i+1]
				i++;
			} else {
				//those not leaded with "-" are added into flist
				flist.add(args[i]);
			}
		}                              //<===== for loop end
		
		//******* Get file list
		File[][] mFiles = new File[2][];
		mFiles[0] = new File(atable.get("-T")).listFiles();
		mFiles[1] = new File[mFiles[0].length];
		for (int i=0 ;i< mFiles[0].length;i++) {
			
			File f1 = new File(atable.get("-C")+mFiles[0][i].getName());
			if(f1.exists()) {
				mFiles[1][i]=f1;
			} else {
				System.out.println(mFiles[0][i].getName() +" is not paired!");
				System.exit(-1);				
			}
			
		}
		
		//******* Merge file list
		try {
			String result = fileMerge (mFiles);
			System.out.println(result);
			//Files.write(Paths.get(flist.get(0)), result.getBytes());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


	}

	private static String fileMerge(File[][] mFiles) throws IOException {
		// create container for result by using first file
		String[] firstdata = new String(Files.readAllBytes(mFiles[0][0].toPath())).split("\n");
		
		String header = "WaveLength";
		String[] data=new String[firstdata.length];
		for(int i=0; i<firstdata.length;i++) {
			data[i] = firstdata[i];
		}
		
		//read and merged file
		for (int i=0 ;i< mFiles[0].length;i++) {
			String[] tdata = new String(Files.readAllBytes(mFiles[0][i].toPath())).split("\n");
			String[] cdata = new String(Files.readAllBytes(mFiles[1][i].toPath())).split("\n");
			//check section

			//add sample name in header
			header += "\t" + mFiles[0][i].getName().replaceAll(".txt", "_T")+"\t"+ mFiles[0][i].getName().replaceAll(".txt", "_C");
			//add sample value to data container
			for(int j=0;j<tdata.length;j++) {
				data[j] = data[j]+ "\t" + tdata[j].split("\t")[1].trim()+"\t"+cdata[j].split("\t")[1].trim();
			}
		}
		// combine result
		StringBuilder sb = new StringBuilder(header+"\n");
		for (int i=0;i<data.length;i++) {
			sb.append("\n"+data[i]);
		}
		
		return sb.toString();
	}

}
