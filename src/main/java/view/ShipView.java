package view;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Data;

@Data
public class ShipView {
    ImageView imageView;
    Shape shape;

    public ShipView(Image image, int x, int y) {
        this.imageView = new ImageView(image);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        shape = new Rectangle(image.getWidth(), image.getHeight());
    }

    public double getLayoutX() {
        return imageView.getLayoutX();
    }

    public double getLayoutY() {
        return imageView.getLayoutY();
    }

    public double getRotate() {
        return imageView.getRotate();
    }

    public void setRotate(double v) {
        imageView.setRotate(v);
    }

    public void move(Vector2 to) {
        imageView.setLayoutX(to.getX());
        imageView.setLayoutY(to.getY());
    }

    public void backward(Vector2 to) {
        imageView.setLayoutX(to.getX());
        imageView.setLayoutY(to.getY());
    }
}
