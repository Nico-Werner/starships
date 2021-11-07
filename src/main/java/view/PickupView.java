package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;

@Data
public class PickupView {
    ImageView imageView;

    public PickupView(Image image, double x, double y) {
        this.imageView = new ImageView(image);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
    }
}
