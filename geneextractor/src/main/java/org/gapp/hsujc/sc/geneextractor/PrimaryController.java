package org.gapp.hsujc.sc.geneextractor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.zip.GZIPInputStream;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;

public class PrimaryController implements Initializable {
	@FXML TextArea textArea = new TextArea();
	@FXML Button LoadFile = new Button();	
	@FXML TextArea textAreadata = new TextArea();
	
	int genes = 0;
	int barcode = 0 ;
	String[][] matrix = null;
	String[] geneList = null;
	
	@Override
    public void initialize(URL location, ResourceBundle resources) {
		textArea.appendText("Please select 10x dataset from \"LoadFile\"."+"\n");
		textAreadata.setEditable(false);
	}
	
	@FXML
    private void loadFile(ActionEvent e) {
		textArea.clear();
		textArea.appendText("Please select 10x dataset from \"LoadFile\"."+"\n");
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		//alert.setHeaderText("File(s) already exist");
		String s = "Load Data may take few minutes.";
		alert.setContentText(s);		 
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			List<File> fileList = null;
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Choose 10X SC Result");
			fileChooser.getExtensionFilters().addAll(
	                new FileChooser.ExtensionFilter("gz", "*.gz"),
	                new FileChooser.ExtensionFilter("All files", "*.*")
	            );		
			/* Show open file dialog to select multiple files. */
			fileList = fileChooser.showOpenMultipleDialog(null);
			if (fileList == null) {
				System.out.println("no folder selected!!");
		    	return;
			}
			//check file barcodes.tsv.gz features.tsv.gz  matrix.mtx.gz
			File barcodestsvgz = null;
			File featurestsvgz = null;
			File matrixmtxgz = null;
			for(File f : fileList) {
				if(f.getName().equals("barcodes.tsv.gz")) {
					barcodestsvgz = f.getAbsoluteFile();
				}
				if(f.getName().equals("features.tsv.gz")) {
					featurestsvgz = f.getAbsoluteFile();
				}
				if(f.getName().equals("matrix.mtx.gz")) {
					matrixmtxgz = f.getAbsoluteFile();
				}
			}
			if(barcodestsvgz == null) {
				System.out.println("barcodes.tsv.gz is not exist!");
				textArea.appendText("barcodes.tsv.gz is not exist!"+"\n");
				return;
			}
			if(featurestsvgz == null) {
				System.out.println("features.tsv.gz is not exist!");
				textArea.appendText("features.tsv.gz is not exist!"+"\n");
				return;
			}
			if(matrixmtxgz == null) {
				System.out.println("matrix.mtx.gz is not exist!");
				textArea.appendText("matrix.mtx.gz is not exist!"+"\n");
				return;
			}
			//read data to data
			try {
				BufferedReader brm = new BufferedReader(new InputStreamReader(
				         new GZIPInputStream(new FileInputStream(matrixmtxgz.getAbsolutePath()))));
				BufferedReader brf = new BufferedReader(new InputStreamReader(
				         new GZIPInputStream(new FileInputStream(featurestsvgz.getAbsolutePath()))));
				BufferedReader brb = new BufferedReader(new InputStreamReader(
				         new GZIPInputStream(new FileInputStream(barcodestsvgz.getAbsolutePath()))));
				String line = brm.readLine();
				while (line.startsWith("%"))line = brm.readLine();
			    String[] dim = line.split(" ");
			    genes = Integer.valueOf(dim[0]);
			    barcode = Integer.valueOf(dim[1]);
			    matrix = new String[genes+1][barcode+1];
			    textArea.appendText("Dataset include "+barcode+" barcodes, "+genes+" genes."+"\n");
			    //set default gene value = 0
			    for(int i = 1; i< genes+1; i ++) {
			    	for(int j = 1; j < barcode+1; j ++) {
			    		matrix[i][j] = "0";
			    	}
			    }
			    //get gene name
			    while(brf.ready()) {
			    	for(int i = 1; i < genes+1; i ++) {
			    		String[] sa = brf.readLine().split("\t");
			    		//System.out.println(sa[0]);
			    		//System.out.println(sa[1]);
			    		matrix[i][0] = sa[0] + " " + sa[1];
			    	}
			    	/*test data
			    	for(int i = 0; i<20;i++) {
			    		System.out.println(matrix[i][0]);	
			    	}
			    	*/
			    }
			    //get barcode 6711
			    String header = "Gene";
			    while(brb.ready()) {
			    	header += "\t" + brb.readLine();
			    }
			    	for(int i = 0 ; i < header.split("\t").length ; i ++) {
			    	//System.out.println(header.split("\t")[i]);
			    	matrix[0][i] = header.split("\t")[i];
			    }
			    	/*test data
			    	for(int i = 0; i<20;i++) {
			    		System.out.println(matrix[0][i]);	
			    	}
			    	*/
			    	
				//for(int i = 0)
			    	int count = 0;
			    	while(brm.ready()) {
			    		String[] vs = brm.readLine().split(" ");
			    		matrix[Integer.valueOf(vs[0])][Integer.valueOf(vs[1])]=vs[2];
			    		if((count++ % 1000000)==0) {
			    			System.out.print("+");
			    			textArea.appendText("+");
			    		}
			    	}
			    	/*test data
			    	for(int i = 0; i<200;i++) {
			    		System.out.println(matrix[100][i]);	
			    	}
			    	*/
			    	textArea.appendText("\n"+"Data upload finish.");
			    	textArea.appendText("\n"+"Please select genelist file, gene name line by line.");
					/*
			    	textArea.appendText("\n"+"UBE2J2");
					textArea.appendText("\n"+"ALB");
					textArea.appendText("\n"+"APOE");
					textArea.appendText("\n"+".....");
					textArea.appendText("\n"+"...");
					*/
					textArea.appendText("\n"+"Or juest past in blow:"+"\n");
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} 

		else
		{
		    return;
		}
	}
	
