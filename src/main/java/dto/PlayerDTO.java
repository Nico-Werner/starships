package dto;

import controller.ShipController;
import javafx.scene.input.KeyCode;
import lombok.Builder;
import lombok.Data;
import player.Player;

import java.io.Serializable;

@Data
@Builder
public class PlayerDTO implements Serializable {
    private int id;
    private int score;
    private int lives;
    private ShipControllerDTO shipController;

    private KeyCode keyForward;
    private KeyCode keyRotateLeft;
    private KeyCode keyBackward;
    private KeyCode keyRotateRight;
    private KeyCode keyShoot;

    public Player toPlayer() {
        ShipController shipController = this.shipController.toShipController();
        shipController.getShipView().updatePoints(score);
        return Player.builder()
                .id(id)
                .score(score)
                .lives(lives)
                .shipController(shipController)
                .keyForward(keyForward)
                .keyRotateLeft(keyRotateLeft)
                .keyBackward(keyBackward)
                .keyRotateRight(keyRotateRight)
                .keyShoot(keyShoot)
                .build();
    }
}
