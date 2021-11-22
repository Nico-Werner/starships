package model;

import collider.MyCollider;
import dto.BulletDTO;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import lombok.Data;
import observer.BulletObserver;
import org.jetbrains.annotations.Nullable;
import visitor.GameObjectVisitor;

@Data
public class Bullet implements MyCollider, GameObject {

    Shape shape;
    double speed;
    double damage;

    //TODO usar observer (aca tienen acceso total al player, no está bueno) -> mejor depender de lo mínimo en una interfaz
    // además permite delegar el tema de asignar puntos
    // hacerlo lista. hacerle la interfaz observable (seguir el patron mas al pie)
    @Nullable
    BulletObserver shooter;

    public Bullet(Circle circle, double speed, double damage, @Nullable BulletObserver shooter) {
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
        if(shooter != null) {
            if(asteroid.getHealth() < 0) shooter.updateScore(damage);
            shooter.updateScore(damage);
        }
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

    @Override
    public Vector2 getPosition() {
        return Vector2.vector(shape.getLayoutX(), shape.getLayoutY());
    }

    @Override
    public double getDirection() {
        return shape.getRotate();
    }

    @Override
    public boolean shouldBeRemoved() {
        return speed == 0;
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visitBullet(this);
    }
}
