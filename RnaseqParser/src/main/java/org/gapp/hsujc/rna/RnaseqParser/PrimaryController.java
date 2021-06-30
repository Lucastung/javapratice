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
import java.util.Hashtable;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class PrimaryController implements Initializable {
	@FXML Button LoadFile = new Button();
	@FXML Button Export = new Button();
    @FXML TableView<Map<String,String>> tvSampleList;

 	//environment setting
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	

    	//Setup TableView property 
    	tvSampleList.setEditable(true);
    	tvSampleList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	//Prepare items of tableview
        ObservableList<Map<String,String>> data = FXCollections.observableArrayList();
    	tvSampleList.getItems().addAll(data);		
    	
		//Set default table column for tableview
    	addColumn("SampleID");
    	addColumn("Group");

	    //Setup context menu for tvSampleList
	    ContextMenu contextMenu = new ContextMenu();
	    MenuItem menuItem1 = new MenuItem("Set as Control");
	    MenuItem menuItem2 = new MenuItem("Set as Test");
	    MenuItem menuItem3 = new MenuItem("Assign as ....");
	    MenuItem miAddTC = new MenuItem("Add factor");
	    MenuItem miDelTC = new MenuItem("Remove factor");
	    MenuItem menuItem4 = new MenuItem("Delete sample");
	    ObservableList<MenuItem> milist = contextMenu.getItems();
	    milist.add(menuItem1);
	    milist.add(menuItem2);
	    milist.add(menuItem3);	    
	    milist.add(new SeparatorMenuItem());
	    milist.add(miAddTC);
	    milist.add(miDelTC);
	    milist.add(new SeparatorMenuItem());
	    milist.add(menuItem4);
	    tvSampleList.setContextMenu(contextMenu);
	    
	    //Setup item1 for control
	    menuItem1.setOnAction((event) -> {
	    	ObservableList<Map<String,String>> mselect = tvSampleList.getSelectionModel().getSelectedItems();
	    	for (Map<String,String> sf : mselect) {
	    		sf.put("Group", "Control");
	    	}
	    	tvSampleList.refresh();
	    });
	    //Setup item2 for test
	    menuItem2.setOnAction((event) -> {
	    	ObservableList<Map<String,String>> mselect = tvSampleList.getSelectionModel().getSelectedItems();
	    	for (Map<String,String> sf : mselect) {
	    		sf.put("Group", "Test");
	    	}
	    	tvSampleList.refresh();
	    });
	    //Setup Item3 for other group
	    menuItem3.setOnAction((event) -> {
	    	Dialog<String[]> dialog = new Dialog<>();
		    dialog.setTitle("Assign as");
		    //dialog.setHeaderText("Please enter group name");
		    DialogPane dialogPane = dialog.getDialogPane();
		    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		    
		    //search exist factor and to combobox
		    ObservableList<TableColumn<Map<String, String>, ?>> tcs =  tvSampleList.getColumns();
	        ObservableList<String> options = FXCollections.observableArrayList();
	        for(TableColumn<Map<String, String>, ?> tc : tcs) {
	        	String tname = tc.getText();
	        	if(tname.equals("SampleID"))continue;
	        	options.add(tname);
	        }
   	        ComboBox<String> comboBox = new ComboBox<>(options); 
   	        comboBox.setEditable(false);
	        comboBox.getSelectionModel().selectFirst();	    
		    		    
		    TextField textField = new TextField("Group?");
		    dialogPane.setContent(new VBox(8,new Label("Assign to"),comboBox, new Label("with"), textField));
	        //Lucas: set method when button is pressed. 
	        dialog.setResultConverter((ButtonType button) -> {
	        	//Lucas: when button OK press, create a new string[] with value of txtField and combobox
	            if (button == ButtonType.OK) {
	                return new String[] { comboBox.getSelectionModel().getSelectedItem() , textField.getText()};
	            }
	            return null;
	        });
	        //Lucas: result is optional, something like property. the result include String[] return from setResultConverter
	        Optional<String[]> result = dialog.showAndWait();
	        ObservableList<Map<String,String>> mselect = tvSampleList.getSelectionModel().getSelectedItems();	    	
	     	if (result.isPresent()){
	     		//Lucas: get string[] from result	     		
	    		String[] r = result.get();
	    		for (Map<String,String> sf : mselect) {
		    		sf.put(r[0], r[1]);
		    	}
	     	}
	    	tvSampleList.refresh();
	    });
		
	    //Setup miAddTC for Add column
	    miAddTC.setOnAction((event) -> {
	    	TextInputDialog dialog = new TextInputDialog();
	    	dialog.setHeaderText("Add new factor in table");
	    	dialog.setTitle("Input Dialog");
	    	dialog.setContentText("Enter factor label:");
	    	// Traditional way to get the response value.
	    	Optional<String> result = dialog.showAndWait();
	     	if (result.isPresent()){
	     		//Lucas: get string[] from result    		
	    		String r = result.get();
	    		// Set foolproof mechanism : prevent users from setting the same column name 
	    		for(TableColumn tN : tvSampleList.getColumns()) {
	    			 if(tN.getText().equals(r)) {
	    				 Alert alert = new Alert(AlertType.WARNING);
	    				 alert.setTitle("Warning Dialog");
	    				 alert.setContentText("factor"+ r + " is exists");
	    				 alert.showAndWait();
	    				 return;
	    			 }
	    		}
	    			for(Map<String,String> m :tvSampleList.getItems()) {
	            		m.put(r, "NA"); 
	            	}
		
	         	addColumn(r);
	    	}
	    });	    
	  //setup removet miDelTC
	    miDelTC.setOnAction((event) -> {
	    	Dialog<String> dialog = new Dialog<>();
		    dialog.setTitle("remvoe factor");
		    DialogPane dialogPane = dialog.getDialogPane();
		    dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		    
		    //search exist factor and to combobox
		    ObservableList<TableColumn<Map<String, String>, ?>> tcs =  tvSampleList.getColumns();
	        ObservableList<String> options = FXCollections.observableArrayList();
	        for(TableColumn<Map<String, String>, ?> tc : tcs) {
	        	String tname = tc.getText();
	        	if(tname.equals("SampleID"))continue;
	        	options.add(tname);
	        }
   	        ComboBox<String> comboBox = new ComboBox<>(options); 
   	        comboBox.setEditable(false);
	        comboBox.getSelectionModel().selectFirst();	    
		    dialogPane.setContent(new VBox(8,new Label("Selected a factor to remove, all values of the factor will removed."),comboBox));
	        //Lucas: set method when button is pressed. 
	        dialog.setResultConverter((ButtonType button) -> {
	        	//Lucas: when button OK press, create a new string[] with value of txtField and combobox
	            if (button == ButtonType.OK) {
	                return comboBox.getSelectionModel().getSelectedItem();
	            }
	            return null;
	        });
	        //Lucas: result is optional, something like property. the result include String[] return from setResultConverter
	        Optional<String> result = dialog.showAndWait();	        	    	
	     	if (result.isPresent()){
	     		String r = result.get();
	     		//remove factor to all rows
	     		ObservableList<Map<String,String>> mselect = tvSampleList.getSelectionModel().getSelectedItems();     		
	    		for (Map<String,String> sf : mselect) {
		    		sf.remove(r);
		    	}
	    		//remove tableColumn form tableview
	    		TableColumn Target = null;
		        for(TableColumn<Map<String, String>, ?> tc : tcs) {
		        	if (tc.getText().equals(r)) {
		        		Target = tc;
		        		break;
		        	};
		        }
		        if(Target != null) tvSampleList.getColumns().remove(Target);
	     	}
	    	tvSampleList.refresh();
	    });
	    
	    
	  //Setup Item4 for delete sample item
	    menuItem4.setOnAction((event) -> {
	    	ObservableList<Map<String,String>> mselect = tvSampleList.getSelectionModel().getSelectedItems();
	    	tvSampleList.getItems().removeAll(mselect);
	    	tvSampleList.refresh();	    	
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
		tvSampleList.getColumns().add(tableColumn);  
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
	    ObservableList<Map<String,String>> data = tvSampleList.getItems();

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
		ObservableList<Map<String, String>> checkdata = tvSampleList.getItems();
		ObservableList<TableColumn<Map<String, String>, ?>>tcs = tvSampleList.getColumns();
		
    	if(checkdata.isEmpty()) {
    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Warning");
			String s = "Please load dataset first.";
			alert.setContentText(s);
			alert.show();
			return;
    	}
    	//Create manifest content and check null value
    	//Get header list
    	ArrayList <String> hlst = new ArrayList<String>();
    	String header = "SampleID";
    	for(TableColumn tc:tcs) {
    		if(tc.getText().equals("SampleID")) continue;
    		hlst.add(tc.getText());
    		header += "\t"+tc.getText();
    	}    	
    	StringBuilder sb = new StringBuilder(header);
    	//append data and check     	
    	for(Map<String, String> sf : checkdata) {
    		String dline = sf.get("SampleID");
    		for(String factor : hlst) {
    			String value = sf.get(factor);
    			if(value == null || value.isEmpty()) {
        			Alert alert = new Alert(AlertType.INFORMATION);
        			alert.setTitle("Warning");
        			//alert.setHeaderText("Information Alert");
        			String s = "factor "+factor+"of "+sf.get("SampleID")+" is empty!";
        			alert.setContentText(s);
        			alert.show();   			
        			return;
        		}
    			dline += "\t"+value;   			
    		}
    		sb.append("\n"+dline);    		    		
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
	    //List<File> ifs = new ArrayList<File>();
	    
	    ArrayList<String> ifst = new ArrayList<String>();
	    
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
	    		ifst.add(sf.get("SampleID")+"\t"+sf.get("Path"));
	    		}
	    }
	    FileProc model = new FileProc();
		
		try {
			model.Merge_RowID(output,rowidcols,datacols,ifst);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
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
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("File Saving");
		//alert.setHeaderText("Information Alert");
		String s = "The file was saved.";
		alert.setContentText(s);
		alert.show();
		}
	

	}

