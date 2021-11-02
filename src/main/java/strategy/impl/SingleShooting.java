package strategy.impl;

import controller.BulletController;
import javafx.scene.shape.Circle;
import model.Bullet;
import strategy.ShootingStrategy;

public class SingleShooting implements ShootingStrategy {

    private double cooldown = 500;
    private double lastShot;

    @Override
    public void shoot(BulletController bulletController, double x, double y, double angle) {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastShot < cooldown) return;
        lastShot = currentTime;
        Circle shape = new Circle(5);
        shape.setLayoutX(x);
        shape.setLayoutY(y);
        shape.setRotate(angle - 90);
        bulletController.addBullet(new Bullet(shape, 300.0, 50));
    }
}
