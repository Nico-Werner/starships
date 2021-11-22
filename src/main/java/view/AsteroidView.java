package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;

@Data
public class AsteroidView implements View {
    ImageView imageView;

    public AsteroidView(Image image, double x, double y, double rotate) {
        imageView = new ImageView(image);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        imageView.setRotate(rotate);
    }
}
