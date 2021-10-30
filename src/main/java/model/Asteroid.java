package model;

import collider.MyCollider;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class Asteroid implements MyCollider {
    Double health;
    Shape shape;

    @Override
    public void handleCollisionWith(MyCollider collider) {
        if(collider.getClass().equals(Ship.class)) {
            Ship ship = (Ship) collider;
            ship.setHealth(ship.getHealth() - health);
        }
    }

    public void move(Vector2 to) {
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }
}
