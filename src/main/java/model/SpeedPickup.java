package model;

import collider.MyCollider;
import dto.PickupDTO;
import dto.PickupType;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class SpeedPickup implements Pickup {
    @Setter
    private boolean active = true;

    private final Shape shape = new Rectangle(50, 50);
    private final PickupType type = PickupType.SPEED;

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
        collider.handleCollisionWith(this);
    }

    @Override
    public void handleCollisionWith(Ship ship) {
        if(!active) return;
        ship.setSpeed(ship.getSpeed() * 2);
        active = false;

        new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    ship.setSpeed(ship.getSpeed() / 2);
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
