module com.connect4 {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires javafx.base;
    requires transitive javafx.graphics;

    opens ui to javafx.graphics, javafx.fxml;
    opens core.constants to javafx.fxml;
    opens core.logic to javafx.fxml;
    opens core.model to javafx.fxml;
    opens core.network to javafx.fxml;
    opens util to javafx.fxml;

    exports ui;
    exports core.constants;
    exports core.logic;
    exports core.model;
    exports core.network;
    exports util;
}