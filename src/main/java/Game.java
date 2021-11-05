import collider.MyCollider;
import com.sun.javafx.tk.FontLoader;
import controller.AsteroidController;
import controller.BulletController;
import controller.ShipController;
import edu.austral.dissis.starships.collision.CollisionEngine;
import edu.austral.dissis.starships.file.ImageLoader;
import edu.austral.dissis.starships.game.*;
import factory.AsteroidFactory;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import lombok.SneakyThrows;
import model.Asteroid;
import model.Bullet;
import model.Ship;
import strategy.impl.SingleShooting;
import view.ShipView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
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

    MenuBox menu;

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

    private Parent loadIntro() throws IOException {
        Pane pane = new Pane();
        pane.setPrefSize(1920, 1080);

        ImageLoader imageLoader = new ImageLoader();
        Image image = imageLoader.loadFromResources("background.png", 100.0, 100.0);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1920);
        imageView.setFitHeight(1080);

        pane.getChildren().add(imageView);

        MenuItem exit = new MenuItem("EXIT");
        exit.setOnMouseClicked(event -> System.exit(0));

        menu = new MenuBox("STARSHIPS",
                new MenuItem("RESUME GAME"),
                new MenuItem("NEW GAME"),
                exit);

        pane.getChildren().add(menu);

        return pane;
    }


    private Parent loadGame() throws IOException {
        ImageLoader imageLoader = new ImageLoader();

        BackgroundImage myBI= new BackgroundImage(imageLoader.loadFromResources("background.png", 1920, 1080),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);

        Image image = imageLoader.loadFromResources("starship.gif", 100.0, 100.0);
        ShipView shipView = new ShipView(image, 200, 200);
        Ship ship = new Ship(200.0, new SingleShooting(), new Rectangle(70, 45));
        ShipController shipController = new ShipController(shipView, ship);

        Pane pane = new Pane(shipView.getImageView());

        pane.setBackground(new Background(myBI));

        new MainTimer(shipController, new AsteroidController(), new BulletController(), context.getKeyTracker(), imageLoader, pane).start();

        return pane;
    }

    private static class MenuBox extends StackPane {
        public MenuBox(String title, MenuItem... items) {
            Rectangle bg = new Rectangle(300, 1080);
            bg.setOpacity(0.2);

            DropShadow shadow = new DropShadow(7, 5, 0, Color.BLACK);
            shadow.setSpread(0.8);

            bg.setEffect(shadow);

            Text text = new Text(title + " ");
            text.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
            text.setFill(Color.WHITE);

            Line hSep = new Line();
            hSep.setEndX(250);
            hSep.setStroke(Color.DARKRED);
            hSep.setOpacity(0.4);

            Line vSep = new Line();
            vSep.setStartX(300);
            vSep.setEndX(300);
            vSep.setEndY(1080);
            vSep.setStroke(Color.DARKRED);
            vSep.setOpacity(0.4);

            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_RIGHT);
            vBox.setPadding(new Insets(60, 0, 0, 0));
            vBox.getChildren().addAll(text, hSep);
            vBox.getChildren().addAll(items);

            setAlignment(Pos.TOP_RIGHT);
            getChildren().addAll(bg, vSep, vBox);
        }
    }

    private static class MenuItem extends StackPane {
        public MenuItem(String name) {
            Rectangle bg = new Rectangle(300, 24);

            LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.BLACK),
                    new Stop(0.2, Color.DARKGRAY));

            bg.setFill(gradient);
            bg.setVisible(false);
            bg.setEffect(new DropShadow(5, 0, 5, Color.BLACK));

            Text text = new Text(name + "    ");
            text.setFill(Color.LIGHTGRAY);
            text.setFont(Font.font(20));

            setAlignment(Pos.CENTER_RIGHT);
            getChildren().addAll(bg, text);

            setOnMouseEntered(event -> {
                bg.setVisible(true);
                text.setFill(Color.WHITE);
            });

            setOnMouseExited(event -> {
                bg.setVisible(false);
                text.setFill(Color.LIGHTGRAY);
            });

            setOnMousePressed(event -> {
                bg.setFill(Color.WHITE);
                text.setFill(Color.BLACK);
            });

            setOnMouseReleased(event -> {
                bg.setFill(gradient);
                text.setFill(Color.WHITE);
            });
        }
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
