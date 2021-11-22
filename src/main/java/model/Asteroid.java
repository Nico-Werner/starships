package model;

import collider.MyCollider;
import dto.AsteroidDTO;
import edu.austral.dissis.starships.vector.Vector2;
import lombok.AllArgsConstructor;
import lombok.Data;
import visitor.GameObjectVisitor;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Asteroid implements Serializable, GameObject {
    Double health;
    Vector2 position;
    double direction;
    double speed;

    public void handleCollisionWith(MyCollider collider) {
        collider.getGameObject().handleCollisionWith(this);
    }

    public void move(Vector2 to) {
        position = to;
    }

    public void handleCollisionWith(Bullet bullet) {
        health = health - bullet.getDamage();
    }

    public AsteroidDTO toDTO() {
        return AsteroidDTO.builder()
                .health(health)
                .posX(position.getX())
                .posY(position.getY())
                .rotate(direction)
                .size(health)
                .speed(speed)
                .build();
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public double getDirection() {
        return direction;
    }

    @Override
    public boolean shouldBeRemoved() {
        return health <= 0;
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visitAsteroid(this);
    }
}
