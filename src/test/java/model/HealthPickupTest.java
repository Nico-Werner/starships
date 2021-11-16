package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HealthPickupTest {

    private final Ship ship = new Ship(100.0, null,null,1);
    private final Pickup pickup = new HealthPickup(0, 0);

    @Test
    void handleCollisionWithShip() {
        ship.setHealth(50.0);
        pickup.handleCollisionWith(ship);
        assertEquals(100, ship.getHealth());
    }
}