package model;

import collider.MyCollider;
import controller.BulletController;
import dto.ShipDTO;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import player.Player;
import strategy.ShootingStrategy;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class Ship implements MyCollider {
    private Double health;
    private Double maxHealth;
    private ShootingStrategy shootingStrategy;
    private Shape shape;
    private double speed;

    public Ship(Double health, ShootingStrategy shootingStrategy, Shape shape, double speed) {
        this.health = health;
        this.maxHealth = health;
        this.shootingStrategy = shootingStrategy;
        this.shape = shape;
        this.speed = speed;
    }

    public List<Bullet> fire(Player shooter) {
        return shootingStrategy.shoot(shooter, shape.getLayoutX() + ((Rectangle) shape).getWidth()/2 , shape.getLayoutY() + ((Rectangle) shape).getHeight()/2, shape.getRotate());
    }

    @Override
    public void handleCollisionWith(MyCollider collider) {
        collider.handleCollisionWith(this);
    }

    public void move(Vector2 to) {
        shape.setLayoutX(to.getX());
        shape.setLayoutY(to.getY());
    }

    public void heal(int amount) {
        health += amount;
        if(health > maxHealth) health = maxHealth;
    }

    @Override
    public void handleCollisionWith(Bullet bullet) {
        if (bullet.getShooter() == null || !bullet.getShooter().isSelf(this)) {
            health -= bullet.getDamage() / 10;

            bullet.setSpeed(0);
            if(bullet.getShooter() != null) {
                if (health < 0) bullet.getShooter().updateScore(bullet.getDamage());
                bullet.getShooter().updateScore(bullet.getDamage() / 10);
            }
        }
    }

    @Override
    public void handleCollisionWith(Asteroid asteroid) {
        health -= asteroid.getHealth()/2;
        asteroid.setHealth(0.0);
    }

    public ShipDTO toDTO() {
        return ShipDTO.builder()
                .health(health)
                .maxHealth(maxHealth)
                .shootingStrategy(shootingStrategy)
                .speed(speed)
                .posX(shape.getLayoutX())
                .posY(shape.getLayoutY())
                .angle(shape.getRotate())
                .build();
    }
}
