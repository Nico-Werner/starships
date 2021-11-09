package model;

import collider.MyCollider;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import strategy.impl.SingleShooting;
import strategy.impl.TripleShooting;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class TripleShootingPickup implements Pickup {
    boolean active = true;
    Shape shape = new Rectangle(50, 50);

    public TripleShootingPickup(int xPos, int yPos) {
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
        TripleShooting tripleShooting = new TripleShooting();
        tripleShooting.setCooldown(ship.getShootingStrategy().getCooldown());
        ship.setShootingStrategy(tripleShooting);
        active = false;

        new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ship.setShootingStrategy(new SingleShooting());
                }
            } , 10000);
    }
}
