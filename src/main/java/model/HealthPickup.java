package model;

import collider.MyCollider;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class HealthPickup implements Pickup {
    boolean active = true;
    Shape shape = new Rectangle(50, 50);

    public HealthPickup(int xPos, int yPos) {
        shape.setLayoutX(xPos);
        shape.setLayoutY(yPos);
    }

    @Override
    public void handleCollisionWith(@NotNull MyCollider collider) {
        if(!active) return;
        if(collider instanceof Ship) {
            ((Ship) collider).heal(50);
            active = false;
        }
    }
}
