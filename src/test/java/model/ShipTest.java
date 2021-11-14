package model;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.Test;
import strategy.impl.SingleShooting;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {

    Ship ship = new Ship(100.0, new SingleShooting(), new Rectangle(100, 70), 100);

    @Test
    void move() {
        ship.move(Vector2.vector(10, 10));
        assertEquals(10, ship.getShape().getLayoutX());
        assertEquals(10, ship.getShape().getLayoutY());
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
        ship.handleCollisionWith(new Asteroid(100.0, new Circle(100), 100));
        assertEquals(50, ship.getHealth());
    }

    @Test
    void testHandleCollisionWithBullet() {
        assertEquals(100, ship.getHealth());
        ship.handleCollisionWith(new Bullet(new Circle(100), 100, 100 , null));
        assertEquals(90, ship.getHealth());
    }
}