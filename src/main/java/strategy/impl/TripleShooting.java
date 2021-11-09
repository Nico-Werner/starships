package strategy.impl;

import controller.BulletController;
import javafx.scene.shape.Circle;
import model.Bullet;
import player.Player;
import strategy.ShootingStrategy;

public class TripleShooting implements ShootingStrategy {

    private double cooldown = 300;
    private double lastShot;

    @Override
    public void shoot(Player shooter, BulletController bulletController, double x, double y, double angle) {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastShot < cooldown) return;
        lastShot = currentTime;
        bulletController.addBullet(createBullet(x, y, angle, shooter));
        bulletController.addBullet(createBullet(x, y, angle - 15, shooter));
        bulletController.addBullet(createBullet(x, y, angle + 15, shooter));
    }

    @Override
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }

    @Override
    public double getCooldown() {
        return cooldown;
    }

    private Bullet createBullet(double x, double y, double angle, Player shooter) {
        Circle shape = new Circle(5);
        shape.setLayoutX(x);
        shape.setLayoutY(y);
        shape.setRotate(angle - 90);
        return new Bullet(shape, 300.0, 50, shooter);
    }
}
