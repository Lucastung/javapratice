module org.ganalyst.test.GuiTv {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.base;

    opens org.ganalyst.test.GuiTv to javafx.fxml;
    exports org.ganalyst.test.GuiTv;
}
