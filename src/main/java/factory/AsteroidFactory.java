package factory;

import edu.austral.dissis.starships.vector.Vector2;
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
        Random random = new Random();
        if(random.nextBoolean()) {
            x = random.nextBoolean() ? 0 - health : x + health;
            y = random.nextInt((int) y);
        } else {
            y = random.nextBoolean() ? 0 - health: y + health;
            x = random.nextInt((int) x);
        }
        return new Asteroid(health, Vector2.vector(x, y), random.nextInt(360), 10000 / health);
    }
}
