package dto;

import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Builder;
import lombok.Data;
import model.Ship;
import strategy.ShootingStrategy;

import java.io.Serializable;

@Data
@Builder
public class ShipDTO implements Serializable {
    private double health;
    private double posX;
    private double posY;
    private double speed;
    private double angle;
    private ShootingStrategy shootingStrategy;

    public Ship toShip() {
        Shape shape = new Rectangle(70, 45);
        shape.setLayoutX(posX);
        shape.setLayoutY(posY);
        shape.setRotate(angle);
        return Ship.builder()
                .health(health)
                .shape(shape)
                .speed(speed)
                .shootingStrategy(shootingStrategy)
                .build();
    }
}
