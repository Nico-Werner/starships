package visitor;

import model.Asteroid;
import model.Bullet;
import model.Pickup;
import model.Ship;

public interface GameObjectVisitor {
    void visitAsteroid(Asteroid asteroid);
    void visitShip(Ship ship);
    void visitBullet(Bullet bullet);
    void visitHealthPickup(Pickup pickup);
    void visitSpeedPickup(Pickup pickup);
    void visitRapidFirePickup(Pickup pickup);
    void visitTripleShootingPickup(Pickup pickup);
}
