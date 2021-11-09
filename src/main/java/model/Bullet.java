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
        if(collider instanceof Asteroid) {
            speed = 0;
            shooter.addPoints(damage);
        }
    }

    public void move(Vector2 to) {
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }
}