	@FXML
    private void loadGenelist(ActionEvent e) {
		if(matrix != null) {			
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Choose GeneList file");
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("txt", "*.txt"),
                new FileChooser.ExtensionFilter("All files", "*.*")
            );		
		/* Show open file dialog to select multiple files. */
		File file = null;	
		/* Show open file dialog to select multiple files. */
		file = fileChooser.showOpenDialog(null);
		if (file == null) {
			System.out.println("no GeneList file selected!!");
	    	return;
		}
		
		
			try {
				geneList = (new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())))).split("\n");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			for(String s : geneList) {
			System.out.println(s);
			textArea.appendText(s+"\n");
			}
		}else {
			System.out.println("Please select 10x dataset first.");
			textArea.appendText("Please select 10x dataset first."+"\n");
			return;
		}
			
	}
	
	@FXML
    private void export(ActionEvent e) {
	
	if(matrix != null) {
		String tv = textArea.getText();
		//textArea.clear();
		//if genelist uploaded
		if(geneList != null) {
			String header = "Gene";
			for(int i = 1 ; i < barcode+1; i ++) {
				header += "\t"+matrix[0][i];
			}
			textAreadata.appendText(header);
			StringBuilder sb = new StringBuilder(header);
			for(String s:geneList) {
				for(int i = 0 ; i < genes+1 ;i++) {
					if(matrix[i][0].endsWith(" "+s.toUpperCase())||matrix[i][0].startsWith(" "+s.toUpperCase())) {
						String da = matrix[i][0];
						for(int j=1;j<barcode+1;j++) {
							da += "\t" + matrix[i][j];
						}
						textAreadata.appendText("\n"+da);
						sb.append("\n"+da);
						continue;
					}else continue;
				}
			}
			FileChooser savefileName = new FileChooser();
			savefileName.setTitle("Save as...");   	
			Date dNow = new Date( );
		    SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd");
			savefileName.setInitialFileName(df.format(dNow)+".csv");
			File selectFile  = savefileName.showSaveDialog(null);
			if(selectFile == null) return;
			File txtfile = selectFile;	
			//
			BufferedWriter bf;
			try {
				bf = new BufferedWriter(new FileWriter(txtfile));
				bf.append(sb.toString());
		    	bf.flush();
		    	bf.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Data Saving");
			//alert.setHeaderText("Information Alert");
			String s = "The data was saved.";
			alert.setContentText(s);
			alert.show();
		}else {
			//use tv for genelist
			String genelis = tv.replace("\n", "\t");
			String[] gl = genelis.split("Or juest past in blow:")[1].split("\t");
			String header = "Gene";
			for(int i = 1 ; i < barcode+1; i ++) {
				header += "\t"+matrix[0][i];
			}
			textAreadata.appendText(header);
			StringBuilder sb = new StringBuilder(header);
			for(String s:gl) {
				for(int i = 0 ; i < genes+1 ;i++) {
					if(matrix[i][0].endsWith(" "+s.toUpperCase())||matrix[i][0].startsWith(" "+s.toUpperCase())) {
						String da = matrix[i][0];
						for(int j=1;j<barcode+1;j++) {
							da += "\t" + matrix[i][j];
						}
						textAreadata.appendText("\n"+da);
						sb.append("\n"+da);
						continue;
					}else continue;
				}
			}
			FileChooser savefileName = new FileChooser();
			savefileName.setTitle("Save as...");   	
			Date dNow = new Date( );
		    SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd");
			savefileName.setInitialFileName(df.format(dNow)+".csv");
			File selectFile  = savefileName.showSaveDialog(null);
			if(selectFile == null) return;
			File txtfile = selectFile;	
			//
			BufferedWriter bf;
			try {
				bf = new BufferedWriter(new FileWriter(txtfile));
				bf.append(sb.toString());
		    	bf.flush();
		    	bf.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				}
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Data Saving");
			//alert.setHeaderText("Information Alert");
			String s = "The data was saved.";
			alert.setContentText(s);
			alert.show();
			
		}
		
		}else {	
		System.out.println("Please select 10x dataset first.");
		textArea.appendText("Please select 10x dataset first."+"\n");
		return;
	}	
	}
	
	@FXML
    private void exportAll(ActionEvent e) {
		if(matrix != null) {
			Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		//alert.setHeaderText("File(s) already exist");
		String s = "Export All data need long long time!!"+"\n"+"This process may take more than 10 mins!!"+"\n"+"Result file size may larger than 400MB";
		alert.setContentText(s);		 
		alert.showAndWait();
		if (alert.getResult() == ButtonType.OK) {
			FileChooser savefileName = new FileChooser();
			savefileName.setTitle("Save as...");   	
			Date dNow = new Date( );
		    SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd");
			savefileName.setInitialFileName(df.format(dNow)+".csv");
			File selectFile  = savefileName.showSaveDialog(null);
			if(selectFile == null) return;
			File txtfile = selectFile;	
			//prepare data
			String header = "Gene";
			for(int i = 1 ; i < barcode+1; i ++) {
				header += "\t"+matrix[0][i];
			}
			StringBuilder sb = new StringBuilder(header);
			for(int i = 1 ; i < genes+1 ;i++) {
				String da = matrix[i][0];
				for(int j=1;j<barcode+1;j++) {
					da += "\t" + matrix[i][j];
				}
				sb.append("\n"+da);
			}			
			//
			BufferedWriter bf;
			try {
				bf = new BufferedWriter(new FileWriter(txtfile));
				bf.append(sb.toString());
		    	bf.flush();
		    	bf.close();
			} catch (IOException e1) {
				e1.printStackTrace();
				}
			Alert alert2 = new Alert(AlertType.INFORMATION);
			alert2.setTitle("Data Saving");
			//alert.setHeaderText("Information Alert");
			String s2 = "The data was saved.";
			alert2.setContentText(s2);
			alert2.show();
		} 
		else
		{
		    return;
		}
		}else {
			System.out.println("Please select 10x dataset first.");
			textArea.appendText("Please select 10x dataset first."+"\n");
			return;
		}
		
	}
}
