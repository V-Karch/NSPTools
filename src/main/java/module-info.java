module program {
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;
    requires java.desktop;

    exports com.nsptools;
    exports com.nsptools.View;
}
