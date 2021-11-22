package controller;

import collider.CollideableObject;
import collider.ColliderManager;
import collider.MyCollider;
import edu.austral.dissis.starships.collision.CollisionEngine;
import edu.austral.dissis.starships.game.KeyTracker;
import edu.austral.dissis.starships.vector.Vector2;
import factory.AsteroidFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Asteroid;
import model.GameObject;
import player.Player;
import view.ImageRenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class GameController {
    Player[] players;
    AsteroidController asteroidController;
    PickupController pickupController;
    KeyTracker keyTracker;
    Pane pane;
    AsteroidFactory asteroidFactory = new AsteroidFactory();
    CollisionEngine collisionEngine = new CollisionEngine();
    ColliderManager colliderManager = new ColliderManager();
    ImageRenderer imageRenderer;

    public GameController(Player[] players, KeyTracker keyTracker, Pane pane, AsteroidController asteroidController, PickupController pickupController) {
        this.players = players;
        this.keyTracker = keyTracker;
        this.pane = pane;
        this.asteroidController = asteroidController;
        this.pickupController = pickupController;
        this.imageRenderer = new ImageRenderer(pane);
    }

    public void update(double secondsSinceLastFrame) {
        pane.requestFocus();

        reviveDeadPlayers();

        List<GameObject> gameObjects = new ArrayList<>();
        gameObjects.addAll(Arrays.stream(players).map(Player::getShipController).map(ShipController::getBulletController).map(BulletController::getBullets).flatMap(Collection::stream).collect(Collectors.toList()));
        gameObjects.addAll(asteroidController.getAsteroids());
        gameObjects.addAll(pickupController.getPickups());

        updatePosition(gameObjects, secondsSinceLastFrame);

        gameObjects.addAll(Arrays.stream(players).map(Player::getShipController).map(ShipController::getShip).collect(Collectors.toList()));

        List<CollideableObject> collideableObjects = colliderManager.generateColliders(gameObjects);

        collisionEngine.checkCollisions(collideableObjects.stream().map(e -> (MyCollider) e).collect(Collectors.toList()));

        imageRenderer.renderObjects(collideableObjects.stream().map(CollideableObject::getGameObject).collect(Collectors.toList()));

        updateHealths();
        updateDeaths();
        spawnAsteroid();
        spawnPickup();


    }

    private void reviveDeadPlayers() {
        for (Player player : players) {
            if (player.getLives() > 0 && player.getShipController().getShip().getHealth() <= 0) {
                player.revive();
            }
        }
    }

    private StackPane createGameOverRectangle() {
        Rectangle rectangle = new Rectangle(600, 200);
        rectangle.setFill(Color.BLACK);
        rectangle.setOpacity(0.4);
        rectangle.setLayoutX(150);
        rectangle.setLayoutY(150);

        Text text = new Text("GAME OVER");
        text.setFont(Font.font(100));
        text.setFill(Color.WHITE);
        text.setX(150);
        text.setY(150);

        StackPane stackPane = new StackPane(rectangle, text);
        stackPane.setPrefSize(600, 200);

        return stackPane;
    }

    private void updateHealths() {
        for (Player player : players) {
            player.getShipController().updateHealth();
        }
    }

    private void spawnPickup() {
        if(Math.random() * 1000 < 3 && pickupController.getPickups().size() < 5) {
            pickupController.spawnPickup(pane.getWidth(), pane.getHeight());
        }
    }

    private void spawnAsteroid() {
        if(Math.random() * 100.0 < 5) {
            Asteroid asteroid = asteroidFactory.createAsteroid(pane.getWidth(), pane.getHeight());
            asteroidController.spawnAsteroid(asteroid);
        }
    }

    private void updatePosition(List<GameObject> gameObjects, Double secondsSinceLastFrame) {
        for(Player player : players) {
            player.updateInput(pane, keyTracker, secondsSinceLastFrame);
        }
        for (GameObject gameObject : gameObjects) {
            Vector2 movementVector = Vector2.vectorFromModule((gameObject.getSpeed() * secondsSinceLastFrame), gameObject.getDirection());
            Vector2 from = gameObject.getPosition();
            Vector2 to = from.add(movementVector);
            gameObject.move(to);
        }
    }

    private void updateDeaths() {
        for(Player player : players) {
            if(player.isShipDead()) {
                 player.updateDeath();
            }
            player.getShipController().getBulletController().removeDeadBullets(pane.getWidth(), pane.getHeight());
        }
        asteroidController.killOutOfBounds(pane.getWidth(), pane.getHeight());
        asteroidController.updateDeaths();
        pickupController.getInactive();
    }

    public boolean isOver() {
        return Arrays.stream(players).allMatch(Player::isDead);
    }

    public void gameOver() {
        StackPane gameOverRectangle = createGameOverRectangle();
        gameOverRectangle.translateXProperty().setValue(pane.getWidth() / 2 - gameOverRectangle.getPrefWidth() / 2);
        gameOverRectangle.translateYProperty().setValue(pane.getHeight() / 2 - gameOverRectangle.getPrefHeight() / 2);
        pane.getChildren().add(gameOverRectangle);
    }
}