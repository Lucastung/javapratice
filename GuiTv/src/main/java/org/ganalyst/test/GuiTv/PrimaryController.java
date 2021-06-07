package org.ganalyst.test.GuiTv;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

public class PrimaryController {
	public class dataRow{
	    private  SimpleStringProperty col1;
	    private  SimpleStringProperty col2;
	    private  SimpleStringProperty col3;
	    
	    public dataRow(String _col1, String _col2, String _col3) {
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
	TableView<dataRow> tv;
	
	@FXML
	TableView<Map<String,String>> tableView;
	
	
    @FXML
    private void switchToSecondary() throws IOException {
    	
    	ObservableList<dataRow>  data = FXCollections.observableArrayList(
    			 new dataRow("A1","B1","C1")
    			,new dataRow("A2","B2","C2")
    			,new dataRow("A3","B3","C3"));

    	tv.setEditable(true);
        TableColumn<dataRow,String> firstNameCol = new TableColumn<dataRow,String>("Column 1");
        firstNameCol.setCellValueFactory(new PropertyValueFactory("col1"));         
        firstNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        firstNameCol.setOnEditCommit(
       		 (CellEditEvent<dataRow, String> t) -> 
       		 { t.getTableView().getItems().get(
                    t.getTablePosition().getRow())
                    .setCol1(t.getNewValue());});
        
        
        TableColumn tcCol2 = new TableColumn("Column 2");
        TableColumn<dataRow,String> secondNameCol = new TableColumn<dataRow,String>("Column 2");
        secondNameCol.setCellValueFactory(new PropertyValueFactory("col2"));
        secondNameCol.setCellFactory(TextFieldTableCell.forTableColumn());   
        secondNameCol.setOnEditCommit(
       		    new EventHandler<CellEditEvent<dataRow, String>>() {
       		       @Override
       		       public void handle(CellEditEvent<dataRow, String> t) {
       		            ((dataRow) t.getTableView().getItems().get(
       		                t.getTablePosition().getRow())
       		                ).setCol2(t.getNewValue());
       		        }
       		    }
       		);
   	 tv.setItems(data);
     tv.getColumns().addAll(firstNameCol,secondNameCol);
    }
    
    @FXML
    private void String2Table() {
    	
    	// https://stackoverflow.com/questions/12211822/populating-a-tableview-with-strings
    	
    	// HashMap as a row object
    	ArrayList<Map<String, String>> valuesArray = new ArrayList<>();
    	tableView.setEditable(true);

    	//add valuesArray 5 X 10
    	for (int j = 0;j<10;j++) {
    		HashMap<String, String> row =  new HashMap<String,String>();
        	for (int i = 0; i <5;i++) {        	
        		row.put(("col"+i), ("data"+j+":"+i));
        	}
        	valuesArray.add(row);
    	}
    	//Create tableColumn
    	for (int i = 0;i<5;i++) {
    		String header = "col"+i;
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
    		tableView.getColumns().add(tableColumn);
    	}
    	// Add data to table 
    	tableView.getItems().addAll(valuesArray);
    	
    	// you can readed edited table by getItems and dump it to text file. 
    	
    }
    
    
}
