package model;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsteroidTest {

    Asteroid asteroid = new Asteroid(100.0, new Circle(100), 100);

    @Test
    void move() {
        asteroid.move(Vector2.vector(1,1));
        assertEquals(1 + asteroid.getHealth()/2, ((Circle) asteroid.getShape()).getCenterX());
        assertEquals(1 + asteroid.getHealth()/2, ((Circle) asteroid.getShape()).getCenterY());
    }

    @Test
    void handleCollisionWithBullet() {
        assertEquals(100, asteroid.getHealth());
        asteroid.handleCollisionWith(new Bullet(new Circle(10), 10, 10,null));
        assertEquals(90, asteroid.getHealth());
    }
}