module org.gapp.hsujc.rna.CreateRnaManifest {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires javafx.base;

    opens org.gapp.hsujc.rna.CreateRnaManifest to javafx.fxml;
    exports org.gapp.hsujc.rna.CreateRnaManifest;
}
