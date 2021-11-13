package model;

import collider.MyCollider;
import dto.BulletDTO;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import lombok.Builder;
import lombok.Data;
import player.Player;

@Data
public class Bullet implements MyCollider {

    Shape shape;
    double speed;
    double damage;
    Player shooter;

    public Bullet(Circle circle, double speed, double damage, Player shooter) {
        shape = circle;
        this.speed = speed;
        this.damage = damage;
        this.shooter = shooter;
    }

    @Override
    public void handleCollisionWith(MyCollider collider) {
        // double dispatch
        collider.handleCollisionWith(this);
    }

    public void move(Vector2 to) {
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }

    @Override
    public void handleCollisionWith(Asteroid asteroid) {
        speed = 0;
        if(asteroid.getHealth() < 0) shooter.addPoints(damage);
        shooter.addPoints(damage);
    }

    public BulletDTO toDTO() {
        return BulletDTO.builder()
                .speed(speed)
                .damage(damage)
                .posX(shape.getLayoutX())
                .posY(shape.getLayoutY())
                .rotate(shape.getRotate())
                .radius(((Circle) shape).getRadius())
                .build();
    }
}
