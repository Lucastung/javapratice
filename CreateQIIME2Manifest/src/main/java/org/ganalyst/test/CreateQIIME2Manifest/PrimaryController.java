package org.ganalyst.test.CreateQIIME2Manifest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;

public class PrimaryController {
	public class sampleFile{		
		private SimpleStringProperty col1;
		private SimpleStringProperty col2;
		private SimpleStringProperty col3;
		
		public sampleFile(String _col1, String _col2, String _col3) {
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

	
	@FXML
	TableView<sampleFile> tv;

    @FXML
    private void switchToSecondary() throws IOException {
    	
    	ObservableList<sampleFile> data = FXCollections.observableArrayList();
    	File f = new File("raw/");
    	if(!f.isDirectory()) {
    		System.exit(-1);
    	}else {
    		System.out.println("這是存放raw files的資料夾");
    		File[] filelist=f.listFiles();
    		String name;
    		String path;
    		String direction;
    		for(int i = 0; i<filelist.length;i++) {
    			name = filelist[i].getName().split("_")[0];
				path = filelist[i].getAbsolutePath();
				if (filelist[i].getName().split("_")[1].endsWith("R1.fastq.gz")) {
					direction = "forword";
				}else {
					direction = "reverse";
				}
				sampleFile newRow = new sampleFile(name,path,direction);
				data.add(newRow);
    		}
    	}
    	
    	tv.setEditable(true);
    	TableColumn<sampleFile,String> firstNameCol = new TableColumn<sampleFile,String>("Column 1");
    	firstNameCol.setCellValueFactory(new PropertyValueFactory("col1"));
    	firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	firstNameCol.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFile, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFile, String> t) {
        				((sampleFile) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol1(t.getNewValue());
        			}
        			
        		}
        		);
    	TableColumn<sampleFile,String> secondNameCol = new TableColumn<sampleFile,String>("absolute-filepath");
    	secondNameCol.setCellValueFactory(new PropertyValueFactory("col2"));
    	secondNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	secondNameCol.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFile, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFile, String> t) {
        				((sampleFile) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol2(t.getNewValue());
        			}        			
        		}
        		);
    	TableColumn<sampleFile,String> thirdNameCol = new TableColumn<sampleFile,String>("direction");
    	thirdNameCol.setCellValueFactory(new PropertyValueFactory("col3"));
    	thirdNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
    	thirdNameCol.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFile, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFile, String> t) {
        				((sampleFile) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol3(t.getNewValue());
        			}        			
        		}
        		);
    	

		tv.getItems().addAll(data);
    	tv.getColumns().addAll(firstNameCol,secondNameCol,thirdNameCol);
    	

	
    }
    
    @FXML
    private void WriteTable(ActionEvent e) {
    	//Lucas: set file name for saving.
    	FileChooser fileChooser0 = new FileChooser();
    	fileChooser0.setTitle("Save as");
    	File selectFile  = fileChooser0.showSaveDialog(null);
    	if(selectFile == null) return;
    	File txtfile = selectFile;
    	
    	/*
    	BufferedWriter bf = new BufferedWriter(new FileWriter(txtfile));
    	bf.append("col1\tcol2\tcol3");
    	
    	ObservableList<sampleFile> data = tv.getItems();
    	
    	for (sampleFile sf :data) {
    		
    		bf.append("\n"+sf.getCol1()+"\t");
    		
    	}
    	bf.flush();
    	bf.close();
		*/
    	
    }
    
    
}
