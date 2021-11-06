package utils;

import controller.BulletController;
import controller.ShipController;
import edu.austral.dissis.starships.file.ImageLoader;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.shape.Rectangle;
import model.Ship;
import strategy.impl.SingleShooting;
import view.ShipView;

import java.io.IOException;
import java.util.List;

public class Config {
    private static final ImageLoader imageLoader = new ImageLoader();

    public static final int PLAYERS = 1;
    public static final int LIVES = 3;

    public static final KeyCode[][] PLAYER_KEYS = {{KeyCode.UP, KeyCode.LEFT, KeyCode.DOWN, KeyCode.RIGHT, KeyCode.SPACE}};
    public static ShipController[] PLAYER_SHIPS;

    static {
        try {
            PLAYER_SHIPS = new ShipController[]{new ShipController(new ShipView(imageLoader.loadFromResources("starship.gif", 100, 100), 200, 200), new Ship(200.0, new SingleShooting(), new Rectangle(70, 45)), new BulletController())};
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
