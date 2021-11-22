package strategy;

import model.Bullet;

import java.io.Serializable;
import java.util.List;

public interface ShootingStrategy extends Serializable {

    List<Bullet> shoot(double x, double y, double angle);

    void setCooldown(double cooldown);

    double getCooldown();
}
