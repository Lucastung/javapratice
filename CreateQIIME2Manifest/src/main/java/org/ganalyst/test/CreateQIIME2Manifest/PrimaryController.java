package org.ganalyst.test.CreateQIIME2Manifest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
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
//	TableView<sampleFile> tableMetadata;
	TableView<Map<String,String>> tableMetadata;
	ObservableList<sampleFile> data = FXCollections.observableArrayList();
	ObservableList<Map<String,String>> data2 = FXCollections.observableArrayList();

    @FXML
    private void loadFile() throws IOException {
    	
    	File f = new File("raw/");
    	if(!f.isDirectory()) {
    		System.exit(-1);
    	}else {
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

		tv.getItems().addAll(data);	
    	tv.getColumns().addAll(firstNameCol,secondNameCol,thirdNameCol);
    	
    	// get data2 column1's value from "data" Col1
    	for (sampleFile d1 : data) {
    		//System.out.println(d1.getCol1());
    		HashMap<String, String> d2 = new HashMap<String, String>();    	
        	d2.put("#SampleID", d1.getCol1());
        //	d2.put("Type","Category");
        	data2.add(d2);
    	}
    	//Lucas: change addColumn method with type setting
    	addColumn("#SampleID","#Q2");
    	tableMetadata.getItems().addAll(data2);
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

	    	ObservableList<sampleFile> tvdata = tv.getItems();
	    	
	    	for (sampleFile sf : tvdata) {	
	    		bf.append("\n"+sf.getCol1()+","+sf.getCol2()+","+sf.getCol3());
	    		
	    	}
	    	bf.flush();
	    	bf.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
    }
    

    @SuppressWarnings("unchecked")
	@FXML
    private void btnAddCol() {
/*    	//inputbox    	
    	TextInputDialog dialog = new TextInputDialog("Input dialog");
    	dialog.setTitle("Column Name");
    	dialog.setContentText("Please enter factor:");
    	Optional<String> result = dialog.showAndWait();
    	
    	//http://tutorials.jenkov.com/javafx/combobox.html
    	ComboBox comboBox = new ComboBox();
    	comboBox.getItems().add("categorical");
    	comboBox.getItems().add("numeric");
    	comboBox.setEditable(true);
    	comboBox.setOnAction((event) -> {
    	    int selectedIndex = comboBox.getSelectionModel().getSelectedIndex();
    	    Object selectedItem = comboBox.getSelectionModel().getSelectedItem();

    	    System.out.println("Selection made: [" + selectedIndex + "] " + selectedItem);
    	    System.out.println("   ComboBox.getValue(): " + comboBox.getValue());
    	});
*/    	
    	
    	//Lucas:create a dialog object
    	Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Dialog Test");
        dialog.setHeaderText("Please specify…");
        //Lucas: get pane from dialog
        DialogPane dialogPane = dialog.getDialogPane();
        //Lucas: setup button
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        //Lucas: Create control for dialog
        TextField textField = new TextField("FactorName");
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
    			for(Map<String,String> m :data2) {
            		m.put(r[0], "NA"); 
            	}
	
         	addColumn(r[0],r[1]);
    	}

    }
    
    //老師建議參考文件 => https://stackoverflow.com/questions/44147595/get-more-than-two-inputs-in-a-javafx-dialog
/*    public class DialogTest extends Application {
    	@Override
        public void start(Stage primaryStage) {
        	Dialog<Results> dialog = new Dialog<>();
            dialog.setTitle("Dialog Test");
            dialog.setHeaderText("Please specify…");
            DialogPane dialogPane = dialog.getDialogPane();
            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            TextField textField = new TextField("Name");
//          DatePicker datePicker = new DatePicker(LocalDate.now());
            ObservableList<Venue> options = FXCollections.observableArrayList(Venue.values());
            ComboBox<Venue> comboBox = new ComboBox<>(options);
            comboBox.getSelectionModel().selectFirst();
            dialogPane.setContent(new VBox(8, textField, comboBox));
            Platform.runLater(textField::requestFocus);
            dialog.setResultConverter((ButtonType button) -> {
            	if (button == ButtonType.OK) {
            		return new Results(textField.getText(), comboBox.getValue());
            	}
            	return null;
            });
            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((Results results) -> {
                System.out.println(
                    results.text + " " + results.venue);
            });
        }
        private enum Venue {Here, There, Elsewhere}
        private class Results {

            String text;
            Venue venue;

            public Results(String name, Venue venue) {
                this.text = name;
                this.venue = venue;
            }
        }
        public void main(String[] args) {
            launch(args);
        }
    	
    }
*/
   
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
    	
    }
    
    
    
    
}
