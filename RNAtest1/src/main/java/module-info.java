module org.gapp.hsujc.RNAtest1 {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.gapp.hsujc.RNAtest1 to javafx.fxml;
    exports org.gapp.hsujc.RNAtest1;
}
