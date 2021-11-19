package strategy;

import controller.BulletController;
import model.Bullet;
import observer.BulletObserver;
import player.Player;

import java.io.Serializable;
import java.util.List;

public interface ShootingStrategy extends Serializable {

    List<Bullet> shoot(BulletObserver shooter, double x, double y, double angle);

    void setCooldown(double cooldown);

    double getCooldown();
}
