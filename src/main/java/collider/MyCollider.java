package collider;

import edu.austral.dissis.starships.collision.Collider;
import javafx.scene.shape.Shape;
import model.Asteroid;
import model.Bullet;
import model.Pickup;
import model.Ship;

public interface MyCollider extends Collider<MyCollider> {

    default void handleCollisionWith(Asteroid asteroid) {}

    default void handleCollisionWith(Ship ship) {};

    default void handleCollisionWith(Bullet bullet) {};

    default void handleCollisionWith(Pickup pickup) {};

}
