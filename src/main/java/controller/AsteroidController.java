package controller;

import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import model.Asteroid;
import view.AsteroidView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AsteroidController {
    Map<Asteroid, AsteroidView> asteroids = new HashMap<>();

    @SneakyThrows
    public ImageView spawnAsteroid(Asteroid asteroid, ImageLoader imageLoader, double x, double y) {
        Image image = imageLoader.loadFromResources("asteroid.png", asteroid.getHealth(), asteroid.getHealth());

        Random random = new Random();
        if(random.nextBoolean()) {
            x = random.nextBoolean() ? 0 - image.getWidth() : x + image.getWidth();
            y = random.nextInt((int) y);
        } else {
            y = random.nextBoolean() ? 0 - image.getHeight(): y + image.getHeight();
            x = random.nextInt((int) x);
        }

        asteroids.put(asteroid, new AsteroidView(image, x, y));
        return asteroids.get(asteroid).getImageView();
    }

    public void updatePositions(Double secondsSinceLastFrame) {
        asteroids.forEach((a, v) -> {
            Vector2 movementVector = Vector2.vectorFromModule((float) (1/a.getHealth() * 10000 * secondsSinceLastFrame), (float) (Math.toRadians(v.getRotate())));
            Vector2 from = Vector2.vector((float) v.getLayoutX(), (float) v.getLayoutY());
            Vector2 to = from.add(movementVector);
            v.move(to);
            a.move(to);
        });
    }

    public List<Asteroid> getAsteroids() {
        return asteroids.keySet().stream().toList();
    }
}
