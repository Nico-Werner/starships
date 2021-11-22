package model;

import edu.austral.dissis.starships.vector.Vector2;
import org.junit.jupiter.api.Test;
import strategy.impl.SingleShooting;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShipTest {

    private final Ship ship = new Ship(100.0, new SingleShooting(), Vector2.vector(0, 0), 100);

    @Test
    void move() {
        ship.move(Vector2.vector(10, 10));
        assertEquals(10, ship.getPosition().getX());
        assertEquals(10, ship.getPosition().getY());
    }

    @Test
    void heal() {
        assertEquals(100, ship.getHealth());
        ship.heal(20);
        assertEquals(100, ship.getHealth()); // ship is not healed as it is at max health
        ship.setHealth(80.0);
        assertEquals(80, ship.getHealth());
        ship.heal(10);
        assertEquals(90, ship.getHealth()); // ship is healed the correct amount
        ship.heal(200);
        assertEquals(100, ship.getHealth()); // ship is healed to max health
    }

    @Test
    void testHandleCollisionWithAsteroid() {
        assertEquals(100, ship.getHealth());
        ship.handleCollisionWith(new Asteroid(100.0, Vector2.vector(0, 0), 0, 100));
        assertEquals(50, ship.getHealth());
    }

    @Test
    void testHandleCollisionWithBullet() {
        assertEquals(100, ship.getHealth());
        ship.handleCollisionWith(new Bullet(Vector2.vector(0, 0), 100, 100 , 10));
        assertEquals(90, ship.getHealth());
    }
}