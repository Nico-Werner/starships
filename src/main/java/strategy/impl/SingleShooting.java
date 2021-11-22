package strategy.impl;

import edu.austral.dissis.starships.vector.Vector2;
import lombok.Getter;
import model.Bullet;
import strategy.ShootingStrategy;

import java.util.List;

@Getter
public class SingleShooting implements ShootingStrategy {

    private double cooldown = 500;
    private double lastShot;

    @Override
    public List<Bullet> shoot(double x, double y, double angle) {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastShot < cooldown) return List.of();
        lastShot = currentTime;
        return List.of(new Bullet(Vector2.vector(x, y), Math.toRadians(angle - 90), 300.0, 50));
    }

    @Override
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}
