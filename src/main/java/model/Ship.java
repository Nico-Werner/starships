package model;

import collider.MyCollider;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import strategy.ShootingStrategy;

@AllArgsConstructor
@Data
public class Ship implements MyCollider {
    private Double health;
    private ShootingStrategy shootingStrategy;
    private Shape shape;

    public void fire(Vector2 from) {
        shootingStrategy.shoot(from);
    }

    @Override
    public void handleCollisionWith(MyCollider collider) {

    }

    public void move(Vector2 to) {
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }
}
