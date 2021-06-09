package org.ganalyst.test.CreateQIIME2Manifest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class PrimaryController {
	public class sampleFiles{
		
		private SimpleStringProperty col1;
		private SimpleStringProperty col2;
		private SimpleStringProperty col3;
		
		public sampleFiles(String _col1, String _col2, String _col3) {
			this.col1 = new SimpleStringProperty(_col1);
			this.col2 = new SimpleStringProperty(_col2);
			this.col3 = new SimpleStringProperty(_col3);
			
		}
		public void setCol1(String _col1) {
			col1.set(_col1);
		}
		public String getCol1() {
			return col1.get();
		}
		public void setCol2(String _col2) {
			col2.set(_col2);
		}
		public String getCol2() {
			return col2.get();
		}
		public void setCol3(String _col3) {
			col3.set(_col3);
		}
		public String getCol3() {
			return col3.get();
		}
	}
	
public static void main(String[] args)throws Exception {
		
		Hashtable <String,String> vcftable = new Hashtable <String,String>();
		ArrayList <String> ftable = new ArrayList <String>();
		
		for(int i = 0; i<args.length;i++) {
			if(args[i].startsWith("-")) {
				if((i+1)>args.length) {
					System.out.println(args[i] + " is empty!");
					System.exit(-1);
				}
				vcftable.put(args[i], args[i+1]);
				i++;
			}else {
				ftable.add(args[i]);
			}
			
		}
		readAllFile(vcftable.get("-i"));
	}
	
	public static void readAllFile(String fq) {		
		File f = new File(fq);
		if(!f.isDirectory()) {
			System.exit(-1);
		}else {
			System.out.println("這是存放raw files的資料夾");
			String[] filelist=f.list();
			for(int i = 0; i<filelist.length;i++) {
				usingBufferedReader(filelist[i]);
			//	System.out.println(filelist[i]);
			}
		}
		
	}
	
	private static String usingBufferedReader(String Multifq) {
		StringBuilder contentBuilder = new StringBuilder();
		try (BufferedReader br = new BufferedReader(new FileReader("raw/"+ Multifq))) {
			System.out.println(Multifq.getBytes()[0]);     //Can't get name of fastq.gz 
			String sCurrentLine = br.getClass().toString();
			System.out.println(sCurrentLine);
			String[] columns;
			if(sCurrentLine.endsWith(".fastq.gz") ) {
				columns = sCurrentLine.split("_");
			}
			
			while ((sCurrentLine = br.readLine()) != null) {
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return contentBuilder.toString();
	}
	
	@FXML
	TableView<sampleFiles> tv;

    @FXML
    private void switchToSecondary() throws IOException {
    	
    	ObservableList<sampleFiles> data = FXCollections.observableArrayList(
    			//new sampleFiles(columns[0],columns[1],columns[2])
    			new sampleFiles("A1","B1","C1"),new sampleFiles("A2","B2","C2"),new sampleFiles("A3","B3","C3"));
    	
    	tv.setEditable(true);
    	TableColumn<sampleFiles,String> firstNameCol = new TableColumn<sampleFiles,String>("Column 1");
    	firstNameCol.setCellValueFactory(new PropertyValueFactory("col1"));
    	firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	firstNameCol.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFiles, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFiles, String> t) {
        				((sampleFiles) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol1(t.getNewValue());
        			}
        			
        		}
        		);
    	
    	tv.getItems().addAll(data);
    	tv.getColumns().addAll(firstNameCol);
    	
    	
    	
    }
}
