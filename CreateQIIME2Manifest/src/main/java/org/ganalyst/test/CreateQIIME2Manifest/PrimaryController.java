package org.ganalyst.test.CreateQIIME2Manifest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {
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
	TableView<sampleFile> tableManifest;
	
	@FXML
	TableView<Map<String,String>> tableMetadata;
	ObservableList<sampleFile> data = FXCollections.observableArrayList();
	ObservableList<Map<String,String>> data2 = FXCollections.observableArrayList();

    @FXML
    private void loadFile() throws IOException {
    	ObservableList<sampleFile> data = tableManifest.getItems();
    	String warning = " ";
    	String name;
		String path;
		String direction;
		
    	FileChooser fc = new FileChooser();
    	fc.setInitialDirectory( new File("."));
    	List<File> filelist = fc.showOpenMultipleDialog(null);
    	if(filelist==null || filelist.size()==0) {
    		//Lucas: Never use System.exit(-1) in GUI program, exit gracefully.
    		//Lucas: Show alert and return
    		return;
    	}
    	
    	//File[] filelist=f.listFiles();
    	//Doing manifest file
		for(File f : filelist) {
			name = f.getName().split("_")[0];
			path = f.getAbsolutePath();
			if (f.getName().split("_")[1].endsWith("R1.fastq.gz")) {
				direction = "forword";
			}else {
				direction = "reverse";
			}
			sampleFile newRow = new sampleFile(name,path,direction);
			//以for loop check file name/path have repeat
			boolean haveSamplepath = false;
			for (sampleFile sf : data) {
				if (sf.getCol2().equals(path)) {
					warning += path + ",";
					haveSamplepath = true;
					continue;
				}
			}
			if(haveSamplepath) continue;				
			data.add(newRow);
		}
					
		//Create Hashset to put #SampleID in metadata table
    	HashSet<String> hs = new HashSet<>();
    	for (sampleFile sn :tableManifest.getItems()) {
    		hs.add(sn.getCol1());
    	}
    	
    	//Lucas: get current column name of metadata table
    	ObservableList<TableColumn<Map<String, String>, ?>> tcs = tableMetadata.getColumns();
    	List<String> htc = new ArrayList<String>(); 
    	for (TableColumn tc :tcs) {
    		htc.add(tc.getText());
    	}

    	//Lucas: Add sample with sample name hashset
    	for (String id : hs) {	    		
	    	//Lucas: Check redundant
	    	boolean haveSample = false;
	    	for (Map<String,String> m : tableMetadata.getItems()) {
    			if(m.get("#SampleID").equals(id)) {
    				haveSample =true;
    				break;
    			}
	    	}
	    	
    		if(haveSample) {
    			//Lucas: pass exist sample
    			continue;
    		} else {    			
    			//Lucas: create new sample item
    			HashMap<String, String> d3 = new HashMap<String, String>();
    			//Lucas: add exist table column value
    			for(String cname :htc) {
    				d3.put(cname, "NA");
    			}
    			//Lucas: replace sampleID column with id
				d3.put("#SampleID", id.trim());
				tableMetadata.getItems().add(d3);
				tableMetadata.getSortOrder().addAll(tcs.get(0));
    		}
    	}
        tableMetadata.sort();
        if(warning != " ") {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Duplicated !!");
			String s = "The sample under this path already exists";
			alert.setContentText(s);
			alert.show();   			
    	}
    }
    
    @FXML
    private void WriteTable(ActionEvent e) {
    	//Lucas: set file name for saving.
    	FileChooser savefileName = new FileChooser();
    	savefileName.setTitle("Save as...");
    	
    	Date dNow = new Date( );
        SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd");
    	savefileName.setInitialFileName(df.format(dNow)+"_manifest.txt");
    	File selectFile  = savefileName.showSaveDialog(null);
    	if(selectFile == null) return;
    	File txtfile = selectFile;
    	
    	BufferedWriter bf;
		try {
			bf = new BufferedWriter(new FileWriter(txtfile));
			bf.append("sample-id"+","+"absolute-filepath"+","+"direction");

	    	ObservableList<sampleFile> Manifestdata = tableManifest.getItems();
	    	
	    	for (sampleFile sf : Manifestdata) {	
	    		bf.append("\n"+sf.getCol1()+","+sf.getCol2()+","+sf.getCol3());
	    		
	    	}
	    	bf.flush();
	    	bf.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Export Metadata file
		FileChooser saveMeatafile = new FileChooser();
		saveMeatafile.setTitle("Save as...");
		saveMeatafile.setInitialFileName(df.format(dNow)+"_metadata.tsv");
    	File selectFile2  = saveMeatafile.showSaveDialog(null);
    	if(selectFile2 == null) return;
    	File tsvfile = selectFile2;
    	BufferedWriter bw;
    	try {
    		StringBuilder sb = new StringBuilder(); 
			
			//Lucas: prepare column header and type			
			tableMetadata.getColumns();
			String headerline = "";
			String typeline ="";
			//Lucas: append column header, type one by one
			for(TableColumn t:tableMetadata.getColumns()) {
				//Lucas: combine header
				String colname = t.getText();
				headerline += colname+"\t";
				//Lucas: combine type 
				typeline +=coltype.get(colname)+"\t";
			}
			sb.append(headerline.trim()+"\n"+typeline.trim());
			//Lucas: append data row one by one			
			ObservableList<Map<String,String>> Metadata = tableMetadata.getItems();
			//Lucas: Define key set to index value
			String[] hkeys = headerline.trim().split("\t");
			String[] tkeys = typeline.trim().split("\t");
			for (Map<String,String> mf : Metadata) {
				String dataline ="";
				for(int i=1; i<hkeys.length ; i++ ) {
					//Prevent the cell value does not match the type
					if (tkeys[i].equals("Category")) {
						dataline += mf.get(hkeys[i])+"\t";
					}else if (tkeys[i].equals("Numeric") && mf.get(hkeys[i]).matches("-?\\d+(\\.\\d+)?")) {
						dataline += mf.get(hkeys[i])+"\t";
					}else  {
						Alert alert = new Alert(AlertType.WARNING);
	    				alert.setTitle("Warning Dialog");
	    				alert.setContentText("The cell value does not match the type!!");
	    				alert.showAndWait();
	    				return;
					}
				}	    		
	    		sb.append("\n"+mf.put("#SampleID","#q2:types")+"\t"+dataline.trim());
	    	}
			bw = new BufferedWriter(new FileWriter(tsvfile));
			bw.append(sb).toString();
	    	bw.flush();
	    	bw.close();
			
		} catch (IOException e2) {
			e2.printStackTrace();
		}
	
    }
    

	@FXML
    private void btnAddCol() { 	
    	//Lucas:create a dialog object => https://stackoverflow.com/questions/44147595/get-more-than-two-inputs-in-a-javafx-dialog
    	Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        //Lucas: get pane from dialog
        DialogPane dialogPane = dialog.getDialogPane();
        //Lucas: setup button
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //Lucas: Create control for dialog
        TextField textField = new TextField("Input Factor Name");
        //Lucas: prepare ob-list for combobox
        ObservableList<String> options = FXCollections.observableArrayList();
        options.add("Category");
        options.add("Numeric");        
        //Lucas: Create combobox
        ComboBox<String> comboBox = new ComboBox<>(options);        
        comboBox.getSelectionModel().selectFirst();
        //Lucas: setup pane in dialog, add new container Vbox, with two controls
        dialogPane.setContent(new VBox(8, textField, comboBox));        
        //Lucas: set method when button is pressed. 
        dialog.setResultConverter((ButtonType button) -> {
        	//Lucas: when button OK press, create a new string[] with value of txtField and combobox
            if (button == ButtonType.OK) {
                return new String[] {textField.getText(),comboBox.getValue()};
            }
            return null;
        });
        
        //Lucas: result is optional, something like property. the result include String[] return from setResultConverter
        Optional<String[]> result = dialog.showAndWait();
     	if (result.isPresent()){
     		//Lucas: get string[] from result    		
    		String[] r = result.get();
    		// Set foolproof mechanism : prevent users from setting the same column name 
    		for(TableColumn tN : tableMetadata.getColumns()) {
    			 if(tN.getText().equals(r[0])) {
    				 Alert alert = new Alert(AlertType.WARNING);
    				 alert.setTitle("Warning Dialog");
    				 alert.setContentText("Column name is exists");
    				 alert.showAndWait();
    				 return;
    			 }
    		}
    			for(Map<String,String> m :tableMetadata.getItems()) {
            		m.put(r[0], "NA"); 
            	}
	
         	addColumn(r[0],r[1]);
    	}

    }
    

    //Lucas: column type is recoreded in the hashtable for export
    Hashtable<String,String> coltype = new Hashtable<String,String>();
    //Lucas: change method => record column type when add a column
    private void addColumn(String colname, String type) {
    	tableMetadata.setEditable(true);
    	//Lucas: record col type
    	coltype.put(colname, type);
    	//Create Column
    	String header = colname;
		//Data source as Map<S,S>, show String value
		TableColumn<Map<String, String>, String> tableColumn = new TableColumn<>(header);
		//Action when its cell is called (get param and return a StringProperty)
		//param is cellDataFeature, .getValue will get row object aka. HashMap<S,S>
		tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(header)));
		//What cell look like? TextFieldTableCell or CheckBoxTableCell ....
		tableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		//Actin when Edit event is called
		tableColumn.setOnEditCommit(
       		 (CellEditEvent<Map<String, String>, String> t) -> 
       		 {   
       			 // get Tableview from event
       			 // get current position and get the row ( a hashmap object)
       			 // get value for hashmap with colname, and set value with newValue for textfield control
       			 t.getTableView().getItems()
       			 .get(t.getTablePosition().getRow())
                 .put(t.getTablePosition().getTableColumn().getText(),t.getNewValue());
       			 });
		// Add column to table column collection
		tableMetadata.getColumns().add(tableColumn);
				
		//Set right mouse button
		ContextMenu contextMenu = new ContextMenu();
	    MenuItem menuItem1 = new MenuItem("Delete Column");
	    menuItem1.setOnAction((event) -> {
//	    	contextMenu.getItems().remove(tableColumn.getText());
//	    	return;
	    	ObservableList<Map<String, String>> selecolum = tableMetadata.getSelectionModel().getSelectedItems();
	    	for (Map<String, String> sc : selecolum) {
	    		
	    		sc.get(tableColumn.getText().replace(tableColumn.getText(), null));
	    	}
	    	
	    	tableMetadata.refresh();
	    	
	    });
	    contextMenu.getItems().add(menuItem1);  
        tableMetadata.setContextMenu(contextMenu);
	    
	    
    }

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableManifest.setEditable(true);
    	
    	TableColumn<sampleFile,String> firstNameCol = new TableColumn<sampleFile,String>("sample-id");
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

    	tableManifest.getItems().addAll(data);	
    	tableManifest.getColumns().addAll(firstNameCol,secondNameCol,thirdNameCol);
    	tableManifest.getSortOrder().addAll(firstNameCol, thirdNameCol);
    	
    	// get data2 column1's value from "data" Col1
    	for (sampleFile d1 : data) {
    		HashMap<String, String> d2 = new HashMap<String, String>();    	
        	d2.put("#SampleID", d1.getCol1());
        	data2.add(d2);
    	}
    	//Lucas: change addColumn method with type setting
    	addColumn("#SampleID","#q2:types");
    	tableMetadata.getItems().addAll(data2);
		
		
	}
   
}
