package model;

import collider.MyCollider;
import dto.BulletDTO;
import edu.austral.dissis.starships.vector.Vector2;
import lombok.Data;
import observer.BulletObserver;
import observer.Observable;
import visitor.GameObjectVisitor;

import java.util.ArrayList;
import java.util.List;

@Data
public class Bullet implements GameObject, Observable {

    Vector2 position;
    double direction;
    double speed;
    double damage;

    List<BulletObserver> observers = new ArrayList<>();

    public Bullet(Vector2 position, double direction, double speed, double damage) {
        this.position = position;
        this.direction = direction;
        this.speed = speed;
        this.damage = damage;
    }

    public void handleCollisionWith(MyCollider collider) {
        // double dispatch
        collider.getGameObject().handleCollisionWith(this);
    }

    public void move(Vector2 to) {
        position = to;
    }

    @Override
    public void handleCollisionWith(Asteroid asteroid) {
        speed = 0;
        if(observers != null) {
            for (BulletObserver observer : observers) {
                observer.onHit(damage, asteroid);
            }
        }
    }

    public BulletDTO toDTO() {
        return BulletDTO.builder()
                .speed(speed)
                .damage(damage)
                .posX(position.getX())
                .posY(position.getY())
                .rotate(direction)
                .radius(5)
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
        return speed == 0;
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visitBullet(this);
    }

    @Override
    public void register(BulletObserver observer) {
        observers.add(observer);
    }

    @Override
    public void unregister(BulletObserver observer) {
        observers.remove(observer);
    }
}
