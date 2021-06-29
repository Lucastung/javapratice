module org.gapp.hsujc.rna.RnaseqParser {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.gapp.hsujc.rna.RnaseqParser to javafx.fxml;
    exports org.gapp.hsujc.rna.RnaseqParser;
}
