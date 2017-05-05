package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.layout.Pane;

/** A pane with an image background */
class ImagePane extends Pane {
  // size an image by placing it in a pane.
  ImagePane(String imageLoc) {
    this(imageLoc, "-fx-background-size: cover; -fx-background-repeat: no-repeat;");
  }

  // size an image by placing it in a pane.
  ImagePane(String imageLoc, String style) {
    this(new SimpleStringProperty(imageLoc), new SimpleStringProperty(style));
  }

  // size a replacable image in a pane and add a replaceable style.
  ImagePane(StringProperty imageLocProperty, StringProperty styleProperty) {
    styleProperty().bind(
    new SimpleStringProperty("-fx-background-image: url(\"")
        .concat(imageLocProperty)
        .concat(new SimpleStringProperty("\");"))
        .concat(styleProperty)    
    );
  }
}
