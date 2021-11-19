package factory;

import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.shape.Circle;
import model.Asteroid;

import java.util.Random;

public class AsteroidFactory {
    Random random = null;

    public Asteroid createAsteroid(double width, double height) {
        return createAsteroid((int) (Math.random() * 100), width, height);
    }

    public Asteroid createAsteroid(int seed, double x, double y) {
        if(random == null) random = new Random(seed);
        double health = random.nextInt(300 - 25) + 25.0;
        Asteroid asteroid = new Asteroid(health, new Circle(health / 3), 10000 / health);
        Random random = new Random();
        if(random.nextBoolean()) {
            x = random.nextBoolean() ? 0 - health : x + health;
            y = random.nextInt((int) y);
        } else {
            y = random.nextBoolean() ? 0 - health: y + health;
            x = random.nextInt((int) x);
        }
        asteroid.move(Vector2.vector(x, y));
        return asteroid;
    }
}
