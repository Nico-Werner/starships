package strategy.impl;

import edu.austral.dissis.starships.vector.Vector2;
import model.Bullet;
import strategy.ShootingStrategy;

import java.util.List;

public class TripleShooting implements ShootingStrategy {

    private double cooldown = 300;
    private double lastShot;

    @Override
    public List<Bullet> shoot(double x, double y, double angle) {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastShot < cooldown) return List.of();
        lastShot = currentTime;
//        bulletController.addBullet(createBullet(x, y, angle, shooter));
//        bulletController.addBullet(createBullet(x, y, angle - 15, shooter));
//        bulletController.addBullet(createBullet(x, y, angle + 15, shooter));
        return List.of(
                createBullet(x, y, angle),
                createBullet(x, y, angle - 10),
                createBullet(x, y, angle + 10)
        );
    }

    @Override
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public double getCooldown() {
        return cooldown;
    }

    private Bullet createBullet(double x, double y, double angle) {
        return new Bullet(Vector2.vector(x, y), Math.toRadians(angle - 90), 300.0, 50);
    }
}
