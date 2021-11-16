package model;

import collider.MyCollider;
import dto.PickupDTO;
import dto.PickupType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import strategy.impl.SingleShooting;
import strategy.impl.TripleShooting;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class TripleShootingPickup implements Pickup {
    @Setter
    private boolean active = true;

    private final Shape shape = new Rectangle(50, 50);
    private final PickupType type = PickupType.TRIPLE_SHOOTING;

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
                    SingleShooting singleShooting = new SingleShooting();
                    singleShooting.setCooldown(ship.getShootingStrategy().getCooldown());
                    ship.setShootingStrategy(singleShooting);
                }
            } , 10000);
    }

    @Override
    public PickupDTO toDTO() {
        return PickupDTO.builder()
                .xPos(shape.getLayoutX())
                .yPos(shape.getLayoutY())
                .type(type)
                .build();
    }
}
