package strategy;

import controller.BulletController;
import player.Player;

import java.io.Serializable;

public interface ShootingStrategy extends Serializable {
    void shoot(Player shooter, BulletController bulletController, double x, double y, double angle);

    void setCooldown(double cooldown);

    double getCooldown();
}
