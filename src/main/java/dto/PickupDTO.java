package dto;

import lombok.Builder;
import lombok.Data;
import model.*;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

@Data
@Builder
public class PickupDTO implements Serializable {
    private double xPos;
    private double yPos;
    private PickupType type;

    public Pickup toPickup() {
        Pickup pickup;

        switch (type) {
            case SPEED -> pickup = new SpeedPickup((int) xPos, (int) yPos);
            case HEALTH -> pickup = new HealthPickup((int) xPos, (int) yPos);
            case TRIPLE_SHOOTING -> pickup = new TripleShootingPickup((int) xPos, (int) yPos);
            case RAPID_FIRE -> pickup = new RapidFirePickup((int) xPos, (int) yPos);
            default -> pickup = null;
        }
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(pickup != null) {
                    pickup.setActive(false);
                }
            }
        }, 5000);
        return pickup;
    }
}
