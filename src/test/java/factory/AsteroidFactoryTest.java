package factory;

import model.Asteroid;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AsteroidFactoryTest {

    private final AsteroidFactory asteroidFactory = new AsteroidFactory();

    @Test
    void createAsteroid() {
        Asteroid asteroid = asteroidFactory.createAsteroid(0);
        assertEquals(135, asteroid.getHealth());
        assertEquals(74.07407407407408, asteroid.getSpeed());

        asteroid = asteroidFactory.createAsteroid(0);
        assertEquals(273, asteroid.getHealth());
        assertEquals(36.63003663003663, asteroid.getSpeed());
    }
}