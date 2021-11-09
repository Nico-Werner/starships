package player;

import controller.ShipController;
import dto.PlayerDTO;
import edu.austral.dissis.starships.game.KeyTracker;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class Player implements Serializable {
    private int id;
    private int score;
    private int lives;
    private ShipController shipController;

    KeyCode keyForward;
    KeyCode keyRotateLeft;
    KeyCode keyBackward;
    KeyCode keyRotateRight;
    KeyCode keyShoot;

    public void updateInput(Pane pane, KeyTracker keyTracker, double secondsSinceLastFrame) {
        keyTracker.getKeySet().forEach(keyCode -> {
            if (keyCode == keyForward) shipController.forward(secondsSinceLastFrame, pane);
            else if (keyCode == keyBackward) shipController.backward(secondsSinceLastFrame, pane);
            else if (keyCode == keyRotateLeft) shipController.rotateLeft(secondsSinceLastFrame);
            else if (keyCode == keyRotateRight) shipController.rotateRight(secondsSinceLastFrame);
            else if (keyCode == keyShoot) {
                shipController.fire(this);
                List<ImageView> imageViews = shipController.getBulletController().renderBullets();
                pane.getChildren().addAll(imageViews);
            }
        });
    }

    public void addPoints(double points) {
        score += points;
        updatePoints();
    }

    public void updatePoints() {
        shipController.getShipView().updatePoints(score);
    }

    public PlayerDTO toDTO() {
        return PlayerDTO.builder()
                .id(id)
                .score(score)
                .lives(lives)
                .shipController(shipController.toDTO())
                .keyForward(keyForward)
                .keyRotateLeft(keyRotateLeft)
                .keyBackward(keyBackward)
                .keyRotateRight(keyRotateRight)
                .keyShoot(keyShoot)
                .build();
    }
}
