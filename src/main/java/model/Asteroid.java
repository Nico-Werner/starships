package model;

import collider.MyCollider;
import dto.AsteroidDTO;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@AllArgsConstructor
@Data
public class Asteroid implements MyCollider, Serializable {
    Double health;
    Shape shape;
    double speed;

    @Override
    public void handleCollisionWith(MyCollider collider) {
        collider.handleCollisionWith(this);
    }

    public void move(Vector2 to) {
        ((Circle) shape).setCenterX(to.getX() + health/2);
        ((Circle) shape).setCenterY(to.getY() + health/2);
    }

    @Override
    public void handleCollisionWith(Bullet bullet) {
        health = health - bullet.getDamage();
    }

    public AsteroidDTO toDTO() {
        return AsteroidDTO.builder()
                .health(health)
                .centerX(((Circle) shape).getCenterX())
                .centerY(((Circle) shape).getCenterY())
                .rotate(shape.getRotate())
                .radius(((Circle) shape).getRadius())
                .speed(speed)
                .build();
    }
}
