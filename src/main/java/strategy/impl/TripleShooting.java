package strategy.impl;

import controller.BulletController;
import javafx.scene.shape.Circle;
import model.Bullet;
import strategy.ShootingStrategy;

public class TripleShooting implements ShootingStrategy {
    private double cooldown = 300;
    private double lastShot;

    @Override
    public void shoot(BulletController bulletController, double x, double y, double angle) {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastShot < cooldown) return;
        lastShot = currentTime;
        bulletController.addBullet(createBullet(x, y, angle));
        bulletController.addBullet(createBullet(x, y, angle - 15));
        bulletController.addBullet(createBullet(x, y, angle + 15));
    }

    private Bullet createBullet(double x, double y, double angle) {
        Circle shape = new Circle(5);
        shape.setLayoutX(x);
        shape.setLayoutY(y);
        shape.setRotate(angle - 90);
        return new Bullet(shape, 300.0, 50);
    }
}
