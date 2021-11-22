package model;

import collider.MyCollider;
import dto.AsteroidDTO;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import visitor.GameObjectVisitor;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Asteroid implements MyCollider, Serializable, GameObject {
    Double health;
    Shape shape;
    double speed;

    @Override
    public void handleCollisionWith(MyCollider collider) {
        collider.handleCollisionWith(this);
    }

    public void move(Vector2 to) {
//        ((Circle) shape).setCenterX(to.getX() + health/2);
//        ((Circle) shape).setCenterY(to.getY() + health/2);
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }

    @Override
    public void handleCollisionWith(Bullet bullet) {
        health = health - bullet.getDamage();
    }

    public AsteroidDTO toDTO() {
        return AsteroidDTO.builder()
                .health(health)
                .posX(shape.getLayoutX())
                .posY(shape.getLayoutY())
                .rotate(shape.getRotate())
                .size(((Rectangle) shape).getWidth())
                .speed(speed)
                .build();
    }

    @Override
    public Vector2 getPosition() {
        return Vector2.vector(shape.getLayoutX(), shape.getLayoutY());
//        return Vector2.vector(((Circle) shape).getCenterX(), ((Circle) shape).getCenterY());
    }

    @Override
    public double getDirection() {
        return shape.getRotate();
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
