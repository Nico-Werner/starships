package model;

import collider.MyCollider;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class HealthPickup implements Pickup {
    boolean active = true;
    Shape shape = new Rectangle(50, 50);

    public HealthPickup(int xPos, int yPos) {
        shape.setLayoutX(xPos);
        shape.setLayoutY(yPos);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                active = false;
            }
        } , 10000);
    }

    @Override
    public void handleCollisionWith(@NotNull MyCollider collider) {
        collider.handleCollisionWith(this);
    }

    @Override
    public void handleCollisionWith(Ship ship) {
        if(!active) return;

        if(ship.getHealth() >= 150) ship.setHealth(200.0);
        else ship.heal(50);
        active = false;
    }
}
