package model;

import collider.MyCollider;
import controller.BulletController;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import strategy.ShootingStrategy;

@AllArgsConstructor
@Data
public class Ship implements MyCollider {
    private Double health;
    private ShootingStrategy shootingStrategy;
    private Shape shape;

    public void fire(BulletController bulletController) {
        shootingStrategy.shoot(bulletController, shape.getLayoutX() + ((Rectangle) shape).getWidth()/2 , shape.getLayoutY() + ((Rectangle) shape).getHeight()/2, shape.getRotate());
    }

    @Override
    public void handleCollisionWith(MyCollider collider) {

    }

    public void move(Vector2 to) {
        shape.setLayoutX(to.getX() + (100 - ((Rectangle) shape).getWidth())/2);
        shape.setLayoutY(to.getY() + (100 - ((Rectangle) shape).getHeight())/2);
    }
}
