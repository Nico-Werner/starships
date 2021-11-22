package model;

import collider.MyCollider;
import edu.austral.dissis.starships.vector.Vector2;
import visitor.GameObjectVisitor;

public interface GameObject {
    Vector2 getPosition();
    double getDirection();
    double getSpeed();
    void move(Vector2 to);

    boolean shouldBeRemoved();

    void accept(GameObjectVisitor visitor);

    void handleCollisionWith(MyCollider collider);

    default void handleCollisionWith(Asteroid asteroid) {}
    default void handleCollisionWith(Ship ship) {}
    default void handleCollisionWith(Bullet bullet) {}
    default void handleCollisionWith(Pickup pickup) {}
}
