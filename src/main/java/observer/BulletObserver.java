package observer;

import model.Asteroid;
import model.Ship;

public interface BulletObserver {
    void onHit(double damage, Asteroid asteroid);
    void onHit(double damage, Ship ship);
    boolean isSelf(Ship ship);
}
