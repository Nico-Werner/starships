package model;

import collider.MyCollider;
import dto.ShipDTO;
import edu.austral.dissis.starships.vector.Vector2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import observer.BulletObserver;
import strategy.ShootingStrategy;
import visitor.GameObjectVisitor;

import java.util.List;

// TODO: por composicion separar la logica del modelo
// TODO: modelo inmutable
@Data
@Builder
@AllArgsConstructor
public class Ship implements GameObject {
    private Double health;
    private Double maxHealth;
    private ShootingStrategy shootingStrategy;

    private Vector2 position;
    private double direction;
    private double speed;

    @Builder.Default
    private String name = "starship.gif";

    public Ship(Double health, ShootingStrategy shootingStrategy, Vector2 position, double speed) {
        this.health = health;
        this.maxHealth = health;
        this.shootingStrategy = shootingStrategy;
        this.position = position;
        direction = 0;
        this.speed = speed;
    }

    public List<Bullet> fire() {
        return shootingStrategy.shoot(position.getX() + 50 , position.getY() + 50, direction);
    }

    public void handleCollisionWith(MyCollider collider) {
        collider.getGameObject().handleCollisionWith(this);
    }

    public void move(Vector2 to) {
        position = to;
    }

    public void heal(int amount) {
        health += amount;
        if(health > maxHealth) health = maxHealth;
    }

    public void handleCollisionWith(Bullet bullet) {
        if (bullet.getObservers() == null || !bullet.getObservers().get(0).isSelf(this)) {
            health -= bullet.getDamage() / 10;

            bullet.setSpeed(0);
            if(bullet.getObservers() != null) {
                for (BulletObserver observer : bullet.getObservers()) {
                    observer.onHit(bullet.getDamage(), this);
                }
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
                .posX(position.getX())
                .posY(position.getY())
                .angle(direction)
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
        visitor.visitShip(this);
    }

    public void reset() {
        health = maxHealth;
    }
}
