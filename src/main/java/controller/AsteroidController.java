package controller;

import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.vector.Vector2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.SneakyThrows;
import model.Asteroid;
import view.AsteroidView;

import java.util.*;

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
        asteroid.getShape().setRotate(asteroids.get(asteroid).getRotate());
        return asteroids.get(asteroid).getImageView();
    }

    public void updatePositions(Double secondsSinceLastFrame) {
        asteroids.forEach((a, v) -> {
            Vector2 movementVector = Vector2.vectorFromModule((1/a.getHealth() * 10000 * secondsSinceLastFrame), (Math.toRadians(v.getRotate())));
            Vector2 from = Vector2.vector(v.getLayoutX(), v.getLayoutY());
            Vector2 to = from.add(movementVector);
            v.move(to);
            a.move(to);
        });
    }

    public List<Asteroid> getAsteroids() {
        return asteroids.keySet().stream().toList();
    }

    public List<ImageView> updateDeaths() {
        List<ImageView> deaths = new ArrayList<>();

        // remove asteroids that have 0 or less health and add their imageView to the deaths list
        asteroids.forEach((a, v) -> {
            if(a.getHealth() <= 0) {
                deaths.add(v.getImageView());
                asteroids.remove(a);
            }
        });

        return deaths;
    }

    public void killOutOfBounds(double width, double height) {
        // set asteroids health to 0 if asteroidView is outside the screen
        asteroids.forEach((a, v) -> {
            if(v.getLayoutX() < 0 - a.getHealth()*2 || v.getLayoutX() > width + a.getHealth()*2 || v.getLayoutY() < 0 - a.getHealth()*2 || v.getLayoutY() > height + a.getHealth()*2) {
                a.setHealth(0.0);
            }
        });
    }
}
