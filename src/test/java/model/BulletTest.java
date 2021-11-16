package model;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulletTest {

    final Bullet bullet = new Bullet(new Circle(10), 10, 10, null);
    final Asteroid asteroid = new Asteroid(100.0, new Circle(100), 10);

    @Test
    void move() {
        bullet.move(Vector2.vector(1,1));
        assertEquals(1, bullet.getShape().getLayoutX());
        assertEquals(1, bullet.getShape().getLayoutY());
    }

    @Test
    void handleCollisionWithAsteroid() {
        assertEquals(10, bullet.getSpeed());
        bullet.handleCollisionWith(asteroid);
        assertEquals(0, bullet.getSpeed()); // bullet speed is 0 after collision as a mark to be removed
    }
}