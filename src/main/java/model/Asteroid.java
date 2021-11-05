package model;

import collider.MyCollider;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class Asteroid implements MyCollider {
    Double health;
    Shape shape;
    double speed;

    @Override
    public void handleCollisionWith(MyCollider collider) {
        if(collider.getClass().equals(Ship.class)) {
            Ship ship = (Ship) collider;
            ship.setHealth(ship.getHealth() - health/2);
            health = 0.0;
        } else if(collider.getClass().equals(Bullet.class)) {
            Bullet bullet = (Bullet) collider;
            health = health - bullet.getDamage();
        }
    }

    public void move(Vector2 to) {
        ((Circle) shape).setCenterX(to.getX() + health/2);
        ((Circle) shape).setCenterY(to.getY() + health/2);
    }
}
