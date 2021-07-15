module org.gapp.hsujc.sc.geneextractor {
    requires javafx.controls;
    requires javafx.fxml;

    opens org.gapp.hsujc.sc.geneextractor to javafx.fxml;
    exports org.gapp.hsujc.sc.geneextractor;
}
