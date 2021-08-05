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

public class QIIME2MainController implements Initializable {
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
	ObservableList<sampleFile> manifestdata = FXCollections.observableArrayList();
	ObservableList<Map<String,String>> metadata = FXCollections.observableArrayList();

    @FXML
    private void loadFile() throws IOException {
    	ObservableList<sampleFile> manifestdata = tableManifest.getItems();
    	String warning = " ";
    	String name;
		String path;
		String direction;
		
    	FileChooser chooseLoadFile = new FileChooser();
    	chooseLoadFile.setInitialDirectory( new File("."));
    	List<File> loadFileList = chooseLoadFile.showOpenMultipleDialog(null);
    	if(loadFileList==null || loadFileList.size()==0) {
    		//Lucas: Never use System.exit(-1) in GUI program, exit gracefully.
    		//Lucas: Show alert and return
    		return;
    	}

    	//Doing manifest file
		for(File fileList : loadFileList) {
			name = fileList.getName().split("_")[0];
			path = fileList.getAbsolutePath();
			if (fileList.getName().split("_")[1].endsWith("R1.fastq.gz")) {
				direction = "forword";
			}else {
				direction = "reverse";
			}
			sampleFile newFileData = new sampleFile(name,path,direction);
			//以for loop check file name/path have repeat
			boolean haveSamplepath = false;
			for (sampleFile sf : manifestdata) {
				if (sf.getCol2().equals(path)) {
					warning += path + ",";
					haveSamplepath = true;
					continue;
				}
			}
			if(haveSamplepath) continue;				
			manifestdata.add(newFileData);
		}
					
		//Create Hashset to put #SampleID in metadata table
    	HashSet<String> manifestSampleName = new HashSet<>();
    	for (sampleFile getManifestSampleName :tableManifest.getItems()) {
    		manifestSampleName.add(getManifestSampleName.getCol1());
    	}
    	
    	//Lucas: get current column name of metadata table
    	ObservableList<TableColumn<Map<String, String>, ?>> getMetadataColumnName = tableMetadata.getColumns();
    	List<String> metadataColumnNameList = new ArrayList<String>(); 
    	for (TableColumn metadataColumnName : getMetadataColumnName) {
    		metadataColumnNameList.add(metadataColumnName.getText());
    	}

    	//Lucas: Add sample with sample name hashset
    	for (String manifestSampleID : manifestSampleName) {	    		
	    	//Lucas: Check redundant
	    	boolean haveSample = false;
	    	for (Map<String,String> metadataSampleID : tableMetadata.getItems()) {
    			if(metadataSampleID.get("#SampleID").equals(manifestSampleID)) {
    				haveSample =true;
    				break;
    			}
	    	}
	    	
    		if(haveSample) {
    			//Lucas: pass exist sample
    			continue;
    		} else {    			
    			//Lucas: create new sample item
    			HashMap<String, String> inputMetadataSampleID = new HashMap<String, String>();
    			//Lucas: add exist table column value
    			for(String columnName :metadataColumnNameList) {
    				inputMetadataSampleID.put(columnName, "NA");
    			}
    			//Lucas: replace sampleID column with id
    			inputMetadataSampleID.put("#SampleID", manifestSampleID.trim());
				tableMetadata.getItems().add(inputMetadataSampleID);
				tableMetadata.getSortOrder().addAll(getMetadataColumnName.get(0));
    		}
    	}

        if(warning != " ") {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("Duplicated !!");
			String showInformation = "The sample under this path already exists";
			alert.setContentText(showInformation);
			alert.show();   			
    	}
    }
    
