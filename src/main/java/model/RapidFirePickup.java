package model;

import collider.MyCollider;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import strategy.impl.SingleShooting;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class RapidFirePickup implements Pickup {
    boolean active = true;
    Shape shape = new Rectangle(50, 50);

    public RapidFirePickup(int xPos, int yPos) {
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
        if(!active) return;
        if (collider instanceof Ship) {
            ((Ship) collider).getShootingStrategy().setCooldown(50);
            active = false;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ((Ship) collider).getShootingStrategy().setCooldown(500);
                }
            } , 10000);
        }
    }
}
