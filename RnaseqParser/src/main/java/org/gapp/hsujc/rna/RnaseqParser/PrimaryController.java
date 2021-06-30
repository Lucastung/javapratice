package org.gapp.hsujc.rna.RnaseqParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class PrimaryController implements Initializable {
	@FXML Button LoadFile = new Button();
	@FXML Button Export = new Button();
    @FXML TableView<Map<String,String>> tv;

 	//environment setting
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	
		//Set default table
    	addColumn("SampleID");
    	addColumn("Group");
    	//addColumn("Path");  //store path
    	//addColumn("fname");  //store file name with .gene.result
    	
    	//tv.setEditable(true);
    	tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        ObservableList<Map<String,String>> data = FXCollections.observableArrayList();
    	tv.getItems().addAll(data);		

	    //Setup context menu for tv
	    ContextMenu contextMenu = new ContextMenu();
	    MenuItem menuItem1 = new MenuItem("Add to control group");
	    MenuItem menuItem2 = new MenuItem("Add to experiment group");
	    MenuItem menuItem3 = new MenuItem("Add to ....");
	    MenuItem menuItem4 = new MenuItem("Delete sample");
	    contextMenu.getItems().add(menuItem1);
	    contextMenu.getItems().add(menuItem2);
	    contextMenu.getItems().add(menuItem3);
	    contextMenu.getItems().add(menuItem4);
	    tv.setContextMenu(contextMenu);
	    //Setup item1 for control
	    menuItem1.setOnAction((event) -> {
	    	ObservableList<Map<String,String>> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (Map<String,String> sf : mselect) {
	    		sf.put("Group", "Con");
	    	}
	    	tv.refresh();
	    });
	    //Setup item2 for test
	    menuItem2.setOnAction((event) -> {
	    	ObservableList<Map<String,String>> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (Map<String,String> sf : mselect) {
	    		sf.put("Group", "Test");
	    	}
	    	tv.refresh();
	    });
	    //Setup Item3 for other group
	    menuItem3.setOnAction((event) -> {
	    	Dialog<String[]> dialog = new Dialog<>();
		    dialog.setTitle("Group setting");
		    dialog.setHeaderText("Please enter group name");
		    DialogPane dialogPane = dialog.getDialogPane();
		    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		    TextField textField = new TextField("Group?");
		    dialogPane.setContent(new VBox(8, textField));
	        //Lucas: set method when button is pressed. 
	        dialog.setResultConverter((ButtonType button) -> {
	        	//Lucas: when button OK press, create a new string[] with value of txtField and combobox
	            if (button == ButtonType.OK) {
	                return new String[] {textField.getText()};
	            }
	            return null;
	        });
	        //Lucas: result is optional, something like property. the result include String[] return from setResultConverter
	        Optional<String[]> result = dialog.showAndWait();
	        ObservableList<Map<String,String>> mselect = tv.getSelectionModel().getSelectedItems();	    	
	     	if (result.isPresent()){
	     		//Lucas: get string[] from result	     		
	    		String[] r = result.get();
	    		for (Map<String,String> sf : mselect) {
		    		sf.put("Group", r[0]);
		    	}
	     	}
	    	tv.refresh();
	    });
	  //Setup Item4 for delete sample item
	    menuItem4.setOnAction((event) -> {
	    	ObservableList<Map<String,String>> mselect = tv.getSelectionModel().getSelectedItems();
	    	tv.getItems().removeAll(mselect);
	    	tv.refresh();	    	
	    });
	}
	private void addColumn(String colname) {    	
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
		tv.getColumns().add(tableColumn);  
    }	
	
	@FXML
    private void loadfile(ActionEvent e) {
		DirectoryChooser directoryChooser = new DirectoryChooser();
	    directoryChooser.setInitialDirectory(new File("."));
	    directoryChooser.setTitle ("Choose RNAseq Result Folder");
	    File folder = directoryChooser.showDialog(null);
	    File[] filename;  
	    //load filename	    
	    if (folder == null || !folder.isDirectory()) {
	    	System.out.println("no folder selected!!");
	    	return;
	    }else {
	    	filename=folder.listFiles();
	    }
	    //load file list
	    ObservableList<Map<String,String>> data = tv.getItems();

	    String warning = "Sample ";
		for (int i = 0; i< filename.length; i++) {
			String fname = filename[i].getName();
			String sname = fname.replace(".genes.results", ""); //fname.split(".genes.results")[0];
			
			boolean haveSample = false;
			for( Map<String,String> hm : data ) {
				if(hm.containsKey("SampleID") && hm.get("SampleID").equals(sname)) {
					warning += sname + ",";
					haveSample = true;
					continue;
				}
			}
			if(haveSample) continue;
			
			HashMap<String, String> d2 = new HashMap<String, String>();
			d2.put("SampleID", sname);
			d2.put("Path", filename[i].getAbsolutePath());
			d2.put("fname", fname);
			data.add(d2);
		}
		if(warning != "Sample ") {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			alert.setHeaderText("File(s) already exist");
			String s = warning+" already exist!";
			alert.setContentText(s);
			alert.show();  
		}
	}
	@FXML
    private void export(ActionEvent e) {
		ObservableList<Map<String, String>> checkdata = tv.getItems();
		
    	if(checkdata.isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			//alert.setHeaderText("Information Alert");
			String s = "Please load dataset first.";
			alert.setContentText(s);
			alert.show();
			return;
    	}
    	for(Map<String, String> sf : checkdata) {
    		if(sf.get("Group") == null || sf.get("Group").isEmpty()) {
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Warning");
    			//alert.setHeaderText("Information Alert");
    			String s = "There are some of information "+sf.get("SampleID")+" may empty!";
    			alert.setContentText(s);
    			alert.show();   			
    			return;
    		}
    	}    	
    	//Lucas: set file name for saving.
    	FileChooser savefileName = new FileChooser();
    	savefileName.setTitle("Save as...");   	
    	Date dNow = new Date( );
        SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd");
    	savefileName.setInitialFileName(df.format(dNow)+"_manifest.txt");
    	File selectFile  = savefileName.showSaveDialog(null);
    	if(selectFile == null) return;
    	File txtfile = selectFile;
    	//merge
    	String output = selectFile.getAbsoluteFile().toString();  	
	    String datacols = "expected_count,TPM,FPKM";
	    String rowidcols = "gene_id";
	    List<File> ifs = new ArrayList<File>();
	    if(checkdata.isEmpty()) {
	    	Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			//alert.setHeaderText("Information Alert");
			String s = "Please select dataset first.";
			alert.setContentText(s);
			alert.show();
	    	System.out.println("No data");
	    }else {
	    	for (Map<String,String> sf : checkdata) {
    		ifs.add(new File(sf.get("Path")));
    	}
	    }
FileProc model = new FileProc();
		
		try {
			model.Merge_RowID(output,rowidcols,datacols,ifs);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	//
    	BufferedWriter bf;
		try {
			bf = new BufferedWriter(new FileWriter(txtfile));
	    	ObservableList<Map<String, String>> tvdata = tv.getItems();
	    	bf.append(tvdata.get(0).get("fname")+"\t"+tvdata.get(0).get("Group"));
	    	for(int i =1; i < tvdata.size();i++) {
	    		bf.append("\n"+tvdata.get(i).get("fname")+"\t"+tvdata.get(i).get("Group")); 	
	    	}
	    	/*This will add "\n" in end of file
	    	 * for (Map<String, String> sf : tvdata) {	
	    		bf.append(sf.get("SampleID")+"\t"+sf.get("Group")+"\n");  		
	    	}*/
	    	bf.flush();
	    	bf.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			}
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("File Saving");
		//alert.setHeaderText("Information Alert");
		String s = "The file was saved.";
		alert.setContentText(s);
		alert.show();
		}
	
	 /*
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
    			for(Map<String,String> m :data) {
            		m.put(r[0]); 
            	}
	
         	addColumn(r[0]);
    	}*/
	}

