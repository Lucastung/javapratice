package org.ganalyst.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class RamanSample {
	File testFile;
	File controlFile;
	String Name;
	String[] tData;
	String[] cData;
	//new RamanSample(); 
	public RamanSample(File _Test, File _Control) throws IOException {
		testFile = _Test;
		controlFile=_Control;
		//Load data for file
		Name = _Test.getName().replace(".txt", "");
		tData = new String(Files.readAllBytes(_Test.toPath())).split("\n");
		cData = new String(Files.readAllBytes(_Control.toPath())).split("\n");
		
		
	}
	public String getName() {
		return Name;
	}

	// i = 1 =>control sample
	// i = 0 =>test sample
	public double getWave(double w1, int i) {
		String[] myData = null;
		if(i==0) {
			myData = tData;
		} else if (i == 1) {
			myData = cData;
		} else {
			System.out.println("Error!");
			System.exit(-1);
		}		
		//get value
		//  wb...w1...wf
		
		
		for(int j =0;j<myData.length;j++) {
			double wf = Double.valueOf(myData[j].substring(0, 9));
			if(wf>w1) {
				double wb = Double.valueOf(myData[j-1].substring(0, 9));
				if( (wf-w1) > (w1-wb)) {
					// wb is closer
					return Double.valueOf(myData[j-1].substring(11));
				} else {
					return Double.valueOf(myData[j].substring(11));
				}
			}
			
			
		}
		return 0;
	}	
	
	public double getIndex(int _index, int i) {
		String[] myData = null;
		if(i==0) {
			myData = cData;
		} else if (i == 1) {
			myData = tData;
		} else {
			System.out.println("Error!");
			System.exit(-1);
		}
				
		if(_index > myData.length) {
			return -1;
		}		
		String[] values = myData[_index].split("\t");
		return Double.valueOf(values[1]) ;
	}
	

}
