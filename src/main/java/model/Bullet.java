package model;

import collider.MyCollider;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
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
        shooter.addPoints(damage);
    }

    @Override
    public void handleCollisionWith(Ship ship) {

    }

    @Override
    public void handleCollisionWith(Bullet bullet) {

    }

    @Override
    public void handleCollisionWith(Pickup pickup) {

    }
}
