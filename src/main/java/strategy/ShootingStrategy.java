package strategy;

import controller.BulletController;
import player.Player;

public interface ShootingStrategy {
    void shoot(Player shooter, BulletController bulletController, double x, double y, double angle);

    void setCooldown(double cooldown);

    double getCooldown();
}
