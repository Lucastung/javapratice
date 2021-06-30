package org.gapp.hsujc.rna.RnaseqParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class FileProc {
	
	public void Merge_RowID(String _output, String _rowidcols, String _datacols, ArrayList<String> _src) throws Exception {
		//get row id cols
		String[] rids = _rowidcols.split(",");
		//get row id cols
		String[] dids = _datacols.split(",");
		//file name
		String filelist ="";
		//data depos
		int sampleNumber = _src.size();
		int datasize = dids.length;
		Hashtable<String,String[]> join = new Hashtable<String,String[]>();
		//join.put("DataColumn##", dids);
		//sample number
		for(int fi =0;fi <_src.size();fi++) {
			String[] finfo= _src.get(fi).split("\t");
			File f = new File(finfo[1]);
			filelist += "\t"+finfo[0];
			System.out.println("Append "+finfo[1]);
			BufferedReader br = new BufferedReader(new FileReader(f));
			//get column of row id and data
			String line=br.readLine();
			String[] cols = line.split("\t");
			int[] didi = new int[dids.length];
			int[] ridi = new int[rids.length];
			for(int i=0;i<dids.length;i++) {
				int pos = -1;
				for(int j=0;j<cols.length;j++) {
					if(dids[i].equals(cols[j])) pos=j;
				}
				if(pos==-1) {
					br.close();
					throw new Exception(dids[i] +" is not found in "+ f.getName());
				}
				didi[i]=pos;
			}
			for(int i=0;i<rids.length;i++) {
				int pos = -1;
				for(int j=0;j<cols.length;j++) {
					if(rids[i].equals(cols[j])) pos=j;
				}
				if(pos==-1) {
					br.close();
					throw new Exception(rids[i] +" is not found in "+ f.getName());
				}
				ridi[i]=pos;
			}
			//import data
			while (br.ready()) {
				String[] data =br.readLine().split("\t");
				String hashid ="";
				for(int i=0;i<ridi.length;i++) {
					hashid += data[ridi[i]]+"\t";					
				}				
				if(!join.containsKey(hashid)) {
					join.put(hashid, new String[sampleNumber*datasize]);
				}
				String[] dataline = join.get(hashid);
				for(int i=0;i<datasize;i++) {
					dataline[fi*datasize+i]=data[didi[i]];
				}
			}
			br.close();					
		}
		//output file
		
		for(int ei = 0;ei<dids.length;ei++) {
			BufferedWriter bw = new BufferedWriter(new FileWriter(_output+"_"+dids[ei]+".txt"));
			bw.write(_rowidcols.replace(",","\t")+filelist);
			for(String k:join.keySet()) {
				bw.write("\n"+ k.trim());
				String[] dataline = join.get(k);
				for(int ji=ei;ji<sampleNumber*datasize;ji+=datasize) {
					bw.write("\t"+dataline[ji]);
				}
			}
			bw.flush();
			bw.close();
			System.out.println("Export "+ _output+"_"+dids[ei]+".txt");
		}
	}
	
	//Merge by first column id	
	public void Merge_TCGA(String _output, String _fsrc, String _tags) throws IOException {		
		//Get file list
		String[] _sFiles = (new String(Files.readAllBytes(Paths.get(_fsrc)), StandardCharsets.UTF_8)).split("\n");
		List<File> _src = new ArrayList<File>();
		for(String sFile:_sFiles) {
			_src.add(new File(sFile));	
		}
		//Get Tag list
		String[] _sTags = (new String(Files.readAllBytes(Paths.get(_tags)), StandardCharsets.UTF_8)).split("\n");
		Hashtable<String,String> tags = new Hashtable<String,String>();
		for(String sTag:_sTags) {
			String[] ssTag = sTag.split("\t");
			tags.put(ssTag[0],ssTag[1]);	
		}		
		

		String filelist ="";
		//data depos
		int sampleNumber = _src.size();
		//Rowid table
		Hashtable<String,String[]> join = new Hashtable<String,String[]>();
		for(int fi =0;fi <_src.size();fi++) {
			File f = _src.get(fi);
			filelist += "\t"+tags.get(f.getName());
			System.out.println("Append "+ f.getName());
			BufferedReader br = new BufferedReader(new FileReader(f));
			//get column of row id and data
			String line=br.readLine();
			//import data
			while (br.ready()) {				
				String[] data =br.readLine().split("\t");
				if(data[0].startsWith("__"))continue;
				if(!join.containsKey(data[0])) {
					join.put(data[0], new String[sampleNumber]);
				}
				String[] dataline = join.get(data[0]);
				dataline[fi]=data[1];
			}
			br.close();					
		}
		//output file
		BufferedWriter bw = new BufferedWriter(new FileWriter(_output+".txt"));
		bw.write("gene_id"+filelist);
		for(String k:join.keySet()) {
			bw.write("\n"+ k.trim());
			String[] dataline = join.get(k);
			for(int ji=0;ji<sampleNumber;ji++) {
				bw.write("\t"+dataline[ji]);
			}
		}
		bw.flush();
		bw.close();
		System.out.println("Export "+ _output);
	}
}
