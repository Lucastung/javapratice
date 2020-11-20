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

		//Merge file 		
		try {
			String result  = mergeFile(mFiles);
			System.out.println(result);
			Files.write(Paths.get(ftable.get(0)), result.getBytes());
			
		} catch (IOException e) {			
			e.printStackTrace();
			System.out.println("Write file error!");
			System.exit(-1);
		}


	}

	private static String mergeFile(File[][] mFiles) throws IOException {
		
		String  fdata = new String(Files.readAllBytes(mFiles[0][0].toPath()));
		String[] firstdata = fdata.split("\n");
		//file format
		//wlen sample1_T Sample1_C Sample2_T .....
		//800 1.234
		//801 2.3432
		
		String header = "WaveLength";
		String[] data = new String[fdata.length()];
		for(int i = 0;i<firstdata.length;i++) {		
			data[i]=firstdata[i].split("\t")[0];
		}
		//merge file one by one
		for (int i = 0;i<mFiles[0].length;i++) {
			String[] tdata = new String(Files.readAllBytes(mFiles[0][i].toPath())).split("\n");
			String[] cdata = new String(Files.readAllBytes(mFiles[1][i].toPath())).split("\n");
			//Sample1.txt => Sample1_T
			header  += "\t"+mFiles[0][i].getName().replace(".txt","_T");
			header += "\t"+mFiles[1][i].getName().replace(".txt","_C");
			
			for(int j =0;j < tdata.length;j++) {
				if(tdata[j].equals("")) continue;
				data[j] += "\t"+tdata[j].split("\t")[1].trim(); 
				data[j] += "\t"+cdata[j].split("\t")[1].trim();
			}			
		}
		StringBuilder sb = new StringBuilder();
		sb.append(header);
		for(int i = 0; i <data.length;i++) {
			sb.append("\n"+data[i]);
		}	
		return sb.toString();
	}
}
