package org.ganalyst.test.GuiTest;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    
    @FXML
    Label LCD;
    
    @FXML
    private void pressKey(ActionEvent e) {
    	Button btn = (Button) e.getSource();
    	if(btn.getText().equals("AC") | btn.getText().equals("C")) {
    		LCD.setText("0");
    		return;
    	}
    	if(btn.getText().equals("+")) {
    		
    	}    	
    	String orginal = LCD.getText();
    	LCD.setText(orginal + btn.getText());
    	
    	String k = btn.getText();
    	switch(k) {
    	case "+":
    		break;
    	case "-":
    		break;
    	case "X":
    		break;
    	case "/":
    		break;
    	default:
    		break;    		
    	}
    	
    	
    	
    }
    
    
}
