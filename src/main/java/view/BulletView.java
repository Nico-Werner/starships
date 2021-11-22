package view;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

@Getter
public class BulletView implements View {
    private final ImageView imageView;

    public BulletView(Image image, double layoutX, double layoutY, double rotate) {
        this.imageView = new ImageView(image);
        this.imageView.setLayoutX(layoutX);
        this.imageView.setLayoutY(layoutY);
        this.imageView.setRotate(rotate);
    }
}
