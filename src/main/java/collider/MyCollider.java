package collider;

import edu.austral.dissis.starships.collision.Collider;
import javafx.scene.shape.Shape;
import model.Asteroid;
import model.Bullet;
import model.Pickup;
import model.Ship;

public interface MyCollider extends Collider<MyCollider> {

    void handleCollisionWith(Asteroid asteroid);

    void handleCollisionWith(Ship ship);

    void handleCollisionWith(Bullet bullet);

    void handleCollisionWith(Pickup pickup);

}
