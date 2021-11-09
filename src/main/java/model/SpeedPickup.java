package model;

import collider.MyCollider;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class SpeedPickup implements Pickup {

    boolean active = true;
    Shape shape = new Rectangle(50, 50);

    public SpeedPickup(int xPos, int yPos) {
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
            final Ship ship = (Ship) collider;
            ship.setSpeed(ship.getSpeed() * 2);
            active = false;

            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ship.setSpeed(ship.getSpeed() / 2);
                }
            } , 10000);
        }
    }
}
