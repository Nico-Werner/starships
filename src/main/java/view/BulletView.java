package view;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Getter;

@Getter
public class BulletView {
    ImageView imageView;

    public BulletView(Image image, double layoutX, double layoutY, double rotate) {
        this.imageView = new ImageView(image);
        this.imageView.setLayoutX(layoutX);
        this.imageView.setLayoutY(layoutY);
        this.imageView.setRotate(rotate);
    }

    public void move(Vector2 to) {
        this.imageView.setLayoutX(to.getX());
        this.imageView.setLayoutY(to.getY());
    }
}
