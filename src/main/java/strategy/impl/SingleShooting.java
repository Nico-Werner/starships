package strategy.impl;

import controller.BulletController;
import javafx.scene.shape.Circle;
import lombok.Getter;
import model.Bullet;
import observer.BulletObserver;
import player.Player;
import strategy.ShootingStrategy;

import java.util.List;

@Getter
public class SingleShooting implements ShootingStrategy {

    private double cooldown = 500;
    private double lastShot;

    // TODO: ver forma de sacar el shooter y que se asigne despues, que esto solo cree las bullets.
    @Override
    public List<Bullet> shoot(BulletObserver shooter, double x, double y, double angle) {
        double currentTime = System.currentTimeMillis();
        if(currentTime - lastShot < cooldown) return List.of();
        lastShot = currentTime;
        Circle shape = new Circle(5);
        shape.setLayoutX(x);
        shape.setLayoutY(y);
        shape.setRotate(angle - 90);
        // bulletController.addBullet(new Bullet(shape, 300.0, 50, shooter));
        return List.of(new Bullet(shape, 300.0, 50, shooter));
    }

    @Override
    public void setCooldown(double cooldown) {
        this.cooldown = cooldown;
    }
}
