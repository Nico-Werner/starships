package model;

import org.junit.jupiter.api.Test;
import strategy.impl.SingleShooting;

import static org.junit.jupiter.api.Assertions.*;

class RapidFirePickupTest {

    Ship ship = new Ship(100.0, new SingleShooting(), null, 10);
    Pickup rapidFirePickup = new RapidFirePickup(0, 0);

    @Test
    void handleCollisionWithShip() {
        assertEquals(500, ship.getShootingStrategy().getCooldown());
        rapidFirePickup.handleCollisionWith(ship);
        assertEquals(50, ship.getShootingStrategy().getCooldown());
    }
}