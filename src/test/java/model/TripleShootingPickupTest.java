package model;

import org.junit.jupiter.api.Test;
import strategy.impl.SingleShooting;
import strategy.impl.TripleShooting;

import static org.junit.jupiter.api.Assertions.*;

class TripleShootingPickupTest {

    Ship ship = new Ship(100.0, new SingleShooting(), null, 100);
    Pickup pickup = new TripleShootingPickup(0, 0);

    @Test
    void handleCollisionWithShip() {
        pickup.handleCollisionWith(ship);
        assertEquals(TripleShooting.class, ship.getShootingStrategy().getClass());
    }
}