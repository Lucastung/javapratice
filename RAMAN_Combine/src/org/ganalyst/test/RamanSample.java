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
	float getTestIndex(int _index) {
		
		if(_index>tData.length) {
			return -1;
		}		
		String[] values = tData[_index].split("\t");
		return Float.valueOf(values[1]) ;
	}
	float getControlIndex(int _index) {
		if(_index>cData.length) {
			return -1;
		}				
		String[] values = cData[_index].split("\t");
		return Float.valueOf(values[1]) ;
	}
	public String getName() {
		return Name;
	}
	public float getWave(int i) {
		// TODO Auto-generated method stub
		return 0;
	}	
	

}