    @FXML
    private void WriteTable(ActionEvent e) {
    	//Lucas: set file name for saving.
    	FileChooser saveManifestFile = new FileChooser();
    	saveManifestFile.setTitle("Save as...");
    	
    	Date dateNow = new Date( );
        SimpleDateFormat dataFormat = new SimpleDateFormat ("yyyyMMdd");
        saveManifestFile.setInitialFileName(dataFormat.format(dateNow)+"_manifest.txt");
    	File selectManifestFileFormat  = saveManifestFile.showSaveDialog(null);
    	if(selectManifestFileFormat == null) return;
    	File txtfile = selectManifestFileFormat;
    	
    	BufferedWriter writeManifestFile;
		try {
			writeManifestFile = new BufferedWriter(new FileWriter(txtfile));
			writeManifestFile.append("sample-id"+","+"absolute-filepath"+","+"direction");

	    	ObservableList<sampleFile> Manifestdata = tableManifest.getItems();
	    	
	    	for (sampleFile getManifestData : Manifestdata) {	
	    		writeManifestFile.append("\n"+getManifestData.getCol1()+","+getManifestData.getCol2()+","+getManifestData.getCol3());
	    		
	    	}
	    	writeManifestFile.flush();
	    	writeManifestFile.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Export Metadata file
		FileChooser saveMetafile = new FileChooser();
		saveMetafile.setTitle("Save as...");
		saveMetafile.setInitialFileName(dataFormat.format(dateNow)+"_metadata.tsv");
    	File selectMetaFileFormat  = saveMetafile.showSaveDialog(null);
    	if(selectMetaFileFormat == null) return;
    	File tsvfile = selectMetaFileFormat;
    	BufferedWriter writeMetadataFile;
    	try {
    		StringBuilder metadataColumnHeader = new StringBuilder(); 
			
			//Lucas: prepare column header and type			
			tableMetadata.getColumns();
			String metadataHeaderline = "";
			String metadataColTypeline ="";
			//Lucas: append column header, type one by one
			for(TableColumn getMetadataCol : tableMetadata.getColumns()) {
				//Lucas: combine header
				String metadataColName = getMetadataCol.getText();
				metadataHeaderline += metadataColName+"\t";
				//Lucas: combine type 
				metadataColTypeline += metadataColType.get(metadataColName)+"\t";
			}
			metadataColumnHeader.append(metadataHeaderline.trim()+"\n"+metadataColTypeline.trim());
			//Lucas: append data row one by one			
			ObservableList<Map<String,String>> metadata = tableMetadata.getItems();
			//Lucas: Define key set to index value
			String[] metadataHeaderKeys = metadataHeaderline.trim().split("\t");
			String[] metadataColTypekeys = metadataColTypeline.trim().split("\t");
			for (Map<String,String> metadataTable : metadata) {
				String metadataDataLine ="";
				for(int i=1; i<metadataHeaderKeys.length ; i++ ) {
					//Prevent the cell value does not match the type
					if (metadataColTypekeys[i].equals("Category")) {
						metadataDataLine += metadataTable.get(metadataHeaderKeys[i])+"\t";
					}else if (metadataColTypekeys[i].equals("Numeric") && metadataTable.get(metadataHeaderKeys[i]).matches("-?\\d+(\\.\\d+)?")) {
						metadataDataLine += metadataTable.get(metadataHeaderKeys[i])+"\t";
					}else  {
						Alert alert = new Alert(AlertType.WARNING);
	    				alert.setTitle("Warning Dialog");
	    				alert.setContentText("The cell value does not match the type!!");
	    				alert.showAndWait();
	    				return;
					}
				}	    		
				metadataColumnHeader.append("\n"+metadataTable.put("#SampleID","#q2:types")+"\t"+metadataDataLine.trim());
	    	}
			writeMetadataFile = new BufferedWriter(new FileWriter(tsvfile));
			writeMetadataFile.append(metadataColumnHeader).toString();
			writeMetadataFile.flush();
			writeMetadataFile.close();
			
		} catch (IOException e2) {
			e2.printStackTrace();
		}	
    }  

	@FXML
    private void btnAddCol() { 	
    	//Lucas:create a dialog object => https://stackoverflow.com/questions/44147595/get-more-than-two-inputs-in-a-javafx-dialog
    	Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dialog Window");
        dialog.setHeaderText("Please specify column name and type");
        //Lucas: get pane from dialog
        DialogPane dialogPane = dialog.getDialogPane();
        //Lucas: setup button
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //Lucas: Create control for dialog
        TextField textField = new TextField("Input Factor Name");
        //Lucas: prepare ob-list for combobox
        ObservableList<String> metadataColumnTypeOptions = FXCollections.observableArrayList();
        metadataColumnTypeOptions.add("Category");
        metadataColumnTypeOptions.add("Numeric");        
        //Lucas: Create combobox
        ComboBox<String> metadataColumnTypeComboBox = new ComboBox<>(metadataColumnTypeOptions);        
        metadataColumnTypeComboBox.getSelectionModel().selectFirst();
        //Lucas: setup pane in dialog, add new container Vbox, with two controls
        dialogPane.setContent(new VBox(8, textField, metadataColumnTypeComboBox));        
        //Lucas: set method when button is pressed. 
        dialog.setResultConverter((ButtonType button) -> {
        	//Lucas: when button OK press, create a new string[] with value of txtField and combobox
            if (button == ButtonType.OK) {
                return new String[] {textField.getText(),metadataColumnTypeComboBox.getValue()};
            }
            return null;
        });
        
        //Lucas: result is optional, something like property. the result include String[] return from setResultConverter
        Optional<String[]> result = dialog.showAndWait();
     	if (result.isPresent()){
     		//Lucas: get string[] from result    		
    		String[] userSettingMetadataColumnName = result.get();
    		// Set foolproof mechanism : prevent users from setting the same column name 
    		for(TableColumn checkMetadataColumn : tableMetadata.getColumns()) {
    			 if(checkMetadataColumn.getText().equals(userSettingMetadataColumnName[0])) {
    				 Alert metadataColumnNameAlert = new Alert(AlertType.WARNING);
    				 metadataColumnNameAlert.setTitle("Warning Dialog");
    				 metadataColumnNameAlert.setContentText("Column name is exists");
    				 metadataColumnNameAlert.showAndWait();
    				 return;
    			 }
    		}
    			for(Map<String,String> checkMetadataItem :tableMetadata.getItems()) {
    				checkMetadataItem.put(userSettingMetadataColumnName[0], "NA"); 
            	}	
         	addColumn(userSettingMetadataColumnName[0],userSettingMetadataColumnName[1]);
    	}
    }    

    //Lucas: Metadata column type is recoreded in the hashtable for export
    Hashtable<String,String> metadataColType = new Hashtable<String,String>();
    //Lucas: change method => record column type when add a column
    private void addColumn(String settingMetadataColName, String settingMetadataColType) {
    	tableMetadata.setEditable(true);
    	//Lucas: record metadata column type
    	metadataColType.put(settingMetadataColName, settingMetadataColType);
    	//Create Column
    	String header = settingMetadataColName;
		//Data source as Map<S,S>, show String value
		TableColumn<Map<String, String>, String> createMetadataTableColumn = new TableColumn<>(header);
		//Action when its cell is called (get param and return a StringProperty)
		//param is cellDataFeature, .getValue will get row object aka. HashMap<S,S>
		createMetadataTableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(header)));
		//What cell look like? TextFieldTableCell or CheckBoxTableCell ....
		createMetadataTableColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		//Actin when Edit event is called
		createMetadataTableColumn.setOnEditCommit(
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
		tableMetadata.getColumns().add(createMetadataTableColumn);
				
		//Set right mouse button
		ContextMenu contextMenu = new ContextMenu();
	    MenuItem deleteFunction = new MenuItem("Delete Column");
	    deleteFunction.setOnAction((event) -> {
	    	ObservableList<Map<String, String>> catchPickMetadataCol = tableMetadata.getSelectionModel().getSelectedItems();
	    	for (Map<String, String> pickMetadataCol : catchPickMetadataCol) {
	    		
	    		pickMetadataCol.get(createMetadataTableColumn.getText().replace(createMetadataTableColumn.getText(), null));
	    	}
	    	
//	    	tableMetadata.getColumns().remove(0);
//	    	tableMetadata.getItems().removeAll(catchPickMetadataCol);
	    	tableMetadata.refresh(); 
	    	System.out.println(createMetadataTableColumn.getText());
	    });
	    contextMenu.getItems().add(deleteFunction);  
        tableMetadata.setContextMenu(contextMenu);
    
    }

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tableManifest.setEditable(true);  	
    	TableColumn<sampleFile,String> manifestFirstColName = new TableColumn<sampleFile,String>("sample-id");
    	manifestFirstColName.setCellValueFactory(new PropertyValueFactory("col1"));
    	manifestFirstColName.setCellFactory(TextFieldTableCell.forTableColumn());
    	manifestFirstColName.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFile, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFile, String> t) {
        				((sampleFile) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol1(t.getNewValue());
        			}      			
        		}
        		);
    	TableColumn<sampleFile,String> manifestSecondColName = new TableColumn<sampleFile,String>("absolute-filepath");
    	manifestSecondColName.setCellValueFactory(new PropertyValueFactory("col2"));
    	manifestSecondColName.setCellFactory(TextFieldTableCell.forTableColumn());
    	manifestSecondColName.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFile, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFile, String> t) {
        				((sampleFile) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol2(t.getNewValue());
        			}        			
        		}
        		);
    	TableColumn<sampleFile,String> manifestThirdColName = new TableColumn<sampleFile,String>("direction");
    	manifestThirdColName.setCellValueFactory(new PropertyValueFactory("col3"));
    	manifestThirdColName.setCellFactory(TextFieldTableCell.forTableColumn());
    	manifestThirdColName.setOnEditCommit(
        		new EventHandler<CellEditEvent<sampleFile, String>>() {
        			@Override
        			public void handle(CellEditEvent<sampleFile, String> t) {
        				((sampleFile) t.getTableView().getItems().get(
        						t.getTablePosition().getRow())
        				        ).setCol3(t.getNewValue());
        			}        			
        		}
        		);

    	tableManifest.getItems().addAll(manifestdata);	
    	tableManifest.getColumns().addAll(manifestFirstColName,manifestSecondColName,manifestThirdColName);
    	tableManifest.getSortOrder().addAll(manifestFirstColName, manifestThirdColName);
    	
    	// get metadata column1's value from "manifest" Col1
    	for (sampleFile manifestDataList : manifestdata) {
    		HashMap<String, String> metadataHashMap = new HashMap<String, String>();    	
    		metadataHashMap.put("#SampleID", manifestDataList.getCol1());
        	metadata.add(metadataHashMap);
    	}
    	//Lucas: change addColumn method with type setting
    	addColumn("#SampleID","#q2:types");
    	tableMetadata.getItems().addAll(metadata);
		
	}
   
}
