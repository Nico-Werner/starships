package observer;

import model.Ship;

public interface BulletObserver {
    void updateScore(double points);
    boolean isSelf(Ship ship);
}
