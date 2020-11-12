package org.ganalyst.test;

import java.io.File;
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
		File tDir = new File(atable.get("-T"));
		File sDir = new File(atable.get("-S"));
		
		if(!tDir.isDirectory() || !sDir.isDirectory()) {
			System.out.println("Path is not direcotry!");
			System.exit(-1);
		}		
		File[] tFiles = tDir.listFiles();
		File[] sFiles = sDir.listFiles();

		


	}

}
