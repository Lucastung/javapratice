module org.gapp.hsujc.RnaMetaGui {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.gapp.hsujc.RnaMetaGui to javafx.fxml;
    exports org.gapp.hsujc.RnaMetaGui;
}
