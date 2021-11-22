package dto;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lombok.Builder;
import lombok.Data;
import model.Asteroid;

import java.io.Serializable;


@Data
@Builder
public class AsteroidDTO implements Serializable {
    private double health;
    private double speed;
    private double posX;
    private double posY;
    private double rotate;
    private double size;

    public Asteroid toAsteroid() {
        Rectangle shape = new Rectangle(size, size);
        shape.setLayoutX(posX);
        shape.setLayoutY(posY);
        shape.setRotate(rotate);
        return new Asteroid(health, shape, speed);
    }
}
