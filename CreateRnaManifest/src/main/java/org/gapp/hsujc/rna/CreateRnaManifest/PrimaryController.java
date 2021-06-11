package org.gapp.hsujc.rna.CreateRnaManifest;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.DirectoryChooser;

public class PrimaryController {
	@FXML TextArea outResult = new TextArea();
	@FXML Button LoadFile = new Button();
	@FXML TableView<sampleFiles> tv;
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
    @FXML
    private void loadfile(ActionEvent e) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setInitialDirectory(new File("/home/hsujc/eclipse-workspace"));
    directoryChooser.setTitle ("Choose RNAseq Result Folder");
    File folder = directoryChooser.showDialog(null);
    File[] filename;  
    outResult.setWrapText(true);
    ObservableList<sampleFiles> data = FXCollections.observableArrayList(null,null,null);
    
    if (folder != null)
    {
    	filename=folder.listFiles();
    	for (int i = 1; i< folder.listFiles().length; i++) {
    		TableColumn<sampleFiles,String> firstNameCol = new TableColumn<sampleFiles,String>("Sample");
    		data = FXCollections.observableArrayList(
        			//new sampleFiles(columns[0],columns[1],columns[2])
        			new sampleFiles(filename[i].toString().split(folder.getName()+"/")[1], null, null));
    		tv.setEditable(true);
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
            		});
        	tv.getItems().addAll(data);
        	tv.getColumns().addAll(firstNameCol);
    		//System.out.println(filename[i].toString().split(folder.getName()+"/")[1]);
    		outResult.appendText(filename[i].toString().split(folder.getName()+"/")[1]+ "/n");
    		 
    	}
    	
    }else{
    outResult.setText("Folder not exist");
    System.out.println("Folder not exist");
    }
    }
}
