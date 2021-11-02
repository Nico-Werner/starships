package strategy;

import controller.BulletController;

public interface ShootingStrategy {
    void shoot(BulletController bulletController, double x, double y, double angle);
}
