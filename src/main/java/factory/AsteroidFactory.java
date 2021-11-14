package factory;

import javafx.scene.shape.Circle;
import model.Asteroid;

import java.util.Random;

public class AsteroidFactory {
    Random random = null;

    public Asteroid createAsteroid() {
        return createAsteroid((int) (Math.random() * 100));
    }

    public Asteroid createAsteroid(int seed) {
        if(random == null) random = new Random(seed);
        double health = random.nextInt(300 - 25) + 25.0;
        return new Asteroid(health, new Circle(health/3), 10000/health);
    }
}
