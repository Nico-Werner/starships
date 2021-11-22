package model;

import collider.MyCollider;
import dto.PickupDTO;
import dto.PickupType;
import edu.austral.dissis.starships.vector.Vector2;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import visitor.GameObjectVisitor;

import java.util.Timer;
import java.util.TimerTask;

@Getter
public class RapidFirePickup implements Pickup {
    @Setter
    private boolean active = true;

    private final Vector2 position;
    private final PickupType type = PickupType.RAPID_FIRE;

    public RapidFirePickup(int xPos, int yPos) {
        position = Vector2.vector(xPos, yPos);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                active = false;
            }
        } , 10000);
    }

    @Override
    public void handleCollisionWith(@NotNull MyCollider collider) {
        collider.getGameObject().handleCollisionWith(this);
    }


    @Override
    public void handleCollisionWith(Ship ship) {
        if(!active) return;
        ship.getShootingStrategy().setCooldown(ship.getShootingStrategy().getCooldown()/5);
        active = false;

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ship.getShootingStrategy().setCooldown(ship.getShootingStrategy().getCooldown()*5);
            }
        } , 10000);
    }

    @Override
    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void accept(GameObjectVisitor visitor) {
        visitor.visitRapidFirePickup(this);
    }

    @Override
    public PickupDTO toDTO() {
        return PickupDTO.builder()
                .xPos(position.getX())
                .yPos(position.getY())
                .type(type)
                .build();
    }
}
