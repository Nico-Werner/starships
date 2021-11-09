package view;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import lombok.Data;

@Data
public class ShipView {
    ImageView imageView;
    Group healthView;
    Rectangle healthFill = new Rectangle(100, 10);
    Text points = new Text();

    public ShipView(Image image, int x, int y) {
        this.imageView = new ImageView(image);
        imageView.setLayoutX(x);
        imageView.setLayoutY(y);
        healthFill.setFill(Color.RED);

        Rectangle rectangle = new Rectangle(100, 10);
        rectangle.setFill(Color.TRANSPARENT);
        rectangle.setStroke(Color.RED);
        rectangle.setStrokeWidth(2);

        healthView = new Group(rectangle, healthFill);
        healthView.setLayoutX(x);
        healthView.setLayoutY(y + 100);

        points.setLayoutX(x);
        points.setLayoutY(y);
        points.setFont(Font.font(20));
        points.setFill(Color.WHITE);
        points.setText("0");
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
        healthView.setLayoutX(to.getX());
        healthView.setLayoutY(to.getY() + 100);
        points.setLayoutX(to.getX());
        points.setLayoutY(to.getY());
    }

    public void updateHealth(Double health) {
        healthFill.setWidth(health/200 * 100);
    }

    public void updatePoints(int points) {
        this.points.setText(String.valueOf(points));
    }
}
