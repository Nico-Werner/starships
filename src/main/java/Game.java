import collider.MyCollider;
import controller.AsteroidController;
import controller.BulletController;
import controller.ShipController;
import edu.austral.dissis.starships.collision.CollisionEngine;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.game.*;
import factory.AsteroidFactory;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.Asteroid;
import model.Bullet;
import model.Ship;
import strategy.impl.SingleShooting;
import view.ShipView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game extends GameApplication {

    @Override
    public WindowSettings setupWindow() {
        return WindowSettings.fromTitle("Starships!");
    }

    @Override
    public Parent initRoot(GameContext context) {
        try {
            return new GameManager(this, context).init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

class GameManager {

    RootSetter rootSetter;
    GameContext context;

    public GameManager(RootSetter rootSetter, GameContext gameContext) {
        this.rootSetter = rootSetter;
        this.context = gameContext;
    }

    boolean isIntro = true;

    Parent init() throws IOException {
        Parent parent = isIntro ? loadIntro() : loadGame();

        parent.setOnMouseClicked(event -> {
            isIntro = !isIntro;
            try {
                rootSetter.setRoot(init());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return parent;
    }

    private void handleMouseClick() throws IOException {
        isIntro = !isIntro;
        rootSetter.setRoot(init());
    }

    private Parent loadIntro() {
        Text title = new Text("Welcome! Click!");

        return new StackPane(title);
    }


    private Parent loadGame() throws IOException {
        ImageLoader imageLoader = new ImageLoader();
        Image image = imageLoader.loadFromResources("starship.gif", 100.0, 100.0);
        ShipView shipView = new ShipView(image, 200, 200);
        Ship ship = new Ship(200.0, new SingleShooting(), new Rectangle(70, 45));
        ShipController shipController = new ShipController(shipView, ship);

        Pane pane = new Pane(shipView.getImageView());

        new MainTimer(shipController, new AsteroidController(), new BulletController(), context.getKeyTracker(), imageLoader, pane).start();

        return pane;
    }

}

class MainTimer extends GameTimer {
    ShipController shipController;
    AsteroidController asteroidController;
    BulletController bulletController;
    KeyTracker keyTracker;
    ImageLoader imageLoader;
    Pane pane;
    AsteroidFactory asteroidFactory = new AsteroidFactory();
    CollisionEngine collisionEngine = new CollisionEngine();

    public MainTimer(ShipController shipController, AsteroidController asteroidController, BulletController bulletController, KeyTracker keyTracker, ImageLoader imageLoader, Pane pane) {
        this.shipController = shipController;
        this.keyTracker = keyTracker;
        this.asteroidController = asteroidController;
        this.imageLoader = imageLoader;
        this.pane = pane;
        this.bulletController = bulletController;
    }

    @Override
    public void nextFrame(double secondsSinceLastFrame) {
        updatePosition(secondsSinceLastFrame);
        updateDeaths();
        spawnAsteroid();
    }

    private void spawnAsteroid() {
        if(Math.random() * 100.0 < 5) {
            Asteroid asteroid = asteroidFactory.createAsteroid();
            ImageView imageView = asteroidController.spawnAsteroid(asteroid, imageLoader, pane.getWidth(), pane.getHeight());
            pane.getChildren().add(imageView);
        }
    }

    private void updatePosition(Double secondsSinceLastFrame) {
        double movement = 100 * secondsSinceLastFrame;

        keyTracker.getKeySet().forEach(keyCode -> {
           switch (keyCode) {
               case UP -> shipController.forward(movement);
               case DOWN -> shipController.backward(movement);
               case LEFT -> shipController.rotateLeft(movement);
               case RIGHT -> shipController.rotateRight(movement);
               case SPACE -> {
                   shipController.fire(bulletController);
                   List<ImageView> imageViews = bulletController.renderBullets(imageLoader);
                   pane.getChildren().addAll(imageViews);
               }
               default -> {
               }
            }

        });

        bulletController.updatePositions(secondsSinceLastFrame);
        asteroidController.updatePositions(secondsSinceLastFrame);

        List<Asteroid> asteroids = asteroidController.getAsteroids();
        List<Bullet> bullets = bulletController.getBullets();
        List<MyCollider> colliders = new ArrayList<>(asteroids);
        colliders.addAll(bullets);
        colliders.add(shipController.getShip());
        collisionEngine.checkCollisions(colliders);
    }

    private void updateDeaths() {
        pane.getChildren().remove(shipController.updateDeath());
        asteroidController.killOutOfBounds(pane.getWidth(), pane.getHeight());
        pane.getChildren().removeAll(asteroidController.updateDeaths());
        pane.getChildren().removeAll(bulletController.removeDeadBullets(pane.getWidth(), pane.getHeight()));
    }
}
