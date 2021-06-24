package org.gapp.hsujc.rna.CreateRnaManifest;

import java.io.File;
import java.net.URL;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class PrimaryController implements Initializable  {
	@FXML TextArea outResult = new TextArea();
	@FXML Button LoadFile = new Button();
	@FXML TableView<sampleFiles> tv;
	public class sampleFiles{		
		private SimpleStringProperty col1;
		private SimpleStringProperty col2;
		public sampleFiles(String _col1, String _col2) {
			this.col1 = new SimpleStringProperty(_col1);
			this.col2 = new SimpleStringProperty(_col2);
			
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
	}
    @FXML
    private void loadfile(ActionEvent e) {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setInitialDirectory(new File("."));
    directoryChooser.setTitle ("Choose RNAseq Result Folder");
    File folder = directoryChooser.showDialog(null);
    File[] filename;  
    outResult.setWrapText(true);
    //left btn menu

    //
    //
    ObservableList<sampleFiles> data = FXCollections.observableArrayList();

    //inputdiolog
    if (folder != null)
    {
    	filename=folder.listFiles();
    	for (int i = 1; i< folder.listFiles().length; i++) {   		
    		data.add(new sampleFiles(filename[i].toString().split(folder.getName()+"/")[1], null));    		
    		//System.out.println(filename[i].toString().split(folder.getName()+"/")[1]);
    		outResult.appendText(filename[i].toString().split(folder.getName()+"/")[1]+ "\n");
    		 
    	}
    	tv.getItems().addAll(data);
    	
    }else{
    outResult.setText("Folder not exist");
    System.out.println("Folder not exist");
    }
    }
    //
    /**
    @FXML
    public void leftBtn() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem menuItem1 = new MenuItem("Add to control group");
        MenuItem menuItem2 = new MenuItem("Add to experiment group");
        MenuItem menuItem3 = new MenuItem("Add to ....");

        menuItem3.setOnAction((event) -> {
            System.out.println("Choice 3 clicked!");
        });

        //contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
        contextMenu.getItems().add(menuItem1);
        contextMenu.getItems().add(menuItem2);
        contextMenu.getItems().add(menuItem3);
        //TextArea textArea = new TextArea();
        outResult.setContextMenu(contextMenu);
        //VBox vBox = new VBox(outResult);
        //Scene scene = new Scene(outResult);

        //stage.setScene(scene);
        //primaryStage.setTitle("menu test");

        //stage.show();
    }
    **/
    @FXML
    private void btnAddCol() {
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
    	        
    	        

    	    }
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//setup tableview
	    TableColumn<sampleFiles,String> firstNameCol = new TableColumn<sampleFiles,String>("Sample");
	    TableColumn<sampleFiles,String> secondNameCol = new TableColumn<sampleFiles,String>("Group");
	    tv.setEditable(true);
	    tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
	    secondNameCol.setCellValueFactory(new PropertyValueFactory("col2"));
	    secondNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    secondNameCol.setOnEditCommit(
	    		new EventHandler<CellEditEvent<sampleFiles, String>>() {
	    			@Override
	    			public void handle(CellEditEvent<sampleFiles, String> t) {
	    				((sampleFiles) t.getTableView().getItems().get(
	    						t.getTablePosition().getRow())
	    				        ).setCol1(t.getNewValue());
	    			}
	    		});
	    tv.getColumns().add(firstNameCol);
	    tv.getColumns().add(secondNameCol);
	    
	    //Setup context menu for tv
	    ContextMenu contextMenu = new ContextMenu();
	    MenuItem menuItem1 = new MenuItem("Add to control group");
	    MenuItem menuItem2 = new MenuItem("Add to experiment group");
	    MenuItem menuItem3 = new MenuItem("Add to ....");
	    //Setup item1 for control
	    menuItem1.setOnAction((event) -> {
	    	ObservableList<sampleFiles> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (sampleFiles sf : mselect) {
	    		sf.setCol2("Control");
	    	}
	    	tv.refresh();
	    });
	    //Setup item2 for test
	    menuItem2.setOnAction((event) -> {
	    	ObservableList<sampleFiles> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (sampleFiles sf : mselect) {
	    		sf.setCol2("Test");
	    	}
	    	tv.refresh();
	    });
	    
	    menuItem3.setOnAction((event) -> {
	    	Dialog<String[]> dialog = new Dialog<>();
		    dialog.setTitle("Group setting");
		    dialog.setHeaderText("Please enter sample type");
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
	        ObservableList<sampleFiles> mselect = tv.getSelectionModel().getSelectedItems();
	     	if (result.isPresent()){
	     		//Lucas: get string[] from result	     		
	    		String[] r = result.get();
	    		for (sampleFiles sf : mselect) {
		    		sf.setCol2(r[0]);
		    	}
	     	}
	    	tv.refresh();
	    });
	    //contextMenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
	    contextMenu.getItems().add(menuItem1);
	    contextMenu.getItems().add(menuItem2);
	    contextMenu.getItems().add(menuItem3);
	    tv.setContextMenu(contextMenu);
	    
	    //test code for textarea
	    ContextMenu contextMenu2 = new ContextMenu();
	    MenuItem menuItem4 = new MenuItem("To control group");
	    MenuItem menuItem5 = new MenuItem("To experiment group");
	    MenuItem menuItem6 = new MenuItem("To ....");
	    menuItem6.setOnAction((event) -> {
	        System.out.println("Choice 3 clicked!");
	    });
	    contextMenu2.getItems().add(menuItem4);
	    contextMenu2.getItems().add(menuItem5);
	    contextMenu2.getItems().add(menuItem6);
	    outResult.setContextMenu(contextMenu2);
	    
		
	}
}
