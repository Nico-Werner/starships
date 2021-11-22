package model;

import edu.austral.dissis.starships.vector.Vector2;
import visitor.GameObjectVisitor;

public interface GameObject {
    Vector2 getPosition();
    double getDirection();
    double getSpeed();
    void move(Vector2 to);

    boolean shouldBeRemoved();

    void accept(GameObjectVisitor visitor);
}
