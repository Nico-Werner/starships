package dto;

import javafx.scene.shape.Circle;
import lombok.Builder;
import lombok.Data;
import model.Asteroid;

import java.io.Serializable;


@Data
@Builder
public class AsteroidDTO implements Serializable {
    private double health;
    private double speed;
    private double centerX;
    private double centerY;
    private double rotate;
    private double radius;

    public Asteroid toAsteroid() {
        Circle shape = new Circle(radius);
        shape.setCenterX(centerX);
        shape.setCenterY(centerY);
        shape.setRotate(rotate);
        return new Asteroid(health, shape, speed);
    }
}
