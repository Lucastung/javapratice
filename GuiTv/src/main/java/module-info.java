module org.ganalyst.test.GuiTv {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;
	requires javafx.graphics;

    opens org.ganalyst.test.GuiTv to javafx.fxml;
    exports org.ganalyst.test.GuiTv;
}
