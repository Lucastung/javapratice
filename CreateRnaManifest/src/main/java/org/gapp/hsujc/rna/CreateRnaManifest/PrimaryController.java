package org.gapp.hsujc.rna.CreateRnaManifest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

public class PrimaryController implements Initializable  {
	@FXML Button LoadFile = new Button();
	@FXML Button Export = new Button();
	@FXML TableView<sampleFile> tv;
	
	public class sampleFile{		
		private SimpleStringProperty col1;
		private SimpleStringProperty col2;
		public sampleFile(String _col1, String _col2) {
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
    ObservableList<sampleFile> data = FXCollections.observableArrayList();
    //load filename
    filename=folder.listFiles();
	for (int i = 1; i< folder.listFiles().length; i++) {   		
		data.add(new sampleFile(filename[i].toString().split(folder.getName()+"/")[1], null));
	}
	tv.getItems().addAll(data);
    }
    
    @FXML
    private void export(ActionEvent e) {
    	ObservableList<sampleFile> checkdata = tv.getItems();
    	ArrayList <String> fdata = new ArrayList<String>();
    	//fdata.add(checkdata.get(0).getCol1()+"\t"+checkdata.get(0).getCol2());
    	for (int i = 0; i < checkdata.size(); i++) {
    		if (checkdata.get(i).getCol1() == null || checkdata.get(i).getCol2() == null) {
    			//need a dialog
    			Alert alert = new Alert(AlertType.INFORMATION);
    			alert.setTitle("Warning");
    			//alert.setHeaderText("Information Alert");
    			String s = "The group information of "+checkdata.get(i).getCol1()+" is empty!";
    			alert.setContentText(s);
    			alert.show();
    			fdata.clear();
    			break;
				}else {
				fdata.add(checkdata.get(i).getCol1()+"\t"+checkdata.get(i).getCol2());
				}
    		}
    	if(!fdata.isEmpty()) {
    		FileChooser savefileName = new FileChooser();
		    	savefileName.setTitle("Save as...");
		    	Date dNow = new Date( );
		        SimpleDateFormat df = new SimpleDateFormat ("yyyyMMdd");
		    	savefileName.setInitialFileName(df.format(dNow)+"_manifest.txt");
		    	File selectFile  = savefileName.showSaveDialog(null);
		    	if(selectFile == null) return;
		    	File txtfile = selectFile;
		    	FileWriter writer;
				try {
					writer = new FileWriter(selectFile);
					writer.write(fdata.get(0));
					for(int j=1;j<fdata.size();j++) {
							writer.write("\n"+fdata.get(j));
					}
				    	writer.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
		    }
		}
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		//setup tableview
	    TableColumn<sampleFile,String> firstNameCol = new TableColumn<sampleFile,String>("Sample");
	    //firstNameCol.setReorderable(true);
	    firstNameCol.setPrefWidth(200);
	    TableColumn<sampleFile,String> secondNameCol = new TableColumn<sampleFile,String>("Group");
	    tv.setEditable(true);
	    tv.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
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
	    		});
	    secondNameCol.setCellValueFactory(new PropertyValueFactory("col2"));
	    secondNameCol.setCellFactory(TextFieldTableCell.forTableColumn());
	    secondNameCol.setPrefWidth(100);
	    secondNameCol.setOnEditCommit(
	    		new EventHandler<CellEditEvent<sampleFile, String>>() {
	    			@Override
	    			public void handle(CellEditEvent<sampleFile, String> t) {
	    				((sampleFile) t.getTableView().getItems().get(
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
	    MenuItem menuItem4 = new MenuItem("Delete sample");
	    contextMenu.getItems().add(menuItem1);
	    contextMenu.getItems().add(menuItem2);
	    contextMenu.getItems().add(menuItem3);
	    contextMenu.getItems().add(menuItem4);
	    tv.setContextMenu(contextMenu);
	    //Setup item1 for control
	    menuItem1.setOnAction((event) -> {
	    	ObservableList<sampleFile> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (sampleFile sf : mselect) {
	    		sf.setCol2("Control");
	    	}
	    	tv.refresh();
	    });
	    //Setup item2 for test
	    menuItem2.setOnAction((event) -> {
	    	ObservableList<sampleFile> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (sampleFile sf : mselect) {
	    		sf.setCol2("Test");
	    	}
	    	tv.refresh();
	    });
	    
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
	        ObservableList<sampleFile> mselect = tv.getSelectionModel().getSelectedItems();
	     	if (result.isPresent()){
	     		//Lucas: get string[] from result	     		
	    		String[] r = result.get();
	    		for (sampleFile sf : mselect) {
		    		sf.setCol2(r[0]);
		    	}
	     	}
	    	tv.refresh();
	    });
	    menuItem4.setOnAction((event) -> {
	    	ObservableList<sampleFile> mselect = tv.getSelectionModel().getSelectedItems();
	    	for (sampleFile sf : mselect) {
	    		sf.setCol2(null);
	    		sf.setCol1(null);
	    	}
	    	tv.refresh();
	    	
	    });
	}
}
