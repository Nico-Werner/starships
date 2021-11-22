package dto;

import edu.austral.dissis.starships.vector.Vector2;
import lombok.Builder;
import lombok.Data;
import model.Ship;
import strategy.ShootingStrategy;
import strategy.impl.SingleShooting;

import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

@Data
@Builder
public class ShipDTO implements Serializable {
    private double health;
    private double maxHealth;
    private double posX;
    private double posY;
    private double speed;
    private double angle;
    private ShootingStrategy shootingStrategy;

    public Ship toShip() {
        Ship ship = Ship.builder()
                .health(health)
                .maxHealth(maxHealth)
                .position(Vector2.vector(posX, posY))
                .direction(angle)
                .speed(speed)
                .shootingStrategy(shootingStrategy)
                .build();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                ship.setShootingStrategy(new SingleShooting());
            }
        } , 5000);

        return ship;
    }
}
